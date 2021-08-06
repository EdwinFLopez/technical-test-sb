package com.adobe.bookstore.books.domains;

import com.adobe.bookstore.JpaModelTestBase;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookTest extends JpaModelTestBase {

    @Test
    void shouldFailValidationWhenUsingNullParameters() {
        Book underTest = new Book(null, null, -1);
        Set<ConstraintViolation<Object>> violations = getValidator().validate(underTest);
        List<String> violationMessages = extractValidationMessages(violations);

        assertEquals(3, violations.size(), "Expected 3 validations to fail");
        assertTrue(violationMessages.contains(BookValidationMessages.BOOK_ID_MANDATORY));
        assertTrue(violationMessages.contains(BookValidationMessages.BOOK_NAME_MANDATORY));
        assertTrue(violationMessages.contains(BookValidationMessages.BOOK_QUANTITY_NEGATIVE));
    }

    @Test
    void shouldFailValidationWhenUsingEmptyParameters() {
        Book underTest = new Book(StringUtils.EMPTY, StringUtils.EMPTY, -1);
        Set<ConstraintViolation<Object>> violations = getValidator().validate(underTest);
        List<String> violationMessages = extractValidationMessages(violations);

        assertEquals(5, violations.size(), "Expected 5 validations to fail");
        assertTrue(violationMessages.contains(BookValidationMessages.BOOK_ID_MANDATORY));
        assertTrue(violationMessages.contains(BookValidationMessages.BOOK_ID_WRONG_SIZE));
        assertTrue(violationMessages.contains(BookValidationMessages.BOOK_NAME_MANDATORY));
        assertTrue(violationMessages.contains(BookValidationMessages.BOOK_NAME_WRONG_SIZE));
        assertTrue(violationMessages.contains(BookValidationMessages.BOOK_QUANTITY_NEGATIVE));
    }

    @Test
    void shouldFailValidationWhenUsingEmptyBookId() {
        Book underTest = new Book(StringUtils.EMPTY, "BN", 0);
        Set<ConstraintViolation<Object>> violations = getValidator().validate(underTest);
        List<String> violationMessages = extractValidationMessages(violations);

        assertEquals(2, violations.size(), "Expected 2 validations to fail");
        assertTrue(violationMessages.contains(BookValidationMessages.BOOK_ID_MANDATORY));
        assertTrue(violationMessages.contains(BookValidationMessages.BOOK_ID_WRONG_SIZE));
    }

    @Test
    void shouldFailValidationWhenUsingEmptyBookName() {
        Book underTest = new Book("ID", StringUtils.EMPTY, 0);
        Set<ConstraintViolation<Object>> violations = getValidator().validate(underTest);
        List<String> violationMessages = extractValidationMessages(violations);

        assertEquals(2, violations.size(), "Expected 2 validations to fail");
        assertTrue(violationMessages.contains(BookValidationMessages.BOOK_NAME_MANDATORY));
        assertTrue(violationMessages.contains(BookValidationMessages.BOOK_NAME_WRONG_SIZE));
    }

    @Test
    void shouldFailValidationWhenUsingNegativeQuantity() {
        Book underTest = new Book("ID", "BN", -1);
        Set<ConstraintViolation<Object>> violations = getValidator().validate(underTest);
        List<String> violationMessages = extractValidationMessages(violations);

        assertEquals(1, violations.size(), "Expected 1 validations to fail");
        assertTrue(violationMessages.contains(BookValidationMessages.BOOK_QUANTITY_NEGATIVE));
    }
}
