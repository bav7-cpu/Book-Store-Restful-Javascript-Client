package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import models.Book;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "books")
public class BookList {
@XmlElement(name = "book")
private List<Book> bookList;
public BookList() {}
public BookList(List<Book> bookList) {
this.bookList = bookList;
}
public List<Book> getBooksList() {
return bookList;
}
public void setBooksList(List<Book> bookList) {
this.bookList = bookList;
}
}
