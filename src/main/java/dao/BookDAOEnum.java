//this DAO class connects to the DB on MySQL Workbench. It also contains methods that allow for full CRUD functionality in the application
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import models.Book;

public enum BookDAOEnum {
	INSTANCE;
	
	Book oneBook = null;
	Connection c = null;
	Statement s = null;
	PreparedStatement preparedStatement = null;
	ResultSet r = null;
	
	String user = "singhbav";
    String password = "foZelind5";
    // Note none default port used, 6306 not 3306
    String url = "jdbc:mysql://mudfoot.doc.stu.mmu.ac.uk:6306/" + user;

 private BookDAOEnum() {
 }
 
 /**
	 * Get Database Connection
	 * 
	 * @return Statement Object
	 */

 private Statement getConnection() {

		// loading jdbc driver for mysql
		try {
			Class.forName("com.mysql.jdbc.Driver").getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			System.out.println(e);
		}

		// connecting to database
		try {
			c = DriverManager.getConnection(url, user, password);
			s = c.createStatement();
		} catch (SQLException se) {
			System.out.println(se);
		}
		return s;
	}

	/**
	 * Close any open database connection
	 * 
	 */
	private void closeConnection() {
		try {
			if (s != null) {
				s.close();
			}
			if (c != null) {
				c.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	private Book getNextBook(ResultSet rs){
 	Book thisBook=null;
		try {
			
			thisBook = new Book(
					rs.getInt("id"),
					rs.getString("title"),
					rs.getString("author"),
					rs.getString("date"),
					rs.getString("genres"),
					rs.getString("characters"),
					rs.getString("synopsis"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
 	return thisBook;		
	}
	
	// the following method will retrieve all the books in the books table in the DB
	public ArrayList<Book> getAllBooks() {
	    ArrayList<Book> allBooks = new ArrayList<Book>();
	try {
		String sql = "select * from  books";
		ResultSet rs = getConnection().executeQuery(sql);
		
		if (rs!=null) {
			 while (rs.next()) {
				 
		            Book book = new Book();
		            try {
		            book.setId(rs.getInt("id")); 
		            book.setTitle(rs.getString("title"));
		            book.setAuthor(rs.getString("author")); 
		            book.setDate(rs.getString("date"));
		            book.setGenres(rs.getString("genres"));
		            book.setCharacters(rs.getString("characters"));
		            book.setSynopsis(rs.getString("synopsis"));
		            System.out.println(book.getTitle());
		            
		            } catch (SQLException s) {
						s.printStackTrace();
					
		            }
		            allBooks.add(book);
			 }
			 rs.close();
		}
	} catch (SQLException s) {
		System.out.println(s);
	}
	closeConnection();
	return allBooks;
	}

	 // this method retrieves a single book by its ID
public Book getBookByID(int id){
	   
		getConnection();
		oneBook=null;
	    // Create select statement and execute it
		try{
		    String selectSQL = "select * from books where id="+id;
		    ResultSet rs = s.executeQuery(selectSQL);
	    // Retrieve the results
		    while(rs.next()){
		    	oneBook = getNextBook(rs);
		    }

		    s.close();
		    closeConnection();
		} catch(SQLException se) { System.out.println(se); }

	   return oneBook;
}
//this method selects a book record by ID and returns it
public Book selectBooks(int id) {

		Book b = new Book();

		try {
			String sql = "select * from  books where id = " + id + ";";
			ResultSet rs = getConnection().executeQuery(sql);

			if (rs != null) {
				while (rs.next()) {

					try {

						b.setId(rs.getInt("Id"));
						b.setTitle(rs.getString("Title"));
						b.setAuthor(rs.getString("Author"));
						b.setDate(rs.getString("Date"));
						b.setGenres(rs.getString("Genres"));
						b.setCharacters(rs.getString("Characters"));
						b.setSynopsis(rs.getString("Synopsis"));

					} catch (SQLException s) {
						s.printStackTrace();
					}

				}

				rs.close();
				return b;
			}
		} catch (SQLException s) {
			System.out.println(s);
		}

		closeConnection();
		return null;
	}

//this method inserts a new book into the database
public boolean insertBook(Book b) throws SQLException {
	    boolean d = false;

	    try {
	    	getConnection();
	    	String sql = "insert into books (Title, Author, Date, Genres, Characters, Synopsis) values (?,?,?,?,?,?);";
	    	preparedStatement = c.prepareStatement(sql);
	    	preparedStatement.setString(1, b.getTitle());
	    	preparedStatement.setString(2, b.getAuthor());
	    	preparedStatement.setString(3, b.getDate());
	    	preparedStatement.setString(4, b.getGenres());
	    	preparedStatement.setString(5, b.getCharacters());
	    	preparedStatement.setString(6, b.getSynopsis());
	    	int rowsAffected = preparedStatement.executeUpdate();
	    	preparedStatement.close();
	    	if (rowsAffected > 0) {
             d = true;
	    	}
	    } catch (SQLException e) {
	        d = false;
	        e.printStackTrace();
	    }

	    return d;
	}


//this method updates an existing book record in the database  
public boolean updateBook(Book b) throws SQLException {
	    boolean update = false;
	    
	    try {
	        getConnection();
	        String sql = "update books set Title = ?, Author = ?, Date = ?, Genres = ?, Characters = ?, Synopsis = ? where id = ?";
		    preparedStatement = c.prepareStatement(sql);
		    preparedStatement.setString(1, b.getTitle());
		    preparedStatement.setString(2, b.getAuthor());
		    preparedStatement.setString(3, b.getDate());
		    preparedStatement.setString(4, b.getGenres());
		    preparedStatement.setString(5, b.getCharacters());
		    preparedStatement.setString(6, b.getSynopsis());
		    preparedStatement.setInt(7, b.getId());

	        int rowsAffected = preparedStatement.executeUpdate();
	        System.out.println("Rows affected: " + rowsAffected);

	        update = (rowsAffected > 0);

	    } catch (SQLException e) {
	        System.err.println("Error updating book: " + e.getMessage());
	        throw new SQLException("Error updating book: " + e.getMessage());
	    }

	    return update;
	}

//this method deletes a book record from the database
	public boolean deleteBook(Book b) throws SQLException {
		boolean delete = false;
    try {
 	   getConnection();
 	   String sql = "delete from books where id = ?";
 	   PreparedStatement stmt = c.prepareStatement(sql);
    	   stmt.setInt(1, b.getId());
    	   int rowsAffected = stmt.executeUpdate();
    	   stmt.close();
	       System.out.println("Rows affected: " + rowsAffected);
	       if (rowsAffected ==1) {
	    	   delete = true;
	       }
    } catch (SQLException e) {
        throw new SQLException("Error deleting book: " + e.getMessage());
    }

    return delete;
}


	// this method searches for books matching a keyword across all the fields
	public ArrayList<Book> searchBook(String searchBooks) {
		  String sql = "select * from books where id like ? or title like ? or author like ? or date like ? or genres like ? or characters like ? or synopsis like ?"; 
		  //% sign is needed to do partial matches 
		  String userInput = "%" + searchBooks + "%";
	      ArrayList<Book> results = new ArrayList<>();

	    try {
	        getConnection();
	        try (PreparedStatement stmt = c.prepareStatement(sql)) {
	        	stmt.setString(1, userInput);
	        	stmt.setString(2, userInput);
	        	stmt.setString(3, userInput);
	        	stmt.setString(4, userInput);
	        	stmt.setString(5, userInput);
	        	stmt.setString(6, userInput);
	        	stmt.setString(7, userInput);
	            try (ResultSet rs = stmt.executeQuery()) {
	                while (rs.next()) {
	                    results.add(getNextBook(rs));
	                }
	            }
	        }
	    } catch (SQLException e) {
	        System.out.println("Search error: " + e.getMessage());
	    } finally {
	        closeConnection();
	    }

	    return results;
	}
	
	
	// this method retrieves books for a specific page and page size for pagination
	public ArrayList<Book> getBooksByPage(int page, int pageSize) {
	    String sql = "select * from books limit ? offset ?";
	    ArrayList<Book> books = new ArrayList<>();

	    
	    try {
	        getConnection();
	        try (
	            PreparedStatement stmt = c.prepareStatement(sql)
	        ) {
	            stmt.setInt(1, pageSize);
	            stmt.setInt(2, (page - 1) * pageSize); //finds the starting position based on the current page
	            try (ResultSet rs = stmt.executeQuery()) {
	                while (rs.next()) books.add(getNextBook(rs));
	            }
	        }
	    } catch (SQLException e) {
	        System.out.println("Pagination error: " + e.getMessage());
	    } finally {
	        closeConnection();
	    }

	    return books;
	}

	// this method gets the total number of books in the database
	public int getBookCount() {
	    String sql = "select count(*) from books";
	    try {
	        getConnection();
	        try (ResultSet rs = s.executeQuery(sql)) {
	        	if (rs.next()) {
	        	    return rs.getInt(1);
	        	} else {
	        	    return 0;
	        	}

	        }
	    } catch (SQLException e) {
	        System.out.println("Error getting book count: " + e.getMessage());
	    } finally {
	        closeConnection();
	    }

	    return 0;
	}



}


