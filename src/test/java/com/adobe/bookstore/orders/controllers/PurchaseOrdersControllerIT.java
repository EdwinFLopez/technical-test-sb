package com.adobe.bookstore.orders.controllers;

import com.adobe.bookstore.BookStore;
import com.adobe.bookstore.orders.domains.export.PurchaseOrderRecord;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.io.IOUtils;
import org.flywaydb.core.Flyway;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = BookStore.class)
public class PurchaseOrdersControllerIT {

    static final String PURCHASE_ORDER_HP_JSON = "/payloads/purchase-order-hp.json";
    static final String PURCHASE_ORDER_HP_WITH_ID_JSON = "/payloads/purchase-order-hp-with-id.json";
    static final String PURCHASE_ORDER_HP_WITH_DUPLICATED_ID_JSON = "/payloads/purchase-order-with-duplicated-id.json";
    static final String PURCHASE_ORDER_NO_CUSTOMER_JSON = "/payloads/purchase-order-no-customer.json";
    static final String PURCHASE_ORDER_NO_DETAILS_JSON = "/payloads/purchase-order-no-details.json";
    static final String PURCHASE_ORDER_NO_ORDER_INFO_JSON = "/payloads/purchase-order-no-order-info.json";
    static final String PURCHASE_ORDER_NO_SHIPPING_ADDRESS_JSON = "/payloads/purchase-order-no-shipping-address.json";
    static final String PURCHASE_ORDER_NO_STOCK_JSON = "/payloads/purchase-order-no-stock.json";

    @Autowired
    TestRestTemplate tpl;
    @Autowired
    Flyway flyway;

    @BeforeEach
    void setUp() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void shouldListPurchaseOrdersAsJsonArrayWhenGivenNoFormatParameter() throws Exception {
        var response = tpl.getForEntity("/purchase-orders", String.class);
        testPurchaseOrdersJsonArrayForGivenResponse(response);
    }

    @Test
    void shouldListPurchaseOrdersAsJsonArrayWhenGivenJSONParameter() throws Exception {
        var response = tpl.getForEntity("/purchase-orders?format=json", String.class);
        testPurchaseOrdersJsonArrayForGivenResponse(response);
    }

    @Test
    void shouldListPurchaseOrdersAsCSVWhenGivenCSVParameter() throws Exception {
        var purchaseOrderHeader = "PURCHASE_ORDER";
        var response = tpl.getForEntity("/purchase-orders?format=csv", String.class);
        var csvBody = response.getBody();
        try (var parser = CSVFormat.DEFAULT
            .withAutoFlush(true)
            .withDelimiter('|')
            .withSystemRecordSeparator()
            .withTrim()
            .withHeader(PurchaseOrderRecord.PURCHASE_ORDER_HEADERS)
            .parse(new StringReader(csvBody))) {

            var headerNames = parser.getHeaderNames();
            var poIds = parser.getRecords().stream()
                .map(record -> record.get(purchaseOrderHeader))
                .filter(entry -> !purchaseOrderHeader.equalsIgnoreCase(entry)) // skip header
                .distinct()
                .collect(Collectors.toList());

            assertPurchaseOrdersResponseLength(response, poIds.size(), poIds);
            assertThat(headerNames).containsOnly(PurchaseOrderRecord.PURCHASE_ORDER_HEADERS);
        }
    }

    @Test
    void shouldFailListingOfPurchaseOrdersWhenGivenInvalidParameter() throws Exception {
        var response = tpl.getForEntity("/purchase-orders?format=invalid-value", String.class);
        var rsBody = response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Invalid Response Code");
        assertThat(rsBody).contains("Expected one of 'csv' or 'json'");
    }

    @Test
    void shouldCreatePurchaseOrderWhenGivenValidPayload() throws Exception {
        var payload = getPurchaseOrderPayload(PURCHASE_ORDER_HP_JSON);
        var entity = getHttpEntityForPurchaseOrder(payload);
        var response = tpl.postForEntity("/purchase-orders/add", entity, String.class);
        assertPurchaseOrderCreated(response);
    }

    @Test
    void shouldCreatePurchaseOrderWhenGivenValidPayloadWithNewId() throws Exception {
        var payload = getPurchaseOrderPayload(PURCHASE_ORDER_HP_WITH_ID_JSON);
        var entity = getHttpEntityForPurchaseOrder(payload);
        var response = tpl.postForEntity("/purchase-orders/add", entity, String.class);
        assertPurchaseOrderCreated(response);
    }

    @Test
    void shouldNotCreatePurchaseOrderWhenGivenValidPayloadWithDuplicatedId() throws Exception {
        var payload = getPurchaseOrderPayload(PURCHASE_ORDER_HP_WITH_DUPLICATED_ID_JSON);
        var entity = getHttpEntityForPurchaseOrder(payload);
        var response = tpl.postForEntity("/purchase-orders/add", entity, String.class);
        assertPurchaseOrderWithInvalidRequest(response);
    }

    @Test
    void shouldNotCreatePurchaseOrderWhenNoDetailsAreGiven() throws Exception {
        var payload = getPurchaseOrderPayload(PURCHASE_ORDER_NO_DETAILS_JSON);
        var entity = getHttpEntityForPurchaseOrder(payload);
        var response = tpl.postForEntity("/purchase-orders/add", entity, String.class);
        assertPurchaseOrderWithInvalidRequest(response);
    }

    @Test
    void shouldNotCreatePurchaseOrderWhenNoCustomerIsGiven() throws Exception {
        var payload = getPurchaseOrderPayload(PURCHASE_ORDER_NO_CUSTOMER_JSON);
        var entity = getHttpEntityForPurchaseOrder(payload);
        var response = tpl.postForEntity("/purchase-orders/add", entity, String.class);
        assertPurchaseOrderWithInvalidRequest(response);
    }

    @Test
    void shouldNotCreatePurchaseOrderWhenNoShippingAddressIsGiven() throws Exception {
        var payload = getPurchaseOrderPayload(PURCHASE_ORDER_NO_SHIPPING_ADDRESS_JSON);
        var entity = getHttpEntityForPurchaseOrder(payload);
        var response = tpl.postForEntity("/purchase-orders/add", entity, String.class);
        assertPurchaseOrderWithInvalidRequest(response);
    }

    @Test
    void shouldNotCreatePurchaseOrderWhenNoOrderInfoIsGiven() throws Exception {
        var payload = getPurchaseOrderPayload(PURCHASE_ORDER_NO_ORDER_INFO_JSON);
        var entity = getHttpEntityForPurchaseOrder(payload);
        var response = tpl.postForEntity("/purchase-orders/add", entity, String.class);
        assertPurchaseOrderWithInvalidRequest(response);
    }

    @Test
    void shouldNotCreatePurchaseOrderWhenNoStockIsAvailable() throws Exception {
        var payload = getPurchaseOrderPayload(PURCHASE_ORDER_NO_STOCK_JSON);
        var entity = getHttpEntityForPurchaseOrder(payload);
        var response = tpl.postForEntity("/purchase-orders/add", entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // -- SUPPORT METHODS.

    private void testPurchaseOrdersJsonArrayForGivenResponse(ResponseEntity<String> response) throws JSONException {
        var jsonString = response.getBody();
        var listOfPurchaseOrders = new JSONArray(jsonString);
        List<String> poIds = new ArrayList<>();
        for (int objIndex = 0; objIndex < listOfPurchaseOrders.length(); objIndex++) {
            var jsonObject = listOfPurchaseOrders.getJSONObject(objIndex);
            poIds.add(jsonObject.getString("purchaseOrderId"));
        }
        assertPurchaseOrdersResponseLength(response, listOfPurchaseOrders.length(), poIds);
    }

    private void assertPurchaseOrdersResponseLength(ResponseEntity<String> response, int listOfPurchaseOrdersLength, List<String> poIds) {
        final int expectedNumberOfPurchaseOrders = 3;
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Invalid Response Status Code");
        assertEquals(expectedNumberOfPurchaseOrders, listOfPurchaseOrdersLength, "Invalid amount of purchase orders found");
        assertThat(poIds)
            .containsOnly(
                "ae1666d6-6100-4ef0-9037-b45dd0d5bb0f",
                "ae1666d6-6200-4ef0-9037-b45dd0d5bb0e",
                "ae1666d6-6300-4ef0-9037-b45dd0d5bb0d"
            );
    }

    private String getPurchaseOrderPayload(String payloadFilePath) throws IOException {
        try(var payloadStream = this.getClass().getResourceAsStream(payloadFilePath)){
            return IOUtils.toString(payloadStream);
        }
    }

    private HttpEntity getHttpEntityForPurchaseOrder(String payload) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity(payload, headers);
    }

    private void assertPurchaseOrderCreated(ResponseEntity<String> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(response.getBody()).contains("purchaseOrderId");
    }

    private void assertPurchaseOrderWithInvalidRequest(ResponseEntity<String> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Invalid request. Please review parameters, payload, and resources");
    }
}
