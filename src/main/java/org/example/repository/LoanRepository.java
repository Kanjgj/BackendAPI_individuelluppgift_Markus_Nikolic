package org.example.repository;

import org.example.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    Optional<Loan> findByBookIdAndReturnDateIsNull(Long bookId);
}