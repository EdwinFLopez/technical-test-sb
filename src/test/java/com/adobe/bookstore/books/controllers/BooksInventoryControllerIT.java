package com.adobe.bookstore.books.controllers;

import com.adobe.bookstore.books.domains.Book;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BooksInventoryControllerIT {

    @Autowired TestRestTemplate tpl;

    @Test
    public void shouldHaveBooksWhenListed() {
        ResponseEntity<List> booksRs = tpl.getForEntity("/books-inventory", List.class);
        assertEquals(HttpServletResponse.SC_OK, booksRs.getStatusCode(), "Invalid response code");
        assertNotNull(booksRs.getBody(), "No body response found");
        assertTrue(booksRs.getBody().size() > 0, "No books found. Check the flyway initialization");
    }

    @Test
    public void shouldFindBookWhenGivenExistentBookId() {
        var bookId = "ae1666d6-6100-4ef0-9037-b45dd0d5bb0e";
        var bookName = "adipisicing culpa Lorem laboris adipisicing";
        var availableQuantity = 0;

        var bookRs = tpl.getForEntity("/books-inventory/" + bookId, Book.class);
        var body = bookRs.getBody();

        assertEquals(HttpStatus.OK, bookRs.getStatusCode(), "Invalid response code");
        assertNotNull(body, "No body response found");
        assertEquals(bookId, body.getBookId(), "Invalid Book Id");
        assertEquals(bookName, body.getBookName(), "Invalid Book Name");
        assertEquals(availableQuantity, body.getQuantity(), "Invalid expected stock");
    }

    @Test
    public void shouldReturnNoBodyWhenGivenNonExistentBookId() {
        var bookRs = tpl.getForEntity("/books-inventory/invalid-book-id-000001", Book.class);
        var body = bookRs.getBody();
        assertEquals(HttpStatus.OK, bookRs.getStatusCode(), "Invalid response code");
        assertNull(body, "Invalid body response found");
    }
}
