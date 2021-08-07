package com.adobe.bookstore.books.services;

import com.adobe.bookstore.books.domains.Book;
import com.adobe.bookstore.exceptions.OutOfStockException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class BooksService {

    private final BooksRepository booksRepository;

    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    @Transactional
    public void updateBooksInventory(Map<String, Integer> booksAndQuantities) {
        List<Book> bookList = new ArrayList<>();
        booksRepository.findAllById(booksAndQuantities.keySet()).forEach(bookList::add);
        if(bookList.size() != booksAndQuantities.size()) {
            var msg = String.format("Invalid number of books to update. Found: %d. Expected: %d",
                bookList.size(), booksAndQuantities.size());
            throw new IllegalArgumentException(msg);
        }
        for (Book book : bookList) {
            var requestedUnits = booksAndQuantities.get(book.getBookId());
            var availableStock = book.getQuantity();
            var newStock = availableStock - requestedUnits;
            if (newStock < 0) {
                var msg = String.format("Book %s has %d unit(s) in stock to fullfil %d",
                    book.getBookId(), availableStock, requestedUnits);
                throw new OutOfStockException(msg);
            }
            booksRepository.updateBookAvailableQuantity(book.getBookId(), newStock);
        }
    }

    @Transactional(readOnly = true)
    public void checkForEnoughStock(Map<String, Integer> selectedBooks) {
        List<Book> booksStock = new ArrayList<>();
        booksRepository.findAllById(selectedBooks.keySet()).forEach(booksStock::add);
        if (booksStock.size() < selectedBooks.size()) {
            throw new IllegalStateException("At least one book was removed from the database");
        }

        var outOfStockBook = booksStock.stream()
            .filter(book -> book.getQuantity() < selectedBooks.get(book.getBookId()))
            .findAny();

        if (outOfStockBook.isPresent()) {
            var oosBook = outOfStockBook.get();
            var msg = String.format("Not enough stock for book %s. Available: %d.", oosBook.getBookId(), oosBook.getQuantity());
            throw new OutOfStockException(msg);
        }
    }
}
