package com.adobe.bookstore.orders.domains.export;

public interface PurchaseOrderRecord {
    String getOrderId();
    String getCustomerName();
    String getShippingAddress();
    String getBookId();
    String getBookName();
    long getQuantity();
}
