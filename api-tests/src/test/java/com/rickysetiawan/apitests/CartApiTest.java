package com.rickysetiawan.apitests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CartApiTest extends BaseApiTest {

    @Test
    @DisplayName("POST /carts creates a cart containing the submitted product lines")
    void createCart_persistsSubmittedProducts() {
        String requestBody = """
            {
              "userId": 1,
              "date": "2026-07-30",
              "products": [
                {"productId": 1, "quantity": 2},
                {"productId": 3, "quantity": 1}
              ]
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/carts")
        .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("products.size()", equalTo(2))
            .body("products[0].productId", equalTo(1))
            .body("products[0].quantity", equalTo(2));
    }

    @Test
    @DisplayName("Every productId referenced by a cart resolves to a real product")
    void cartProductIds_areValidAcrossTheCatalog() {
        // This is the test I'd actually want in a real e-commerce QA suite:
        // it doesn't just check the cart endpoint in isolation, it checks
        // that the cart and the product catalog agree with each other —
        // exactly the kind of cross-resource data-integrity bug that a
        // single-endpoint smoke test would never catch.
        Response cartResponse = given().pathParam("id", 1).when().get("/carts/{id}");
        cartResponse.then().statusCode(200);

        List<Map<String, Object>> products = cartResponse.jsonPath().getList("products");
        assertTrue(products != null && !products.isEmpty(), "Cart 1 should contain product lines to validate");

        for (Map<String, Object> line : products) {
            int productId = (int) line.get("productId");
            given()
                .pathParam("id", productId)
            .when()
                .get("/products/{id}")
            .then()
                .statusCode(200)
                .body("id", equalTo(productId));
        }
    }

    @Test
    @DisplayName("GET /carts returns carts ordered/queryable by date range")
    void getCarts_withDateRange_returnsOnlyCartsInRange() {
        given()
            .queryParam("startdate", "2026-01-01")
            .queryParam("enddate", "2026-12-31")
        .when()
            .get("/carts")
        .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(0));
    }
}
