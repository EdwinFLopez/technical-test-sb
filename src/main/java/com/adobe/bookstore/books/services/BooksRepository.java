package com.adobe.bookstore.books.services;

import com.adobe.bookstore.books.domains.Book;
import org.springframework.data.repository.CrudRepository;

public interface BooksRepository extends CrudRepository<Book, String> {
}
