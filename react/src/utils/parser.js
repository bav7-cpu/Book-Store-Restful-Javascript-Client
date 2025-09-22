export function parseBooks(data, format) {
  if (format === "application/json") {
    return data;
  }

  // if format is in xml, it is parsed into a DOM to get the book information
  if (format === "application/xml") {
    const parser = new DOMParser(); // turns it into a dom object
    const xml = parser.parseFromString(data, "application/xml");
    const books = xml.getElementsByTagName("book");

    return Array.from(books).map((map) => ({
      id: map.querySelector("id")?.textContent,
      title: map.querySelector("title")?.textContent,
      author: map.querySelector("author")?.textContent,
      date: map.querySelector("date")?.textContent,
      genres: map.querySelector("genres")?.textContent,
      characters: map.querySelector("characters")?.textContent,
      synopsis: map.querySelector("synopsis")?.textContent
    }));
  }
// if format is text, it will get the book information in text format
  if (format === "text/plain") {
    const lines = data.split("\n").filter((line) => line.includes("Book ["));
    return lines.map((line, index) => {
      const extract = (key) => {
        const match = line.match(new RegExp(`${key}=([^,\\]]+)`));
        return match ? match[1].trim() : "";
      };

      return {
        id: extract("id") || index + 1,
        title: extract("title"),
        author: extract("author"),
        date: extract("date"),
        genres: extract("genres"),
        characters: extract("characters"),
        synopsis: extract("synnopsis") 
      };
    });
  }

  return [];
}
