package com.example.javaproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void matcherAddsOrders() {
        //Act
        matcher.addNewOrder(new Order(1, 40, 30, ActionType.BUY));
        Order order = matcher.getBuyOrders().get(0);

        //Assert
        assertEquals(40, order.getPrice());
        assertEquals(1, order.getAccount());
        assertEquals(30, order.getQuantity());
        assertEquals(ActionType.BUY, order.getAction());
    }

    @Test
    void differentActionsAreSeparated() {
        //Act
        matcher.addNewOrder(new Order(1, 40, 30, ActionType.BUY));
        matcher.addNewOrder(new Order(2, 40, 30, ActionType.SELL));
        Order buyOrder = matcher.getBuyOrders().get(0);
        Order sellOrder = matcher.getSellOrders().get(0);

        //Assert
        assertEquals(1, buyOrder.getAccount());
        assertEquals(2, sellOrder.getAccount());
    }

    @Test
    void invalidOrdersAreCaught() {
        //Act
        matcher.addNewOrder(new Order(-10, 40, 30, ActionType.BUY));
        matcher.addNewOrder(new Order(1, 0, 30, ActionType.BUY));
        matcher.addNewOrder(new Order(1, 40, 0, ActionType.BUY));
        List<Order> orders = matcher.getBuyOrders();

        //Assert
        assertFalse(orders.get(0).isValid());
        assertFalse(orders.get(1).isValid());
        assertFalse(orders.get(2).isValid());
    }

    @Test
    void matcherAddsBuyOrdersToCorrectPlace() {
        //Act
        matcher.addNewOrder(new Order(1, 40, 30, ActionType.BUY));
        matcher.addNewOrder(new Order(1, 30, 30, ActionType.BUY));
        Order order0 = matcher.getBuyOrders().get(0);
        Order order1 = matcher.getBuyOrders().get(1);

        //Assert
        assertEquals(30, order0.getPrice());
        assertEquals(40, order1.getPrice());
    }

    @Test
    void oldestOrdersAreFirstInList() {
        //Act
        matcher.addNewOrder(new Order(1, 40, 30, ActionType.BUY));
        matcher.addNewOrder(new Order(2, 40, 30, ActionType.BUY));
        Order order0 = matcher.getBuyOrders().get(0);
        Order order1 = matcher.getBuyOrders().get(1);

        //Assert
        assertEquals(1, order0.getAccount());
        assertEquals(2, order1.getAccount());
    }

    @Test
    void findExactMatchOneValue() {
        //Act
        matcher.addNewOrder(new Order(1, 40, 30, ActionType.SELL));
        Order match = matcher.findMatchingOrder(new Order(2, 40, 30, ActionType.BUY)).get();

        //Assert
        assertEquals(1, match.getAccount());
    }

    @Test
    void findExactMatchTwoValues() {
        //Act
        matcher.addNewOrder(new Order(1, 40, 30, ActionType.BUY));
        matcher.addNewOrder(new Order(2, 30, 30, ActionType.BUY));
        Order match = matcher.findMatchingOrder(new Order(3, 30, 30, ActionType.SELL)).get();

        //Assert
        assertEquals(2, match.getAccount());
    }

    @Test
    void findOldestMatchSELL() {
        //Act
        matcher.addNewOrder(new Order(1, 40, 30, ActionType.BUY));
        matcher.addNewOrder(new Order(2, 40, 30, ActionType.BUY));
        Order match = matcher.findMatchingOrder(new Order(3, 40, 30, ActionType.SELL)).get();

        //Assert
        assertEquals(1, match.getAccount());
    }

    @Test
    void findOldestMatchBUY() {
        //Act
        matcher.addNewOrder(new Order(1, 40, 30, ActionType.SELL));
        matcher.addNewOrder(new Order(2, 40, 30, ActionType.SELL));
        Order match = matcher.findMatchingOrder(new Order(3, 40, 30, ActionType.BUY)).get();

        //Assert
        assertEquals(1, match.getAccount());
    }

    @Test
    void noMatchInEmptyArray() {
        //Act
        Optional<Order> match = matcher.findMatchingOrder(new Order(3, 40, 30, ActionType.BUY));

        //Assert
        assertFalse(!match.isEmpty());
    }

    @Test
    void noMatchInArrayBUY() {
        //Act
        matcher.addNewOrder(new Order(1, 40, 30, ActionType.SELL));
        matcher.addNewOrder(new Order(2, 40, 30, ActionType.SELL));
        Optional<Order> match = matcher.findMatchingOrder(new Order(30, 30, 30, ActionType.BUY));

        //Assert
        assertFalse(!match.isEmpty());
    }
    @Test
    void noMatchInArraySELL() {
        //Act
        matcher.addNewOrder(new Order(1, 40, 30, ActionType.BUY));
        matcher.addNewOrder(new Order(2, 40, 30, ActionType.BUY));
        Optional<Order> match = matcher.findMatchingOrder(new Order(30, 50, 30, ActionType.SELL));

        //Assert
        assertFalse(!match.isEmpty());
    }
    @Test
    void addOrderIfNoneFound() {
        //Act
        matcher.completeTrade(new Order(30, 50, 30, ActionType.SELL));
        List<Order> newList = matcher.getSellOrders();

        //Assert
        assertEquals(1, newList.size());
    }

    @Test
    void removeOrderIfFound() {
        //Act
        matcher.addNewOrder(new Order(2, 40, 30, ActionType.BUY));
        matcher.addNewOrder(new Order(3, 30, 30, ActionType.BUY));
        matcher.completeTrade(new Order(4, 20, 30, ActionType.SELL));
        List<Order> newList = matcher.getBuyOrders();

        //Assert
        assertEquals(1, newList.size());
        assertEquals(2, newList.get(0).getAccount());
    }

    @Test
    void makeTwoMatches() {
        //Act
        matcher.addNewOrder(new Order(2, 40, 10, ActionType.BUY));
        matcher.addNewOrder(new Order(3, 30, 20, ActionType.BUY));
        matcher.completeTrade(new Order(4, 20, 30, ActionType.SELL));
        List<Order> newList = matcher.getBuyOrders();

        //Assert
        assertEquals(0, newList.size());
    }

    @Test
    void makeTwoMatchesOneRemaining() {
        //Act
        matcher.addNewOrder(new Order(2, 40, 10, ActionType.BUY));
        matcher.addNewOrder(new Order(3, 30, 20, ActionType.BUY));
        matcher.addNewOrder(new Order(5, 40, 10, ActionType.BUY));
        matcher.completeTrade(new Order(4, 20, 30, ActionType.SELL));
        List<Order> newList = matcher.getBuyOrders();

        //Assert
        assertEquals(1, newList.size());
        assertEquals(5, newList.get(0).getAccount());
    }

    @Test
    void makePartMatch() {
        //Act
        matcher.addNewOrder(new Order(2, 40, 10, ActionType.BUY));
        matcher.completeTrade(new Order(4, 20, 30, ActionType.SELL));
        List<Order> newList = matcher.getSellOrders();

        //Assert
        assertEquals(1, newList.size());
        assertEquals(20, newList.get(0).getQuantity());
    }

    @Test
    void makePartMatch2() {
        //Act
        matcher.addNewOrder(new Order(2, 40, 30, ActionType.BUY));
        matcher.completeTrade(new Order(4, 20, 10, ActionType.SELL));
        List<Order> newList = matcher.getBuyOrders();

        //Assert
        assertEquals(1, newList.size());
        assertEquals(20, newList.get(0).getQuantity());
    }

    @Test
    void makeTwoAndAHalfBuyMatches() {
        //Act
        matcher.addNewOrder(new Order(2, 40, 10, ActionType.BUY));
        matcher.addNewOrder(new Order(3, 40, 10, ActionType.BUY));
        matcher.addNewOrder(new Order(4, 40, 10, ActionType.BUY));
        matcher.completeTrade(new Order(4, 20, 25, ActionType.SELL));

        List<Order> newList = matcher.getBuyOrders();

        //Assert
        assertEquals(1, newList.size());
        assertEquals(5, newList.get(0).getQuantity());
        assertEquals(4, newList.get(0).getAccount());
    }

    @Test
    void makeTwoAndAHalfSellMatches() {
        //Act
        matcher.addNewOrder(new Order(2, 40, 10, ActionType.SELL));
        matcher.addNewOrder(new Order(3, 40, 10, ActionType.SELL));
        matcher.addNewOrder(new Order(4, 40, 10, ActionType.SELL));
        matcher.completeTrade(new Order(4, 60, 25, ActionType.BUY));

        List<Order> newList = matcher.getSellOrders();

        //Assert
        assertEquals(1, newList.size());
        assertEquals(5, newList.get(0).getQuantity());
        assertEquals(4, newList.get(0).getAccount());
    }

    @Test
    void addTrade() {
        //Act
        matcher.addNewOrder(new Order(2, 40, 10, ActionType.SELL));
        matcher.completeTrade(new Order(4, 50, 25, ActionType.BUY));
        List<Trade> newList = matcher.getTrades();

        //Assert
        assertEquals(1, newList.size());
        assertEquals(10, newList.get(0).getQuantity());
        assertEquals(40, newList.get(0).getPrice());
    }

    @Test
    void addTwoTrades() {
        //Act
        matcher.addNewOrder(new Order(2, 50, 20, ActionType.BUY));
        matcher.addNewOrder(new Order(2, 40, 15, ActionType.BUY));
        matcher.completeTrade(new Order(4, 20, 25, ActionType.SELL));
        List<Trade> newList = matcher.getTrades();

        //Assert
        assertEquals(2, newList.size());
        assertEquals(15, newList.get(0).getQuantity());
        assertEquals(40,newList.get(0).getPrice());
        assertEquals(10, newList.get(1).getQuantity());
        assertEquals(50,newList.get(1).getPrice());
    }
}