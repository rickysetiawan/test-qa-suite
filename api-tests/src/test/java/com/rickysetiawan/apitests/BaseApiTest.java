package com.rickysetiawan.apitests;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.BeforeAll;

/**
 * Shared setup for every API test class. Requests/responses are logged
 * only when a test fails, which keeps CI output readable while still
 * giving full context to debug a failure.
 */
public class BaseApiTest {

    @BeforeAll
    static void setUpBaseUri() {
        RestAssured.baseURI = "https://fakestoreapi.com";
        RestAssured.filters(
                new RequestLoggingFilter(),
                new ResponseLoggingFilter()
        );
    }
}
