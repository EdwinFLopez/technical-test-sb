package com.adobe.bookstore.orders.services;

import com.adobe.bookstore.orders.domains.PurchaseOrder;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.adobe.bookstore.orders.domains.export.PurchaseOrderRecord;

public interface PurchaseOrdersRepository extends CrudRepository<PurchaseOrder, String> {
    @Query(nativeQuery = true, value = "select * from purchase_orders_with_details")
    List<PurchaseOrderRecord> findAllPurchaseOrdersWithDetails();
}
