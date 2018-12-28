package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Book;
import play.Logger;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import javax.persistence.TypedQuery;
import java.util.List;

public class BooksController extends Controller {

    private final static Logger.ALogger LOGGER = Logger.of(BooksController.class);

    final JPAApi jpaApi;

    @Inject
    public BooksController(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    @Transactional
    public Result createBook() {

        final JsonNode json = request().body().asJson();

        final Book book = Json.fromJson(json, Book.class);

        LOGGER.debug("Book title = " + book.getTitle());
        LOGGER.error("This is an error");

        if (null == book.getTitle()) {
            return badRequest("Title must be provided");
        }

        jpaApi.em().persist(book);

        final JsonNode result = Json.toJson(book);

        return ok(result);
    }

    @Transactional
    public Result getBookById(Integer id) {

        if (null == id) {
            return badRequest("Id must be provided");
        }

        final Book book = jpaApi.em().find(Book.class, id);
        if (null == book) {
            return notFound();
        }

        final JsonNode result = Json.toJson(book);

        return ok(result);
    }

    @Transactional
    public Result updateBookById(Integer id) {

        if (null == id) {
            return badRequest("Id must be provided");
        }

        final Book existingBook = jpaApi.em().find(Book.class, id);
        if (null == existingBook) {
            return notFound();
        }

        final JsonNode json = request().body().asJson();
        final Book newBook = Json.fromJson(json, Book.class);

        existingBook.setTitle(newBook.getTitle());
        jpaApi.em().persist(existingBook);

        final JsonNode result = Json.toJson(existingBook);
        return ok(result);
    }

    @Transactional
    public Result deleteBookById(Integer id) {

        if (null == id) {
            return badRequest("Id must be provided");
        }

        final Book book = jpaApi.em().find(Book.class, id);
        if (null == book) {
            return notFound();
        }

        jpaApi.em().remove(book);

        final JsonNode result = Json.toJson(book);
        return ok(result);
    }

    @Transactional
    public Result getAllBooks() {

        TypedQuery<Book> query = jpaApi.em().createQuery("SELECT b FROM Book b", Book.class);
        List<Book> books = query.getResultList();

        final JsonNode result = Json.toJson(books);

        return ok(result);
    }

}









