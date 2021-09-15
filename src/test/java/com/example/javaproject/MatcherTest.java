package com.example.javaproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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
        assertEquals(order.getPrice(), 40);
        assertEquals(order.getAccount(), 1);
        assertEquals(order.getQuantity(), 30);
        assertEquals(order.getAction(), ActionType.BUY);
    }

    @Test
    void differentActionsAreSeparated() {
        //Act
        matcher.addNewOrder(new Order(1, 40, 30, ActionType.BUY));
        matcher.addNewOrder(new Order(2, 40, 30, ActionType.SELL));
        Order buyOrder = matcher.getBuyOrders().get(0);
        Order sellOrder = matcher.getSellOrders().get(0);

        //Assert
        assertEquals(buyOrder.getAccount(), 1);
        assertEquals(sellOrder.getAccount(), 2);
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
        assertEquals(order0.getPrice(), 30);
        assertEquals(order1.getPrice(), 40);
    }

    @Test
    void oldestOrdersAreFirstInList() {
        //Act
        matcher.addNewOrder(new Order(1, 40, 30, ActionType.BUY));
        matcher.addNewOrder(new Order(2, 40, 30, ActionType.BUY));
        Order order0 = matcher.getBuyOrders().get(0);
        Order order1 = matcher.getBuyOrders().get(1);

        //Assert
        assertEquals(order0.getAccount(), 1);
        assertEquals(order1.getAccount(), 2);
    }

    @Test
    void findExactMatchOneValue() {
        //Act
        matcher.addNewOrder(new Order(1, 40, 30, ActionType.SELL));
        Order match = matcher.findMatchingOrder(new Order(2, 40, 30, ActionType.BUY));

        //Assert
        assertEquals(match.getAccount(), 1);
    }

    @Test
    void findExactMatchTwoValues() {
        //Act
        matcher.addNewOrder(new Order(1, 40, 30, ActionType.BUY));
        matcher.addNewOrder(new Order(2, 30, 30, ActionType.BUY));
        Order match = matcher.findMatchingOrder(new Order(3, 30, 30, ActionType.SELL));

        //Assert
        assertEquals(match.getAccount(), 2);
    }

    @Test
    void findOldestMatchSELL() {
        //Act
        matcher.addNewOrder(new Order(1, 40, 30, ActionType.BUY));
        matcher.addNewOrder(new Order(2, 40, 30, ActionType.BUY));
        Order match = matcher.findMatchingOrder(new Order(3, 40, 30, ActionType.SELL));

        //Assert
        assertEquals(match.getAccount(), 1);
    }

    @Test
    void findOldestMatchBUY() {
        //Act
        matcher.addNewOrder(new Order(1, 40, 30, ActionType.SELL));
        matcher.addNewOrder(new Order(2, 40, 30, ActionType.SELL));
        Order match = matcher.findMatchingOrder(new Order(3, 40, 30, ActionType.BUY));

        //Assert
        assertEquals(match.getAccount(), 1);
    }

    @Test
    void noMatchInEmptyArray() {
        //Act
        Order match = matcher.findMatchingOrder(new Order(3, 40, 30, ActionType.BUY));

        //Assert
        assertNull(match);
    }

    @Test
    void noMatchInArrayBUY() {
        //Act
        matcher.addNewOrder(new Order(1, 40, 30, ActionType.SELL));
        matcher.addNewOrder(new Order(2, 40, 30, ActionType.SELL));
        Order match = matcher.findMatchingOrder(new Order(30, 30, 30, ActionType.BUY));

        //Assert
        assertNull(match);
    }
    @Test
    void noMatchInArraySELL() {
        //Act
        matcher.addNewOrder(new Order(1, 40, 30, ActionType.BUY));
        matcher.addNewOrder(new Order(2, 40, 30, ActionType.BUY));
        Order match = matcher.findMatchingOrder(new Order(30, 50, 30, ActionType.SELL));

        //Assert
        assertNull(match);
    }
    @Test
    void addOrderIfNoneFound() {
        //Act
        matcher.completeTrade(new Order(30, 50, 30, ActionType.SELL));
        List<Order> newList = matcher.getSellOrders();

        //Assert
        assertEquals(newList.size(),1);
    }

    @Test
    void removeOrderIfFound() {
        //Act
        matcher.addNewOrder(new Order(2, 40, 30, ActionType.BUY));
        matcher.addNewOrder(new Order(3, 30, 30, ActionType.BUY));
        matcher.completeTrade(new Order(4, 20, 30, ActionType.SELL));
        List<Order> newList = matcher.getBuyOrders();

        //Assert
        assertEquals(newList.size(),1);
        assertEquals(newList.get(0).getAccount(),2);
    }

    @Test
    void makeTwoMatches() {
        //Act
        matcher.addNewOrder(new Order(2, 40, 10, ActionType.BUY));
        matcher.addNewOrder(new Order(3, 30, 20, ActionType.BUY));
        matcher.completeTrade(new Order(4, 20, 30, ActionType.SELL));
        List<Order> newList = matcher.getBuyOrders();

        //Assert
        assertEquals(newList.size(),0);
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
        assertEquals(newList.size(),1);
        assertEquals(newList.get(0).getAccount(),5);
    }

    @Test
    void makePartMatch() {
        //Act
        matcher.addNewOrder(new Order(2, 40, 10, ActionType.BUY));
        matcher.completeTrade(new Order(4, 20, 30, ActionType.SELL));
        List<Order> newList = matcher.getSellOrders();

        //Assert
        assertEquals(newList.size(),1);
        assertEquals(newList.get(0).getQuantity(),20);
    }

    @Test
    void makePartMatch2() {
        //Act
        matcher.addNewOrder(new Order(2, 40, 30, ActionType.BUY));
        matcher.completeTrade(new Order(4, 20, 10, ActionType.SELL));
        List<Order> newList = matcher.getBuyOrders();

        //Assert
        assertEquals(newList.size(),1);
        assertEquals(newList.get(0).getQuantity(),20);
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
        assertEquals(newList.size(),1);
        assertEquals(newList.get(0).getQuantity(),5);
        assertEquals(newList.get(0).getAccount(),4);
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
        assertEquals(newList.size(),1);
        assertEquals(newList.get(0).getQuantity(),5);
        assertEquals(newList.get(0).getAccount(),4);
    }
}