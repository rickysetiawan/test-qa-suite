package com.rickysetiawan.apitests;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class ProductsApiTest extends BaseApiTest {

    @Test
    @DisplayName("GET /products returns a non-empty list with the expected fields")
    void getAllProducts_returnsExpectedShape() {
        given()
        .when()
            .get("/products")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", greaterThan(0))
            .body("[0].id", notNullValue())
            .body("[0].title", not(emptyOrNullString()))
            .body("[0].price", greaterThan(0f))
            .body("[0].category", not(emptyOrNullString()));
    }

    @Test
    @DisplayName("GET /products/{id} returns exactly the product requested")
    void getSingleProduct_returnsMatchingId() {
        int productId = 1;

        given()
            .pathParam("id", productId)
        .when()
            .get("/products/{id}")
        .then()
            .statusCode(200)
            .body("id", equalTo(productId))
            .body("price", greaterThan(0f));
    }

    @Test
    @DisplayName("GET /products/{id} with a non-existent id does not silently succeed")
    void getSingleProduct_withInvalidId_returnsEmptyOrNotFound() {
        // FakeStoreAPI returns 200+null rather than a 404 for unknown ids —
        // asserting on that quirk explicitly, instead of assuming REST-by-the-book
        // behavior, is the difference between a test that reflects the real API
        // and one that only reflects what we wish the API did.
        given()
            .pathParam("id", 999999)
        .when()
            .get("/products/{id}")
        .then()
            .statusCode(200)
            .body(is(anyOf(equalTo(""), equalTo("null"), containsString("null"))));
    }

    @Test
    @DisplayName("GET /products/category/electronics returns only electronics")
    void getProductsByCategory_filtersCorrectly() {
        given()
            .pathParam("category", "electronics")
        .when()
            .get("/products/category/{category}")
        .then()
            .statusCode(200)
            .body("category", everyItem(equalTo("electronics")));
    }

    @Test
    @DisplayName("POST /products creates a product and echoes the submitted fields")
    void createProduct_returnsCreatedResourceWithId() {
        String requestBody = """
            {
              "title": "QA Portfolio Test Product",
              "price": 19.99,
              "description": "Created by an automated API test.",
              "category": "electronics"
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/products")
        .then()
            .statusCode(200)
            .body("id", notNullValue())
            .body("title", equalTo("QA Portfolio Test Product"))
            .body("price", equalTo(19.99f));
    }
}
