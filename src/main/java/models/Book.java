// this class is the model class for the MVC application. It contains appropriate naming for the getters and setters as well as a toString method
package models;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Book {

	int id;
	String title;
	String author;
	String date;
	String genres;
	String characters;
	String synopsis;
	
	
	public Book(int id, String title, String author, String date, String genres, String characters, String synopsis) {

		this.id = id;
		this.title = title;
		this.author = author;
		this.date = date;
		this.genres = genres;
		this.characters = characters;
		this.synopsis = synopsis;
		
	}
	
	public Book(String title, String author, String date, String genres, String characters, String synopsis) {
		super();
		this.title = title;
		this.author = author;
		this.date = date;
		this.genres = genres;
		this.characters = characters;
		this.synopsis = synopsis;
	}

	public Book(int id) {

		this.id = id;
		
	}
	public Book() {}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getGenres() {
		return genres;
	}

	public void setGenres(String genres) {
		this.genres = genres;
	}

	public String getCharacters() {
		return characters;
	}

	public void setCharacters(String characters) {
		this.characters = characters;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", title=" + title + ", author=" + author + ", date=" + date + ", genres=" + genres
				+ ", synnopsis=" + synopsis + ", characters=" + characters + "]";
	}
}
