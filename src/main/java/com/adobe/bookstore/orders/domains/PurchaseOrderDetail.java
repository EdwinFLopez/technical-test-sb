package com.adobe.bookstore.orders.domains;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PURCHASE_ORDER_DETAILS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
public class PurchaseOrderDetail implements Serializable {

    @EmbeddedId
    private PurchaseOrderDetailId detailId;

    @Column(name = "QUANTITY", nullable = false)
    @Min(value = 0, message = PurchaseOrderValidationMessages.PODETAIL_QUANTITY_NEGATIVE)
    private int quantity;
}
