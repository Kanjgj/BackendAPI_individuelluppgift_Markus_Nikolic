package org.example.controller;

import org.example.dto.LoanDTO;
import org.example.entity.Loan;
import org.example.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<Loan> create(@RequestBody LoanDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(loanService.create(dto));
    }

    @GetMapping
    public List<Loan> getAll() {
        return loanService.getAll();
    }
}