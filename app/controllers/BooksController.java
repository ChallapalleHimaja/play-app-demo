package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Book;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.HashMap;
import java.util.Map;

public class BooksController extends Controller {

    int index = 0;
    final Map<Integer, Book> books = new HashMap<>();

    public Result createBook() {

        final JsonNode json = request().body().asJson();

        final Book book = Json.fromJson(json, Book.class);

        if (null == book.getTitle()) {
            return badRequest("Title must be provided");
        }

        book.setId(index++);
        books.put(book.getId(), book);

        final JsonNode result = Json.toJson(book);

        return ok(result);
    }

    public Result getBookById(Integer id) {

        if (null == id) {
            return badRequest("Id must be provided");
        }

        final Book book = books.get(id);
        if (null == book) {
            return notFound();
        }

        final JsonNode result = Json.toJson(book);

        return ok(result);
    }

    public Result updateBookById(Integer id) {

        if (null == id) {
            return badRequest("Id must be provided");
        }

        final Book existingBook = books.get(id);
        if (null == existingBook) {
            return notFound();
        }

        final JsonNode json = request().body().asJson();
        final Book newBook = Json.fromJson(json, Book.class);

        existingBook.setTitle(newBook.getTitle());
        books.put(existingBook.getId(), existingBook);

        final JsonNode result = Json.toJson(existingBook);
        return ok(result);
    }

    public Result deleteBookById(Integer id) {

        // TODO Missing implementation

        return ok("Delete book");
    }

    public Result getAllBooks() {

        final JsonNode result = Json.toJson(books.values());

        return ok(result);
    }

}
