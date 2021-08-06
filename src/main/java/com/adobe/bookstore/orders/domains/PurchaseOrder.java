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
    @NotBlank(message = "Purchase Order Id can't be blank")
    @Size(min = 2, max = 36, message = "Purchase Order Id must be between 2 and 36 characters long")
    private String purchaseOrderId;

    @Column(name = "CUSTOMER_NAME", nullable = false)
    @NotBlank(message = "Customer Name can't be blank")
    @Size(min = 2, max = 255, message = "Customer Name must be between 2 and 255 characters long")
    private String customerName;

    @Column(name = "SHIPPING_ADDRESS", nullable = false)
    @NotBlank(message = "Shipping Address can't be blank")
    @Size(min = 2, max = 255, message = "Shipping Address must be between 2 and 255 characters long")
    private String shippingAddress;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ORDER_ID")
    @Builder.Default
    private List<PurchaseOrderDetail> details = new ArrayList<>();
}
