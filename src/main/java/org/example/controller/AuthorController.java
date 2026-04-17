package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.AuthorDTO;
import org.example.dto.BookDTO;
import org.example.entity.Author;
import org.example.service.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    public ResponseEntity<AuthorDTO> create(@Valid @RequestBody AuthorDTO dto) {
        Author author = authorService.create(dto);

        AuthorDTO response = new AuthorDTO();
        response.setName(author.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public AuthorDTO get(@PathVariable Long id) {
        Author author = authorService.get(id);

        AuthorDTO dto = new AuthorDTO();
        dto.setName(author.getName());

        return dto;
    }

    @GetMapping("/{id}/books")
    public List<BookDTO> getBooks(@PathVariable Long id) {
        Author author = authorService.get(id);

        return author.getBooks().stream().map(book -> {
            BookDTO dto = new BookDTO();
            dto.setTitle(book.getTitle());
            dto.setAuthor(book.getAuthor());
            dto.setIsbn(book.getIsbn());
            dto.setPublishedYear(book.getPublishedYear());
            return dto;
        }).toList();
    }
}