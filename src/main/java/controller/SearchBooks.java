package controller;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import dao.BookDAOEnum;
import models.Book;

/**
 * Servlet implementation class SearchBooks
 */
@WebServlet("/searchbooks")
public class SearchBooks extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private BookDAOEnum dao;

	public SearchBooks() {
		this.dao = BookDAOEnum.INSTANCE;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String searchBooks = request.getParameter("searchBooks");
		Collection<Book> searchBooksResults;

		if (searchBooks == null || searchBooks.trim().isEmpty()) {
			searchBooksResults = java.util.Collections.emptyList(); 
		} else {
			searchBooksResults = dao.searchBook(searchBooks.trim());
		}

		request.setAttribute("searchBooksResults", searchBooksResults);
		RequestDispatcher rd = request.getRequestDispatcher("searchbook.jsp");
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
