package com.example.javaproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MatcherTest {
    private Matcher matcher;

    @BeforeEach
    void setUp() {
        matcher = new Matcher();
    }

    @Test
    @DisplayName("Check orders are added with the correct fields")
    void matcherAddsOrders() {
        //Act
        matcher.addNewOrder(new Order(1, 40, 30, ActionType.BUY));
        Order order = matcher.getBuyOrders().get(0);

        //Assert
        assertAll(
                () -> assertEquals(new BigDecimal("40.0"), order.getPrice(), "ORDERS PRICE IS NOT CORRECT"),
                () -> assertEquals(1, order.getAccount(), "ORDERS ACCOUNT IS NOT CORRECT"),
                () -> assertEquals(new BigDecimal("30.0"), order.getQuantity(), "ORDERS QUANTITY IS NOT CORRECT"),
                () -> assertEquals(ActionType.BUY, order.getAction(), "ORDERS ACTION IS NOT CORRECT")
        );
    }

    @Test
    @DisplayName("Check buy and sell orders are added to different lists")
    void differentActionsAreSeparated() {
        //Act
        matcher.addNewOrder(new Order(1, 40, 30, ActionType.BUY));
        matcher.addNewOrder(new Order(2, 40, 30, ActionType.SELL));
        Order buyOrder = matcher.getBuyOrders().get(0);
        Order sellOrder = matcher.getSellOrders().get(0);

        //Assert
        assertAll(
                () -> assertEquals(1, buyOrder.getAccount(), "BUY ORDER IS NOT IN IT'S OWN LIST"),
                () -> assertEquals(2, sellOrder.getAccount(), "BUY ORDER IS NOT IN IT'S OWN LIST")
        );
    }

    @Test
    @DisplayName("Check orders are added in the correct order")
    void matcherAddsBuyOrdersToCorrectPlace() {
        //Act
        matcher.addNewOrder(new Order(1, 40, 30, ActionType.BUY));
        matcher.addNewOrder(new Order(1, 30, 30, ActionType.BUY));
        Order order0 = matcher.getBuyOrders().get(0);
        Order order1 = matcher.getBuyOrders().get(1);

        //Assert
        assertAll(
                () -> assertEquals(new BigDecimal("30.0"), order0.getPrice(), "LIST HAS NOT BEEN ORDERED CORRECTLY"),
                () -> assertEquals(new BigDecimal("40.0"), order1.getPrice(), "LIST HAS NOT BEEN ORDERED CORRECTLY")
        );
    }

    @Test
    @DisplayName("Check orders are ordered properly")
    void oldestOrdersAreFirstInList() {
        //Act
        matcher.addNewOrder(new Order(1, 40, 30, ActionType.BUY));
        matcher.addNewOrder(new Order(2, 40, 30, ActionType.BUY));
        Order order0 = matcher.getBuyOrders().get(0);
        Order order1 = matcher.getBuyOrders().get(1);

        //Assert
        assertAll(
                () -> assertEquals(1, order0.getAccount(), "FIRST ORDER SHOULD COME FIRST"),
                () -> assertEquals(2, order1.getAccount(), "SECOND ORDER SHOULD COME LAST")
        );
    }

    @Test
    @DisplayName("Check a match can be found between two similar orders")
    void findExactMatchOneValue() {
        //Act
        matcher.addNewOrder(new Order(1, 40, 30, ActionType.SELL));
        Order match = matcher.findMatchingOrder(new Order(2, 40, 30, ActionType.BUY)).get();

        //Assert
        assertEquals(1, match.getAccount(), "MATCH IS NOT FOUND");
    }

    @Test
    @DisplayName("Check a match is found when two orders are added")
    void findExactMatchTwoValues() {
        //Act
        matcher.addNewOrder(new Order(1, 40, 30, ActionType.BUY));
        matcher.addNewOrder(new Order(2, 30, 30, ActionType.BUY));
        Order match = matcher.findMatchingOrder(new Order(3, 30, 30, ActionType.SELL)).get();

        //Assert
        assertEquals(2, match.getAccount(), "CLOSEST MATCH HAS NOT BEEN FOUND");
    }

    @Test
    @DisplayName("Check the oldest buy order is matched")
    void findOldestMatchSELL() {
        //Act
        matcher.addNewOrder(new Order(1, 40, 30, ActionType.BUY));
        matcher.addNewOrder(new Order(2, 40, 30, ActionType.BUY));
        Order match = matcher.findMatchingOrder(new Order(3, 40, 30, ActionType.SELL)).get();

        //Assert
        assertEquals(1, match.getAccount(), "OLDEST MATCH IS NOT FOUND");
    }

    @Test
    @DisplayName("Check the oldest sell order is matched")
    void findOldestMatchBUY() {
        //Act
        matcher.addNewOrder(new Order(1, 40, 30, ActionType.SELL));
        matcher.addNewOrder(new Order(2, 40, 30, ActionType.SELL));
        Order match = matcher.findMatchingOrder(new Order(3, 40, 30, ActionType.BUY)).get();

        //Assert
        assertEquals(1, match.getAccount(), "OLDEST MATCH IS NOT FOUND");
    }

    @Test
    @DisplayName("Check an empty list returns an empty match")
    void noMatchInEmptyList() {
        //Act
        Optional<Order> match = matcher.findMatchingOrder(new Order(3, 40, 30, ActionType.BUY));

        //Assert
        assertTrue(match.isEmpty(), "EMPTY ARRAY IS INVALID");
    }

    @Test
    @DisplayName("Check that no invalid buy matches are made")
    void noMatchInListBUY() {
        //Act
        matcher.addNewOrder(new Order(1, 40, 30, ActionType.SELL));
        matcher.addNewOrder(new Order(2, 40, 30, ActionType.SELL));
        Optional<Order> match = matcher.findMatchingOrder(new Order(30, 30, 30, ActionType.BUY));

        //Assert
        assertTrue(match.isEmpty(), "AN INVALID MATCH HAS BEEN FOUND");
    }
    @Test
    @DisplayName("Check that no invalid sell matches are made")
    void noMatchInListSELL() {
        //Act
        matcher.addNewOrder(new Order(1, 40, 30, ActionType.BUY));
        matcher.addNewOrder(new Order(2, 40, 30, ActionType.BUY));
        Optional<Order> match = matcher.findMatchingOrder(new Order(30, 50, 30, ActionType.SELL));

        //Assert
        assertTrue(match.isEmpty(), "AN INVALID MATCH HAS BEEN FOUND");
    }

    @Test
    @DisplayName("Check that a new order is added if none are found")
    void addOrderIfNoneFound() {
        //Act
        matcher.completeTrade(new Order(30, 50, 30, ActionType.SELL));
        List<Order> newList = matcher.getSellOrders();

        //Assert
        assertEquals(1, newList.size(), "NEW ORDER HAS NOT BEEN ADDED TO LIST");
    }

    @Test
    @DisplayName("Check that a matched order is removed")
    void removeOrderIfFound() {
        //Act
        matcher.addNewOrder(new Order(2, 40, 30, ActionType.BUY));
        matcher.addNewOrder(new Order(3, 30, 30, ActionType.BUY));
        matcher.completeTrade(new Order(4, 20, 30, ActionType.SELL));
        List<Order> newList = matcher.getBuyOrders();

        //Assert
        assertAll(
                () -> assertEquals(1, newList.size(), "ORDER HAS NOT BEEN REMOVED"),
                () -> assertEquals(2, newList.get(0).getAccount(), "ORDER HAS NOT BEEN REMOVED")
        );
    }

    @Test
    @DisplayName("Check that two orders can be matched with one order")
    void makeTwoMatches() {
        //Act
        matcher.addNewOrder(new Order(2, 40, 10, ActionType.BUY));
        matcher.addNewOrder(new Order(3, 30, 20, ActionType.BUY));
        matcher.completeTrade(new Order(4, 20, 30, ActionType.SELL));
        List<Order> newList = matcher.getBuyOrders();

        //Assert
        assertEquals(0, newList.size(), "BOTH ORDERS HAVE NOT BEEN MATCHED");
    }

    @Test
    @DisplayName("Check that two orders can be matched and one is left remaining")
    void makeTwoMatchesOneRemaining() {
        //Act
        matcher.addNewOrder(new Order(2, 40, 10, ActionType.BUY));
        matcher.addNewOrder(new Order(3, 30, 20, ActionType.BUY));
        matcher.addNewOrder(new Order(5, 40, 10, ActionType.BUY));
        matcher.completeTrade(new Order(4, 20, 30, ActionType.SELL));
        List<Order> newList = matcher.getBuyOrders();

        //Assert
        assertAll(
                () -> assertEquals(1, newList.size(), "FINAL LIST SHOULD HAVE ONE ORDER LEFT"),
                () -> assertEquals(5, newList.get(0).getAccount(), "REMAINING ORDER SHOULD BE NEWEST ORDER")
        );
    }

    @Test
    @DisplayName("Check that a partial match can be made when the new order has a larger quantity")
    void makePartMatch() {
        //Act
        matcher.addNewOrder(new Order(2, 40, 10, ActionType.BUY));
        matcher.completeTrade(new Order(4, 20, 30, ActionType.SELL));
        List<Order> newList = matcher.getSellOrders();

        //Assert
        assertAll(
                () -> assertEquals(1, newList.size(), "ONE AND A HALF MATCHES SHOULD HAVE BEEN MADE"),
                () -> assertEquals(new BigDecimal("20"), newList.get(0).getQuantity(), "QUANTITY REMAINING SHOULD BE 20")
        );
    }

    @Test
    @DisplayName("Check that a partial match can be made when the new order has a smaller quantity")
    void makePartMatch2() {
        //Act
        matcher.addNewOrder(new Order(2, 40, 30, ActionType.BUY));
        matcher.completeTrade(new Order(4, 20, 10, ActionType.SELL));
        List<Order> newList = matcher.getBuyOrders();

        //Assert
        assertAll(
                () -> assertEquals(1, newList.size(), "ONE AND A HALF MATCHES SHOULD HAVE BEEN MADE"),
                () ->assertEquals(new BigDecimal("20"), newList.get(0).getQuantity(), "QUANTITY REMAINING SHOULD BE 20")
        );
    }

    @Test
    @DisplayName("Check that two and a half sell matches can be made")
    void makeTwoAndAHalfBuyMatches() {
        //Act
        matcher.addNewOrder(new Order(2, 40, 10, ActionType.BUY));
        matcher.addNewOrder(new Order(3, 40, 10, ActionType.BUY));
        matcher.addNewOrder(new Order(4, 40, 10, ActionType.BUY));
        matcher.completeTrade(new Order(4, 20, 25, ActionType.SELL));

        List<Order> newList = matcher.getBuyOrders();

        //Assert
        assertAll(
                () -> assertEquals(1, newList.size(), "ONE ORDER SHOULD BE REMAINING AFTER A MATCH"),
                () -> assertEquals(new BigDecimal("5"), newList.get(0).getQuantity(), "REMAINING ORDER SHOULD HAVE 5 QUANTITY AFTER A MATCH"),
                () -> assertEquals(4, newList.get(0).getAccount(), "REMAINING ORDER SHOULD BE ACCOUNT 4")
        );
    }

    @Test
    @DisplayName("Check that two and a half buy matches can be made")
    void makeTwoAndAHalfSellMatches() {
        //Act
        matcher.addNewOrder(new Order(2, 40, 10, ActionType.SELL));
        matcher.addNewOrder(new Order(3, 40, 10, ActionType.SELL));
        matcher.addNewOrder(new Order(4, 40, 10, ActionType.SELL));
        matcher.completeTrade(new Order(4, 60, 25, ActionType.BUY));

        List<Order> newList = matcher.getSellOrders();

        //Assert
        assertAll(
                () -> assertEquals(1, newList.size(), "ONE ORDER SHOULD BE REMAINING AFTER A MATCH"),
                () -> assertEquals(new BigDecimal("5"), newList.get(0).getQuantity(), "REMAINING ORDER SHOULD HAVE 5 QUANTITY AFTER A MATCH"),
                () -> assertEquals(4, newList.get(0).getAccount(), "REMAINING ORDER SHOULD BE ACCOUNT 4")
        );
    }

    @Test
    @DisplayName("Check that a trade can be made")
    void addTrade() {
        //Act
        matcher.addNewOrder(new Order(2, 40, 10, ActionType.SELL));
        matcher.completeTrade(new Order(4, 50, 25, ActionType.BUY));
        List<Trade> newList = matcher.getTrades();

        //Assert
        assertAll(
                () -> assertEquals(1, newList.size(), "ONE TRADE SHOULD BE CREATED"),
                () -> assertEquals(10, newList.get(0).getQuantity(), "TRADE SHOULD HAVE THE LOWEST QUANTITY"),
                () -> assertEquals(40, newList.get(0).getPrice(), "TRADE SHOULD HAVE OLD ORDERS PRICE")
        );
    }

    @Test
    @DisplayName("Check that two trades can be made with a single sell order")
    void addTwoTrades() {
        //Act
        matcher.addNewOrder(new Order(2, 50, 20, ActionType.BUY));
        matcher.addNewOrder(new Order(2, 40, 15, ActionType.BUY));
        matcher.completeTrade(new Order(4, 20, 25, ActionType.SELL));
        List<Trade> newList = matcher.getTrades();

        //Assert
        assertAll(
                () -> assertEquals(2, newList.size(), "TWO TRADES SHOULD HAVE BEEN CREATED"),
                () -> assertEquals(15, newList.get(0).getQuantity(), "THE FIRST TRADES QUANTITY SHOULD BE THE LOWEST QUANTITY"),
                () -> assertEquals(40,newList.get(0).getPrice(), "THE FIRST TRADES PRICE SHOULD BE THE OLD PRICE"),
                () -> assertEquals(10, newList.get(1).getQuantity(), "THE SECOND TRADES QUANTITY SHOULD BE THE LOWEST QUANTITY"),
                () -> assertEquals(50,newList.get(1).getPrice(), "THE SECOND TRADES PRICE SHOULD BE THE OLD PRICE")
        );
    }
}