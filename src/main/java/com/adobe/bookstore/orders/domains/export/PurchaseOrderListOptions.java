package com.adobe.bookstore.orders.domains.export;

import org.springframework.http.MediaType;

public enum PurchaseOrderListOptions {
    JSON, CSV;

    public static boolean isAnOption(String value) {
        return JSON.name().equalsIgnoreCase(value) || CSV.name().equalsIgnoreCase(value);
    }

    public String getContentType() {
        return (this == JSON ? MediaType.APPLICATION_JSON_VALUE : "text/csv").concat(";charset=UTF-8");
    }
}
