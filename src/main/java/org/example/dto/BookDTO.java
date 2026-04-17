package org.example.dto;

import jakarta.validation.constraints.NotBlank;

public class BookDTO {

    @NotBlank
    private String title;

    @NotBlank
    private String author;

    private String isbn;
    private int publishedYear;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public int getPublishedYear() { return publishedYear; }
    public void setPublishedYear(int publishedYear) { this.publishedYear = publishedYear; }
}