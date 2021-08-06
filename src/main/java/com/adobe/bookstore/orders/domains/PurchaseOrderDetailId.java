package com.adobe.bookstore.orders.domains;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
public class PurchaseOrderDetailId implements Serializable {

    @Column(name = "ORDER_ID", nullable = false, length = 36)
    @NotBlank(message = "Order Id can't be blank")
    private String orderId;

    @Column(name = "BOOK_ID", nullable = false, length = 36)
    @NotBlank(message = "Book Id can't be blank")
    private String bookId;
}
