package com.adobe.bookstore.orders.domains.export;

public interface PurchaseOrderRecord {

    String[] PURCHASE_ORDER_HEADERS = {"PURCHASE_ORDER", "CUSTOMER_NAME", "SHIPPING_ADDRESS", "BOOK_ID", "BOOK_NAME", "QUANTITY"};

    String getOrderId();

    String getCustomerName();

    String getShippingAddress();

    String getBookId();

    String getBookName();

    long getQuantity();
}
