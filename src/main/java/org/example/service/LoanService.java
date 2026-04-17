package org.example.service;

import org.example.dto.LoanDTO;
import org.example.entity.Book;
import org.example.entity.Loan;
import org.example.repository.BookRepository;
import org.example.repository.LoanRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;

    public LoanService(LoanRepository loanRepository, BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public synchronized Loan create(LoanDTO dto) {

        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));

        loanRepository.findByBookIdAndReturnDateIsNull(book.getId())
                .ifPresent(l -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book already loaned");
                });

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setLoanDate(LocalDate.now());

        return loanRepository.save(loan);
    }

    public List<Loan> getAll() {
        return loanRepository.findAll();
    }
}