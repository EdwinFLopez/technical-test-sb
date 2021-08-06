package com.adobe.bookstore.orders.domains.payload;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderData {

    private String purchaseOrderId;
    private String customerName;
    private String shippingAddress;
    private List<DetailsData> details = new ArrayList<>();

    public Map<String, Integer> getDetailsData() {
        return this.getDetails()
            .stream()
            .collect(Collectors.toMap(DetailsData::getBookId, DetailsData::getQuantity));
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DetailsData {
        private String bookId;
        private int quantity;
    }
}
