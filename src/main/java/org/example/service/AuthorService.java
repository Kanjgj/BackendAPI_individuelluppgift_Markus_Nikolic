package org.example.service;

import org.example.dto.AuthorDTO;
import org.example.entity.Author;
import org.example.repository.AuthorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author create(AuthorDTO dto) {
        Author author = new Author();
        author.setName(dto.getName());
        return authorRepository.save(author);
    }

    public Author get(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));
    }
}