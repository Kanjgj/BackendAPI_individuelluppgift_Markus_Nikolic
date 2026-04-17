package org.example;

import org.example.dto.AuthorDTO;
import org.example.dto.BookDTO;
import org.example.dto.LoanDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.example.entity.Loan;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LibraryApiIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    void createAuthorBookAndLoan() {

        // CREATE AUTHOR
        AuthorDTO author = new AuthorDTO();
        author.setName("Markus");

        ResponseEntity<String> authorResponse =
                restTemplate.postForEntity(url("/api/v1/authors"), author, String.class);

        assertThat(authorResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // CREATE BOOK
        BookDTO book = new BookDTO();
        book.setTitle("Test Book");
        book.setAuthor("Markus");
        book.setIsbn("123");
        book.setPublishedYear(2024);

        ResponseEntity<String> bookResponse =
                restTemplate.postForEntity(url("/api/v1/books"), book, String.class);

        assertThat(bookResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // CREATE LOAN (använd ID 1 eller 2 beroende på testordning)
        LoanDTO loan = new LoanDTO();
        loan.setBookId(2L);

        ResponseEntity<String> loanResponse =
                restTemplate.postForEntity(url("/api/v1/loans"), loan, String.class);

        assertThat(loanResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void shouldFailWhenLoaningSameBookTwice() {

        BookDTO book = new BookDTO();
        book.setTitle("Test");
        book.setAuthor("Markus");
        book.setIsbn("123");
        book.setPublishedYear(2024);

        restTemplate.postForEntity(url("/api/v1/books"), book, String.class);

        LoanDTO loan = new LoanDTO();
        loan.setBookId(1L);

        restTemplate.postForEntity(url("/api/v1/loans"), loan, String.class);

        ResponseEntity<String> secondLoan =
                restTemplate.postForEntity(url("/api/v1/loans"), loan, String.class);

        assertThat(secondLoan.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldReturn404WhenBookNotFound() {

        ResponseEntity<String> response =
                restTemplate.getForEntity(url("/api/v1/books/999"), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldOnlyCreateOneLoanUnderConcurrency() throws InterruptedException {

        // skapa book
        BookDTO book = new BookDTO();
        book.setTitle("Concurrent Book");
        book.setAuthor("Markus");
        book.setIsbn("123");
        book.setPublishedYear(2024);

        restTemplate.postForEntity(url("/api/v1/books"), book, String.class);

        int threads = 100;

        Thread[] threadList = new Thread[threads];

        for (int i = 0; i < threads; i++) {
            threadList[i] = new Thread(() -> {
                try {
                    LoanDTO loan = new LoanDTO();
                    loan.setBookId(1L);

                    restTemplate.postForEntity(url("/api/v1/loans"), loan, String.class);
                } catch (Exception ignored) {}
            });
        }

        for (Thread t : threadList) t.start();
        for (Thread t : threadList) t.join();

        ResponseEntity<Loan[]> response =
                restTemplate.getForEntity(url("/api/v1/loans"), Loan[].class);

        assertThat(response.getBody().length).isEqualTo(1);
    }
}