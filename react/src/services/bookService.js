import axios from "axios";

const API_URL = "/book-api-controller";

// Helper to build payload based on selected format
const buildPayload = (book, format) => {
  if (format === "application/json") {
    return JSON.stringify(book);
  }

  if (format === "application/xml") {
    return `
<book>
  <id>${book.id || ""}</id>
  <title>${book.title}</title>
  <author>${book.author}</author>
  <date>${book.date}</date>
  <genres>${book.genres}</genres>
  <characters>${book.characters}</characters>
  <synopsis>${book.synopsis}</synopsis>
</book>`.trim();
  }

  if (format === "text/plain") {
    return `id: ${book.id || ""}
title: ${book.title}
author: ${book.author}
date: ${book.date}
genres: ${book.genres}
characters: ${book.characters}
synopsis: ${book.synopsis}`.trim();
  }

  return "";
};

// sends a GET request to fetch all books in the selected format
export const getBooks = (format) =>
  axios.get(API_URL, {
    headers: { Accept: format },
    responseType: format === "application/xml" ? "text" : "json"
  });

// sends a POST request to add a new book
export const addBook = (book, format) =>
  axios.post(API_URL, buildPayload(book, format), {
    headers: {
      "Content-Type": format,
      Accept: format
    },
    transformRequest: [(data) => data] // prevents axios from reformatting the payload
  });

// sends a PUT request to update an existing book
export const updateBook = (book, format) =>
  axios.put(API_URL, buildPayload(book, format), {
    headers: {
      "Content-Type": format,
      Accept: format
    },
    transformRequest: [(data) => data] // same here
  });

// sends a DELETE request to remove a book by ID
export const deleteBook = (id, format) => {
  let payload;

  if (format === "application/json") {
    payload = JSON.stringify({ id });
  } else if (format === "application/xml") {
    payload = `<book><id>${id}</id></book>`;
  } else {
    payload = `id: ${id}`;
  }

  return axios.delete(API_URL, {
    headers: {
      "Content-Type": format,
      Accept: format
    },
    data: payload,
    transformRequest: [(data) => data] // ensures raw data is used
  });
};
