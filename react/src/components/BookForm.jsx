import React, { useState, useEffect } from "react";

// this component displays a form for adding or updating a book in the application
function BookForm({ book, onSave, onCancel }) {
  const [form, setForm] = useState({
    title: "", author: "", date: "", genres: "", characters: "", synopsis: ""
  });

  // loads book data into the form if updating a book
  useEffect(() => {
    if (book) setForm(book);
  }, [book]);

  // updates the form when input fields change
  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  // habdles form submissions to saves a new or updated book
  function submit(e) {
    e.preventDefault();
    if (!form.title || !form.author) {
      alert("Title and author are required.");
      return;
    }
    onSave(form);
    setForm({ title: "", author: "", date: "", genres: "", characters: "", synopsis: "" });
  }

  return (
    <form onSubmit={submit} className="mb-4">
      <input name="title" value={form.title} onChange={handleChange} className="form-control mb-2" placeholder="Title" />
      <input name="author" value={form.author} onChange={handleChange} className="form-control mb-2" placeholder="Author" />
      <input name="date" value={form.date} onChange={handleChange} className="form-control mb-2" placeholder="Date" />
      <input name="genres" value={form.genres} onChange={handleChange} className="form-control mb-2" placeholder="Genres" />
      <input name="characters" value={form.characters} onChange={handleChange} className="form-control mb-2" placeholder="Characters" />
      <input name="synopsis" value={form.synopsis} onChange={handleChange} className="form-control mb-2" placeholder="Synopsis" />
      <button className="btn btn-primary me-2" type="submit">
        {form.id ? "Update" : "Add"} Book
      </button>
      {book && <button className="btn btn-secondary" type="button" onClick={onCancel}>Cancel</button>}
    </form>
  );
}

export default BookForm;
