package com.adobe.bookstore.orders.domains;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PURCHASE_ORDERS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(setterPrefix = "with")
@Data
public class PurchaseOrder implements Serializable {

    @Id
    @Column(name = "ORDER_ID", length = 36, nullable = false)
    @NotBlank(message = PurchaseOrderValidationMessages.PURCHASE_ORDER_ID_MANDATORY)
    @Size(min = 2, max = 36, message = PurchaseOrderValidationMessages.PURCHASE_ORDER_ID_WRONG_SIZE)
    private String purchaseOrderId;

    @Column(name = "CUSTOMER_NAME", nullable = false)
    @NotBlank(message = PurchaseOrderValidationMessages.CUSTOMER_NAME_MANDATORY)
    @Size(min = 2, max = 255, message = PurchaseOrderValidationMessages.CUSTOMER_NAME_WRONG_SIZE)
    private String customerName;

    @Column(name = "SHIPPING_ADDRESS", nullable = false)
    @NotBlank(message = PurchaseOrderValidationMessages.SHIPPING_ADDRESS_MANDATORY)
    @Size(min = 2, max = 255, message = PurchaseOrderValidationMessages.SHIPPING_ADDRESS_WRONG_SIZE)
    private String shippingAddress;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ORDER_ID")
    @Builder.Default
    private List<PurchaseOrderDetail> details = new ArrayList<>();
}
