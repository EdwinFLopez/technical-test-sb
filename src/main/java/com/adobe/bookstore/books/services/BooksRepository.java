package com.adobe.bookstore.books.services;

import com.adobe.bookstore.books.domains.Book;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BooksRepository extends CrudRepository<Book, String> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Book b set b.quantity=:quantity where id=:bookId")
    int updateBookAvailableQuantity(@Param("bookId") String bookId, @Param("quantity") int quantity);
}
