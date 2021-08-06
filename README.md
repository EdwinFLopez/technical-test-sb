# Introduction

SpringBoot Learning Exercise.

* Repository url: https://github.com/edwinflopez/technical-test-sb
* Download with: git clone https://github.com/edwinflopez/technical-test-sb
* Run the solution with maven: mvn clean spring-boot:run
* The application context is: /bookstore/api/v1
* The required endpoints are:
  * Get all purchase orders as csv: `GET http://localhost:8080/bookstore/api/v1/purchase-orders?format=csv`
  * Get all purchase orders as json: `GET http://localhost:8080/bookstore/api/v1/purchase-orders?format=json`
  * Create a new purchase order: `POST http://localhost:8080/bookstore/api/v1/purchase-orders/add`
    * JSON Payload:
    ```json
    {
        "purchaseOrderId" : "111666d6-610a-4ef0-9037-b45dd0d5bbaa", // Not required.
        "customerName": "Claudia Torres Perez",                     // Required
        "shippingAddress" : "Bilbao Street 45, Barcelona, Spain",   // Required
        "details" : [
            { "bookId" : "22d580fc-d02e-4f70-9980-f9693c18f6e0", "quantity" : 1 }, // At least one
            { "bookId" : "d02b58ae-8731-451c-9acb-1941adf88501", "quantity" : 1 },
            { "bookId" : "58716995-b335-4bb0-89c1-3503bc003118", "quantity" : 1 },
            { "bookId" : "e415e3af-e87e-47e6-9bf2-f08c72e2f281", "quantity" : 1 }
        ] // Required
    }
    ```