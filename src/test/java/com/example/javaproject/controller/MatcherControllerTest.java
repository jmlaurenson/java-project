package com.example.javaproject.controller;

import com.example.javaproject.ActionType;
import com.example.javaproject.Order;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

class MatcherControllerTest {
    @BeforeEach
    void setUp() {

    }
    @Test
    @DisplayName("Check that getting an empty sell list returns a 204 error")
    void ensureThatEmptySellListReturnsNoContent() throws Exception {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/sellOrders")).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode(), "RESPONSE SHOULD RETURN 204");
    }
    @Test
    @DisplayName("Check that getting an empty buy list returns a 204 error")
    void ensureThatEmptyBuyListReturnsNoContent() throws Exception {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/buyOrders")).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode(), "RESPONSE SHOULD RETURN 204");
    }

    @Test
    @DisplayName("Check that posting a new order returns a 201, created error")
    void ensureThatAddOrderReturnsCreated() throws Exception {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder().header("Content-Type", "application/json")
                .POST(HttpRequest
                        .BodyPublishers
                        .ofString("{\"account\": 2,\"price\": 6.0,\"quantity\": 4,\"action\": \"SELL\"}"))
                .uri(URI.create("http://localhost:8080/addOrder")).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "RESPONSE SHOULD RETURN 201");
    }

    @Test
    @DisplayName("Check that posting an invalid order returns a 400, bad request error")
    void ensureThatAnInvalidOrderReturnsBadRequest() throws Exception {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder().header("Content-Type", "application/json")
                .POST(HttpRequest
                        .BodyPublishers
                        .ofString("{\"account\": 2,\"price\": \"six\",\"quantity\": 4,\"action\": \"SELL\"}"))
                .uri(URI.create("http://localhost:8080/addOrder")).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode(), "RESPONSE SHOULD RETURN 400");
    }

    @Test
    @DisplayName("Check that getting a sell list returns 200, OK")
    void ensureThatNonEmptySellListReturns() throws Exception {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/sellOrders")).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "RESPONSE SHOULD RETURN 200");
    }

}