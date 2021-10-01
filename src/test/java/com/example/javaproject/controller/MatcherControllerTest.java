package com.example.javaproject.controller;

import com.example.javaproject.Authentication.AccountManager;
import com.example.javaproject.Authentication.DBManager;
import com.example.javaproject.Authentication.User;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MatcherControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private  AccountManager accountManager;
    @MockBean
    private DBManager dbManager;

    @BeforeEach
    public void setUp(){
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("Check that getting an empty sell list returns a 204 error")
    void ensureThatEmptySellListReturnsNoContent() throws Exception {
        when(dbManager.getUser(anyString())).thenReturn(true);

        mockMvc.perform(get("/sellOrders")
                        .header("token", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    @DisplayName("Check that getting an empty buy list returns a 200 error")
    void ensureThatEmptyBuyListReturnsNoContent() throws Exception {
        when(dbManager.getUser(anyString())).thenReturn(true);
        mockMvc.perform(get("/buyOrders")
                        .header("token", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    @DisplayName("Check that posting a new order returns a 201, created")
    void ensureThatAddOrderReturnsCreated() throws Exception {
        when(dbManager.getUser(anyString())).thenReturn(true);
        mockMvc.perform(post("/addOrder")
                        .header("token", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"account\": 2,\"price\": 6.0,\"quantity\": 4.0,\"action\": \"SELL\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(content().json("{\"account\":2,\"price\":6.0,\"quantity\":4.0,\"action\":\"SELL\"}"));
    }

    @Test
    @DisplayName("Check that posting an invalid order with a string returns a relevant response")
    void ensureThatAnInvalidOrderThrowsError() throws Exception {
        MvcResult result = mockMvc.perform(post("/addOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"account\": 2,\"price\": \"six\",\"quantity\": 4,\"action\": \"SELL\"}"))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("Cannot deserialize value of type `double` from String \"six\": not a valid `double` value"));

    }

    @Test
    @DisplayName("Check that posting an invalid order with an invalid number returns a relevant response")
    void ensureThatAnInvalidOrderThrowsError2() throws Exception {
        MvcResult result = mockMvc.perform(post("/addOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"account\": 2,\"price\": 0,\"quantity\": 4,\"action\": \"SELL\"}"))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("must be greater than or equal to 0.01"));
    }

    @Test
    @DisplayName("Check that posting an invalid order with multiple invalid fields returns a relevant response")
    void ensureThatAnInvalidOrderThrowsMultipleErrors() throws Exception {
        MvcResult result = mockMvc.perform(post("/addOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"account\": 2,\"price\": 0.999,\"quantity\": 0,\"action\": \"SELL\"}"))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertAll(
                () -> assertTrue(result.getResponse().getContentAsString().contains("numeric value out of bounds")),
                () -> assertTrue(result.getResponse().getContentAsString().contains("must be greater than or equal to 0.01"))
        );
    }

    @Test
    @DisplayName("Check that posting an invalid order with all invalid fields returns a relevant response")
    void ensureThatAnInvalidOrderThrowsFourErrors() throws Exception {
        MvcResult result = mockMvc.perform(post("/addOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"account\": -1,\"price\": 0.999,\"quantity\": 0,\"action\": null}"))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertAll(
                () -> assertTrue(result.getResponse().getContentAsString().contains("numeric value out of bounds")),
                () -> assertTrue(result.getResponse().getContentAsString().contains("must be greater than or equal to 0.01")),
                () -> assertTrue(result.getResponse().getContentAsString().contains("must be greater than or equal to 0")),
                () -> assertTrue(result.getResponse().getContentAsString().contains("must not be null"))
        );
    }


    @Test
    @DisplayName("Check that posting an empty order returns a 400, invalid request")
    void ensureThatAnEmptyOrderThrowsError() throws Exception {
        mockMvc.perform(post("/addOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"account\":,\"price\":,\"quantity\":,\"action\":}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Check that getting a sell list returns 200, OK")
    void ensureThatNonEmptySellListReturns() throws Exception {
        when(dbManager.getUser(anyString())).thenReturn(true);
        mockMvc.perform(get("/sellOrders")
                        .header("token", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"account\":2,\"price\":6.0,\"quantity\":4.0,\"action\":\"SELL\"}]"));
        }

}