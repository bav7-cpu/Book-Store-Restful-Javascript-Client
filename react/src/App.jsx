import React, { useState, useEffect } from "react"; //initialized the hooks
import { getBooks, addBook, updateBook, deleteBook } from "./services/bookService";
import { parseBooks } from "./utils/parser";
import BookForm from "./components/BookForm";
import BookList from "./components/BookList";
import FormatSelector from "./components/FormatSelector";

const booksPerPage = 10;

function App() {
  const [books, setBooks] = useState([]);
  const [format, setFormat] = useState("application/json");
  const [search, setSearch] = useState("");
  const [editBook, setEditBook] = useState(null);
  const [page, setPage] = useState(1);
  const [jumpPage, setJumpPage] = useState("");
  const [showAddModal, setShowAddModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);

  // fetches books whenever the data format is changed
  useEffect(() => {
    fetchBooks();
  }, [format]);

  // resets to page 1 when the search query changes
  useEffect(() => {
    setPage(1);
  }, [search]);

  // fetches books from the server
  const fetchBooks = () => {
    getBooks(format)
      .then(res => {
        const parsed = parseBooks(res.data, format);
        setBooks(parsed);
        setPage(1);
      })
      .catch(console.error);
  };
  
//saves a new book or updates an existing book
  const saveBook = (book) => {
    const method = book.id ? updateBook : addBook;
    method(book, format)
      .then(() => {
        alert(book.id ? "Book updated!" : "Book added!");
        fetchBooks();
        setEditBook(null);
        setShowAddModal(false);
        setShowEditModal(false);
      })
      .catch(console.error);
  };
  
//deletes a book by ID
  const removeBook = (id) => {
    deleteBook(id, format)
      .then(() => {
        alert("Book deleted!");
        fetchBooks();
      })
      .catch(console.error);
  };

  //filters books based on the search input
  const filtered = books.filter(b =>
    b.title?.toLowerCase().includes(search.toLowerCase()) ||
    b.author?.toLowerCase().includes(search.toLowerCase()) ||
    b.genres?.toLowerCase().includes(search.toLowerCase())
  );

  const totalPages = (filtered.length + booksPerPage - 1) / booksPerPage | 0;
  const visibleBooks = filtered.slice((page - 1) * booksPerPage, page * booksPerPage);

  return (
    <div className="container mt-4">
      <h2>Bav's Books</h2>

      <FormatSelector format={format} setFormat={setFormat} />

      <input
        type="text"
        className="form-control my-3"
        placeholder="Search title, author or genre..."
        value={search}
        onChange={(e) => setSearch(e.target.value)}
      />

      <div className="text-end mb-3">
        <button className="btn btn-primary" onClick={() => setShowAddModal(true)}>
          Add Book
        </button>
      </div>

      <BookList
        books={visibleBooks}
        onEdit={(book) => {
          setEditBook(book);
          setShowEditModal(true);
        }}
        onDelete={removeBook}
      />
	  {/* renders pagination controls */}
      {totalPages > 1 && (
        <>
          <ul className="pagination justify-content-center">
            <li className={`page-item ${page === 1 ? "disabled" : ""}`}>
              <button className="page-link" onClick={() => setPage(page - 1)}>Prev</button>
            </li>

            {(() => {
              const maxShown = 5;
              let start = Math.max(page - Math.floor(maxShown / 2), 1);
              let end = Math.min(start + maxShown - 1, totalPages);
              start = Math.max(end - maxShown + 1, 1);

              return Array.from({ length: end - start + 1 }, (_, i) => {
                const p = start + i;
                return (
                  <li key={p} className={`page-item ${p === page ? "active" : ""}`}>
                    <button className="page-link" onClick={() => setPage(p)}>{p}</button>
                  </li>
                );
              });
            })()}

            <li className={`page-item ${page === totalPages ? "disabled" : ""}`}>
              <button className="page-link" onClick={() => setPage(page + 1)}>Next</button>
            </li>
          </ul>

          <div className="d-flex justify-content-center align-items-center mt-2">
            <input
              type="number"
              className="form-control form-control-sm w-auto me-2"
              placeholder="Page"
              value={jumpPage}
              onChange={(e) => setJumpPage(e.target.value)}
            />
            <button
              className="btn btn-primary btn-sm"
              onClick={() => {
                const num = parseInt(jumpPage);
                if (!isNaN(num) && num >= 1 && num <= totalPages) {
                  setPage(num);
                  setJumpPage("");
                } else {
                  alert(`Enter a number between 1 and ${totalPages}`);
                }
              }}
            >
              Go
            </button>
          </div>
        </>
      )}

      {/* renders the modal for adding a new book */}
      {showAddModal && (
        <>
          <div className="modal fade show d-block" tabIndex="-1">
            <div className="modal-dialog">
              <div className="modal-content">
                <div className="modal-header">
                  <h5 className="modal-title">Add Book</h5>
                  <button className="btn-close" onClick={() => setShowAddModal(false)}></button>
                </div>
                <div className="modal-body">
                  <BookForm
                    book={null}
                    onSave={saveBook}
                    onCancel={() => setShowAddModal(false)}
                  />
                </div>
              </div>
            </div>
          </div>
          <div className="modal-backdrop fade show"></div>
        </>
      )}

      {/* renders the modal for editing an existing book */}
      {showEditModal && editBook && (
        <>
          <div className="modal fade show d-block" tabIndex="-1">
            <div className="modal-dialog">
              <div className="modal-content">
                <div className="modal-header">
                  <h5 className="modal-title">Edit Book</h5>
                  <button className="btn-close" onClick={() => setShowEditModal(false)}></button>
                </div>
                <div className="modal-body">
                  <BookForm
                    book={editBook}
                    onSave={saveBook}
                    onCancel={() => setShowEditModal(false)}
                  />
                </div>
              </div>
            </div>
          </div>
          <div className="modal-backdrop fade show"></div>
        </>
      )}
    </div>
  );
}

export default App;
