package models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Book {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("title")
    private String title;

    //private Collection<Author> author;

    public Book(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    public Book() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
