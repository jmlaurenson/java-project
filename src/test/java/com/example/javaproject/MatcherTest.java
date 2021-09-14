package com.example.javaproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MatcherTest {
    private Matcher matcher;

    @BeforeEach
    void setUp(){
        matcher = new Matcher();
    }

    @Test
    void matcherAddsOrders() {
        //Act
        matcher.addNewOrder(1,40,30,ActionType.BUY);
        Order order = matcher.getBuyOrders().get(0);

        //Assert
        assertEquals(order.getPrice(), 40);
        assertEquals(order.getAccount(), 1);
        assertEquals(order.getQuantity(), 30);
        assertEquals(order.getAction(), ActionType.BUY);
    }

    @Test
    void differentActionsAreSeparated() {
        //Act
        matcher.addNewOrder(1,40,30,ActionType.BUY);
        matcher.addNewOrder(2,40,30,ActionType.SELL);
        Order buyOrder = matcher.getBuyOrders().get(0);
        Order sellOrder = matcher.getSellOrders().get(0);

        //Assert
        assertEquals(buyOrder.getAccount(), 1);
        assertEquals(sellOrder.getAccount(), 2);
    }

    @Test
    void invalidOrdersAreCaught() {
        //Act
        matcher.addNewOrder(-10,40,30,ActionType.BUY);
        matcher.addNewOrder(1,0,30,ActionType.BUY);
        matcher.addNewOrder(1,40,0,ActionType.BUY);
        List<Order> orders = matcher.getBuyOrders();

        //Assert
        assertEquals(orders.get(0).isValid(), false);
        assertEquals(orders.get(1).isValid(), false);
        assertEquals(orders.get(2).isValid(), false);
    }
    @Test
    void matcherAddsBuyOrdersToCorrectPlace() {
        //Act
        matcher.addNewOrder(1,40,30,ActionType.BUY);
        matcher.addNewOrder(1,30,30,ActionType.BUY);
        Order order0 = matcher.getBuyOrders().get(0);
        Order order1 = matcher.getBuyOrders().get(1);

        //Assert
        assertEquals(order0.getPrice(), 30);
        assertEquals(order1.getPrice(), 40);
    }

    @Test
    void oldestOrdersAreFirstInList() {
        //Act
        matcher.addNewOrder(1,40,30,ActionType.BUY);
        matcher.addNewOrder(2,40,30,ActionType.BUY);
        Order order0 = matcher.getBuyOrders().get(0);
        Order order1 = matcher.getBuyOrders().get(1);

        //Assert
        assertEquals(order0.getAccount(), 1);
        assertEquals(order1.getAccount(), 2);
    }
}