package com.adobe.bookstore.orders.controllers;

import com.adobe.bookstore.books.services.BooksService;
import com.adobe.bookstore.orders.domains.PurchaseOrder;
import com.adobe.bookstore.orders.domains.export.PurchaseOrderListOptions;
import com.adobe.bookstore.orders.domains.payload.PurchaseOrderData;
import com.adobe.bookstore.orders.services.PurchaseOrdersService;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/purchase-orders")
@Slf4j
public class PurchaseOrdersController {

    private final BooksService booksService;
    private final PurchaseOrdersService purchaseOrdersService;

    public PurchaseOrdersController(PurchaseOrdersService purchaseOrdersService, BooksService booksService) {
        this.purchaseOrdersService = purchaseOrdersService;
        this.booksService = booksService;
    }

    @GetMapping
    public ResponseEntity<String> listPurchaseOrders(@RequestParam(name = "format", required = false, defaultValue = "json") String format)
    throws IOException {
        if (!PurchaseOrderListOptions.isAnOption(format)) {
            var msg = String.format("Invalid format requested: %s. Expected one of 'csv' or 'json'", format);
            throw new IllegalArgumentException(msg);
        }
        var listOption = PurchaseOrderListOptions.valueOf(StringUtils.upperCase(format));
        return ResponseEntity.ok()
            .header("Content-Type", listOption.getContentType())
            .body(listOption == PurchaseOrderListOptions.CSV
                ? purchaseOrdersService.getPurchaseOrderListAsCSV()
                : purchaseOrdersService.getPurchaseOrderListAsJSON()
            );
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<PurchaseOrder> getPurchaseOrder(@PathVariable String uuid) {
        Optional<PurchaseOrder> po = purchaseOrdersService.findPurchaseOrderById(uuid);
        if (po.isPresent()) {
            return ResponseEntity.ok(po.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, String>> addPurchaseOrder(@RequestBody PurchaseOrderData payload) {
        var purchaseOrder = purchaseOrdersService.addPurchaseOrder(payload);
        var uri = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{uuid}")
            .buildAndExpand(purchaseOrder.getPurchaseOrderId())
            .toUri();

        CompletableFuture
            .runAsync(() -> booksService.updateBooksInventory(payload.getDetailsData()))
            .exceptionally((Throwable e) -> {
                log.error("Could not update the stock: {}", e.getMessage(), e);
                return null;
            });

        return ResponseEntity
                .created(uri)
                .body(Map.of("purchaseOrderId", purchaseOrder.getPurchaseOrderId()));
    }
}
