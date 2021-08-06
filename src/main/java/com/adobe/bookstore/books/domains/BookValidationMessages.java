package com.adobe.bookstore.books.domains;

public final class BookValidationMessages {

    public static final String BOOK_ID_MANDATORY = "Id is mandatory";
    public static final String BOOK_ID_WRONG_SIZE = "Id must be between 2 and 36 characters long";
    public static final String BOOK_NAME_MANDATORY = "Name is mandatory";
    public static final String BOOK_NAME_WRONG_SIZE = "Name must be between 2 and 36 characters long";
    public static final String BOOK_QUANTITY_NEGATIVE = "Quantity should be at least 0";

    private BookValidationMessages() {}
}
