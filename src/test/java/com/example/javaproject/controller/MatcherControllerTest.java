package com.example.javaproject.controller;

import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;
import static org.junit.jupiter.api.Assertions.*;

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

    @Before
    public void setUp() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("Check that getting an empty sell list returns a 204 error")
    void ensureThatEmptySellListReturnsNoContent() throws Exception {
        MvcResult result = mockMvc.perform(get("/sellOrders")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
        assertEquals("Order list is empty", result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("Check that getting an empty buy list returns a 204 error")
    void ensureThatEmptyBuyListReturnsNoContent() throws Exception {
        MvcResult result = mockMvc.perform(get("/buyOrders")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
        assertEquals("Order list is empty", result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("Check that posting a new order returns a 201, created")
    void ensureThatAddOrderReturnsCreated() throws Exception {
        MvcResult result = mockMvc.perform(post("/addOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"account\": 2,\"price\": 6.0,\"quantity\": 4.0,\"action\": \"SELL\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andReturn();
        assertEquals("{\"account\":2,\"price\":6.0,\"quantity\":4.0,\"action\":\"SELL\"}", result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("Check that posting an invalid order returns a 415, unsupported media type")
    void ensureThatAnInvalidOrderThrowsError() throws Exception {
        mockMvc.perform(post("/addOrder")
                        .content("{\"account\": 2,\"price\": \"six\",\"quantity\": 4,\"action\": \"SELL\"}"))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @DisplayName("Check that posting an empty order returns a 415, unsupported media type")
    void ensureThatAnEmptyOrderThrowsError() throws Exception {
        mockMvc.perform(post("/addOrder")
                        .content("{}"))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @DisplayName("Check that getting a sell list returns 200, OK")
    void ensureThatNonEmptySellListReturns() throws Exception {
        MvcResult result = mockMvc.perform(get("/sellOrders")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        assertEquals("[{\"account\":2,\"price\":6.0,\"quantity\":4.0,\"action\":\"SELL\"}]", result.getResponse().getContentAsString());
    }

}