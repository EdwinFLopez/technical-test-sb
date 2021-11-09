package com.adobe.bookstore.orders.services;

import com.adobe.bookstore.books.services.BooksService;
import com.adobe.bookstore.orders.domains.PurchaseOrder;
import com.adobe.bookstore.orders.domains.PurchaseOrderDetail;
import com.adobe.bookstore.orders.domains.PurchaseOrderDetailId;
import com.adobe.bookstore.orders.domains.export.PurchaseOrderRecord;
import com.adobe.bookstore.orders.domains.payload.PurchaseOrderData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class PurchaseOrdersService {

    private final BooksService booksService;
    private final PurchaseOrdersRepository poRepository;

    public PurchaseOrdersService(BooksService booksService, PurchaseOrdersRepository poRepository) {
        this.booksService = booksService;
        this.poRepository = poRepository;
    }

    @Transactional
    public PurchaseOrder addPurchaseOrder(PurchaseOrderData purchaseOrder) {
        var uuid = StringUtils.defaultIfBlank(purchaseOrder.getPurchaseOrderId(), UUID.randomUUID().toString());
        if(poRepository.existsById(uuid)) {
            var msg = String.format("Purchase Order %s already exist. Update operation not supported", uuid);
            throw new UnsupportedOperationException(msg);
        }
        var selectedBooks = purchaseOrder.getDetailsData();
        booksService.checkForEnoughStock(selectedBooks);

        List<PurchaseOrderDetail> purchaseOrderDetails = new ArrayList<>();
        selectedBooks.keySet()
            .stream()
            .map(bookId -> new PurchaseOrderDetail(new PurchaseOrderDetailId(uuid, bookId), selectedBooks.get(bookId)))
            .forEach(purchaseOrderDetails::add);

        PurchaseOrder po = PurchaseOrder.builder()
            .withPurchaseOrderId(uuid)
            .withCustomerName(purchaseOrder.getCustomerName())
            .withShippingAddress(purchaseOrder.getShippingAddress())
            .withDetails(purchaseOrderDetails)
            .build();

        return poRepository.save(po);
    }

    public Optional<PurchaseOrder> findPurchaseOrderById(String purchaseOrderId) {
        return poRepository.findById(purchaseOrderId);
    }

    public List<PurchaseOrder> findAllPurchaseOrders() {
        List<PurchaseOrder> purchaseOrders = new ArrayList<>();
        poRepository.findAll().forEach(purchaseOrders::add);
        return purchaseOrders;
    }

    public String getPurchaseOrderListAsCSV() throws IOException {
        StringBuilder strCsvBuffer = new StringBuilder();
        CSVFormat csvFormat = CSVFormat.DEFAULT
            .withAutoFlush(true)
            .withDelimiter('|')
            .withSystemRecordSeparator()
            .withTrim()
            .withHeader(PurchaseOrderRecord.PURCHASE_ORDER_HEADERS);
        try (var csvPrinter = new CSVPrinter(strCsvBuffer, csvFormat)) {
            poRepository
                .findAllPurchaseOrdersWithDetails()
                .stream()
                .map((PurchaseOrderRecord csvrecord) -> new Object[] {
                csvrecord.getOrderId(),
                csvrecord.getCustomerName(),
                csvrecord.getShippingAddress(),
                csvrecord.getBookId(),
                csvrecord.getBookName(),
                csvrecord.getQuantity()
            })
            .forEach(data -> {
                try {
                    csvPrinter.printRecord(data);
                } catch (IOException e) {
                    log.warn("Could not print record for csv: {}", e.getMessage(), e);
                }
            });
        }
        return strCsvBuffer.toString();
    }

    public String getPurchaseOrderListAsJSON() throws JsonProcessingException {
        return new JsonMapper().writeValueAsString(findAllPurchaseOrders());
    }
}
