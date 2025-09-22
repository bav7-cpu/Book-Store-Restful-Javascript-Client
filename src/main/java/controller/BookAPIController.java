// this controller class connects to the DAO and handles HTTP requests for full CRUD functionality in the application
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import dao.BookDAOEnum;
import jakarta.xml.bind.*;
import models.Book;
import controller.BookList;

@WebServlet("/book-api-controller")
public class BookAPIController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BookDAOEnum dao;

 // this constructor initialises the DAO instance
    public BookAPIController() {
        this.dao = BookDAOEnum.INSTANCE;
    }
 // this method formats the HTTP response based on the requested content type
    private void formatResponse(HttpServletResponse response, String message, String format) throws IOException {
        PrintWriter out = response.getWriter();
        response.setCharacterEncoding("UTF-8");

        if ("application/xml".equals(format)) {
            response.setContentType("application/xml");
            out.write("<response><message>" + message + "</message></response>");
        } else if ("text/plain".equals(format)) {
            response.setContentType("text/plain");
            out.write(message);
        } else {
            response.setContentType("application/json");
            out.write("{ \"message\": \"" + message + "\" }");
        }

        out.close();
    }
    
 // parses plain text body into a book object for the doPost and doPut methods
    private Book parsePlainText(String body, boolean requireId) {
        String[] lines = body.split("\n");
        int id = 0;
        String title = "", author = "", date = "", genres = "", characters = "", synopsis = "";

        for (String line : lines) {
            String[] parts = line.split(":", 2); // literally splits the text into e.g id : 2
            if (parts.length == 2) { // makes sure that a key and value exists
                String key = parts[0].trim().toLowerCase(); // removes extra space and turns into lowercase
                String value = parts[1].trim(); // gets the value and trims the unneccessary spaces 
                switch (key) { // matches the key and stores the value in the correct variable 
                    case "id": id = Integer.parseInt(value); break;
                    case "title": title = value; break;
                    case "author": author = value; break;
                    case "date": date = value; break;
                    case "genres": genres = value; break;
                    case "characters": characters = value; break;
                    case "synopsis": synopsis = value; break;
                }
            }
        }
// if id is required than it is true, otherwise it is false
        return requireId ? new Book(id, title, author, date, genres, characters, synopsis) : new Book(title, author, date, genres, characters, synopsis);
    }


    // this method handles GET requests and returns all books in the requested format
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");

        PrintWriter out = response.getWriter();
        ArrayList<Book> allBooks = dao.getAllBooks();
        BookList bl = new BookList(allBooks);
        String format = request.getHeader("Accept");

        if ("application/xml".equals(format)) {
            try {
                response.setContentType("application/xml");
                response.setCharacterEncoding("UTF-8");

                JAXBContext jaxbContext = JAXBContext.newInstance(BookList.class);
                Marshaller marshaller = jaxbContext.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                marshaller.marshal(bl, out);
            } catch (JAXBException e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error generating XML");
            }
        } else if ("text/plain".equals(format)) {
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            for (Book book : allBooks) {
                out.println(book.toString());
            }
        } else {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.write(new Gson().toJson(allBooks));
        }

        out.close();
    }

 // this method handles POST requests to insert a new book
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	request.setCharacterEncoding("UTF-8");
    	response.setCharacterEncoding("UTF-8");

        String format = request.getHeader("Accept");
        String contentType = request.getContentType();
        response.setCharacterEncoding("UTF-8");

        try {
        	//reads the full body of the request within a single string
            String body = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual + "\n");
            //easier way to get the raw data to send through the request body
            Book book = null;

            // book object is created using GSON and JSON
            if ("application/json".equals(contentType)) {
                book = new Gson().fromJson(body, Book.class);

             // book object is created using XML and JAXB
            } else if ("application/xml".equals(contentType)) {
                JAXBContext context = JAXBContext.newInstance(Book.class);
                book = (Book) context.createUnmarshaller().unmarshal(new StringReader(body));

            } else if ("text/plain".equals(contentType)) {
                book = parsePlainText(body, false); 
                // false because id isn't required
            }


            if (book == null || book.getTitle().isEmpty() || book.getAuthor().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                formatResponse(response, "Error: Required fields missing.", format);
                return;
            }

            dao.insertBook(book);
            formatResponse(response, "Book inserted successfully", format);

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            formatResponse(response, "Error processing request", format);
        }
    }

 // this method handles PUT requests to update an existing book
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	request.setCharacterEncoding("UTF-8");
    	response.setCharacterEncoding("UTF-8");

        String format = request.getHeader("Accept");
        String contentType = request.getContentType();
        response.setCharacterEncoding("UTF-8");

        try {
            String body = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual + "\n");
            Book book = null;

            if ("application/json".equals(contentType)) {
                book = new Gson().fromJson(body, Book.class);

            } else if ("application/xml".equals(contentType)) {
                JAXBContext context = JAXBContext.newInstance(Book.class);
                book = (Book) context.createUnmarshaller().unmarshal(new StringReader(body));

            } else if ("text/plain".equals(contentType)) {
                book = parsePlainText(body, true);
             // true because id is required
            }


            if (book == null || book.getId() == 0 || book.getTitle().isEmpty() || book.getAuthor().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                formatResponse(response, "Error: All fields including ID must be provided and non-empty.", format);
                return;
            }

            dao.updateBook(book);
            formatResponse(response, "Book updated successfully", format);

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            formatResponse(response, "Error processing request", format);
        }
    }
    // this method handles DELETE requests to delete a book by ID
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String format = request.getHeader("Accept");
        String contentType = request.getContentType();
        response.setCharacterEncoding("UTF-8");

        try {
            String body = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual + "\n");
            int id = 0;

            if ("application/json".equals(contentType)) {
                JsonObject json = new Gson().fromJson(body, JsonObject.class);
                id = json.has("id") ? json.get("id").getAsInt() : 0;

            } else if ("application/xml".equals(contentType)) {
                JAXBContext context = JAXBContext.newInstance(Book.class);
                Book book = (Book) context.createUnmarshaller().unmarshal(new StringReader(body));
                id = book.getId();

            } else if ("text/plain".equals(contentType)) {
                String[] lines = body.split("\n");
                for (String line : lines) {
                    String[] parts = line.split(":", 2);
                    if (parts.length == 2 && parts[0].trim().equalsIgnoreCase("id")) {
                        id = Integer.parseInt(parts[1].trim());
                    }
                }
            }

            if (id == 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                formatResponse(response, "Error: ID must be provided.", format);
                return;
            }

            Book book = new Book(id);
            dao.deleteBook(book);
            formatResponse(response, "Book deleted successfully", format);

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            formatResponse(response, "Error: ID must be a valid integer.", format);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            formatResponse(response, "Error processing request", format);
        }
    }
}
