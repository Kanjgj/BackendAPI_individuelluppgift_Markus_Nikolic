package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.BookDTO;
import org.example.entity.Book;
import org.example.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<BookDTO> create(@Valid @RequestBody BookDTO dto) {
        Book book = bookService.createBook(dto);

        BookDTO response = new BookDTO();
        response.setTitle(book.getTitle());
        response.setAuthor(book.getAuthor());
        response.setIsbn(book.getIsbn());
        response.setPublishedYear(book.getPublishedYear());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<BookDTO> getAll() {
        return bookService.getAllBooks().stream().map(book -> {
            BookDTO dto = new BookDTO();
            dto.setTitle(book.getTitle());
            dto.setAuthor(book.getAuthor());
            dto.setIsbn(book.getIsbn());
            dto.setPublishedYear(book.getPublishedYear());
            return dto;
        }).toList();
    }

    @GetMapping("/{id}")
    public BookDTO getById(@PathVariable Long id) {
        Book book = bookService.getBook(id);

        BookDTO dto = new BookDTO();
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setIsbn(book.getIsbn());
        dto.setPublishedYear(book.getPublishedYear());

        return dto;
    }
}