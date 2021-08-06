package com.adobe.bookstore.orders.domains;

public final class PurchaseOrderValidationMessages {

    public static final String PURCHASE_ORDER_ID_MANDATORY = "Purchase Order Id can't be blank";
    public static final String PURCHASE_ORDER_ID_WRONG_SIZE = "Purchase Order Id must be between 2 and 36 characters long";
    public static final String CUSTOMER_NAME_MANDATORY = "Customer Name can't be blank";
    public static final String CUSTOMER_NAME_WRONG_SIZE = "Customer Name must be between 2 and 255 characters long";
    public static final String SHIPPING_ADDRESS_MANDATORY = "Shipping Address can't be blank";
    public static final String SHIPPING_ADDRESS_WRONG_SIZE = "Shipping Address must be between 2 and 255 characters long";
    public static final String PODETAIL_QUANTITY_NEGATIVE = "Quantity should be at least 0";
    public static final String PODETAIL_ID_ORDER_ID_MANDATORY = "Order Id can't be blank";
    public static final String PODETAIL_ID_BOOK_ID_MANDATORY = "Book Id can't be blank";

    private PurchaseOrderValidationMessages() {
    }
}
