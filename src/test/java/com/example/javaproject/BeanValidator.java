package com.example.javaproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class BeanValidator {
    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeEach
    public void createValidator() {
        //Build a ValidatorFactory instance based on the default Bean Validation provider
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Check that a negative account cannot be added")
    public void checkForNegativeAccount() {
        Order order = new Order(-10, 40, 30, ActionType.BUY);
        Set<ConstraintViolation<Order>> violations = validator.validate(order);
        for (ConstraintViolation<Order> violation : violations) {
            System.out.println(violation.getMessage()); // Print all the violations
        }
        assertEquals(1, violations.size(), violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Check that an order price cannot have a value of zero or under")
    public void checkForPriceAtZero() {
        Order order = new Order(10, 0, 30, ActionType.BUY);
        Set<ConstraintViolation<Order>> violations = validator.validate(order);
        for (ConstraintViolation<Order> violation : violations) {
            System.out.println(violation.getMessage());// Print all the violations
        }
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Check that an order quantity cannot have a value of zero or under")
    public void checkForZeroQuantity() {
        Order order = new Order(10, 40, 0, ActionType.BUY);
        Set<ConstraintViolation<Order>> violations = validator.validate(order);
        for (ConstraintViolation<Order> violation : violations) {
            System.out.println(violation.getMessage());// Print all the violations
        }
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Check that a price cannot have more than two decimal places")
    public void checkForPriceWithMoreThanTwoDecimalPlaces() {
        Order order = new Order(10, 40.123, 30, ActionType.BUY);
        Set<ConstraintViolation<Order>> violations = validator.validate(order);
        for (ConstraintViolation<Order> violation : violations) {
            System.out.println(violation.getMessage()); // Print all the violations
        }
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Check that an order price cannot be more than 999,999,999")
    public void checkForPriceWithMoreThanNineIntegralDigits() {
        Order order = new Order(10, 1000000000, 30, ActionType.BUY);
        Set<ConstraintViolation<Order>> violations = validator.validate(order);
        for (ConstraintViolation<Order> violation : violations) {
            System.out.println(violation.getMessage()); // Print all the violations
        }
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Check that a trade price cannot equal zero or less")
    public void checkForTradePriceAtZero() {
        Order order1 = new Order(10, 0.5, 30.6, ActionType.BUY);
        Order order2 = new Order(10, 0.5, 30.6, ActionType.BUY);
        Trade trade = new Trade(order1, order2);
        trade.setPrice(0.0);
        Set<ConstraintViolation<Trade>> violations = validator.validate(trade);
        for (ConstraintViolation<Trade> violation : violations) {
            System.out.println(violation.getMessage());
        }
        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("Check that a trade quantity cannot equal zero or less")
    public void checkForTradeZeroQuantity() {
        Order order1 = new Order(10, 0.5, 30.6, ActionType.BUY);
        Order order2 = new Order(10, 0.5, 30.6, ActionType.BUY);
        Trade trade = new Trade(order1, order2);
        trade.setQuantity(0.0);
        Set<ConstraintViolation<Trade>> violations = validator.validate(trade);
        for (ConstraintViolation<Trade> violation : violations) {
            System.out.println(violation.getMessage());
        }
        assertEquals(1, violations.size());
    }
}