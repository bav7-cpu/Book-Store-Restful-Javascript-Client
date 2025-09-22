import React from "react";

// displays all the books with options to edit and delete from the "on".
export default function BookList({ books, onEdit, onDelete }) {
  if (books.length === 0) {
    return <p>No books.</p>;
  }

  // renders a table to show the book details and action buttons (update and delte)
  return (
    <table className="table table-striped">
      <thead>
        <tr>
          <th>ID</th><th>Title</th><th>Author</th><th>Date</th><th>Genres</th><th>Characters</th><th>Synopsis</th><th>Update</th><th>Delete</th>
        </tr>
      </thead>
      <tbody>
        {books.map(book => (
          <tr key={book.id || book.title}>
            <td>{book.id}</td>
            <td>{book.title}</td>
            <td>{book.author}</td>
            <td>{book.date}</td>
            <td>{book.genres}</td>
            <td>{book.characters}</td>
            <td>{book.synopsis}</td>
            <td>
              <button className="btn btn-success btn-sm me-2" onClick={() => onEdit(book)}>Update</button>
			</td>
			<td>
              <button className="btn btn-danger btn-sm" onClick={() => onDelete(book.id)}>Delete</button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
