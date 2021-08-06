package com.adobe.bookstore.books.controllers;

import com.adobe.bookstore.books.domains.Book;
import com.adobe.bookstore.books.services.BooksRepository;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/books-inventory")
public class BooksInventoryController {

    private final BooksRepository bookRepository;

    public BooksInventoryController(BooksRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public List<Book> list() {
        List<Book> books = new ArrayList<>();
        bookRepository.findAll().forEach(books::add);
        return books;
    }

    @GetMapping("/{bookId}")
    public Optional<Book> findByBookId(@PathVariable String bookId) {
        return bookRepository.findById(bookId);
    }
}
