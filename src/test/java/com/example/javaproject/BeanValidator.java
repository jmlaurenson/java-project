package com.example.javaproject;

import org.junit.AfterClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class BeanValidator {
    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeEach
    public void createValidator() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterClass
    public void close() {
        factory.close();
    }

    @Test
    public void checkForNegativeAccount() throws Exception {
        Order order = new Order(-10, 40, 30, ActionType.BUY);
        Set<ConstraintViolation<Order>> violations = validator.validate(order);
        for (ConstraintViolation<Order> violation : violations) {
            System.out.println(violation.getMessage());
        }
        assertEquals(1, violations.size());
    }

    @Test
    public void checkForPriceAtZero() throws Exception {
        Order order = new Order(10, 0, 30, ActionType.BUY);
        Set<ConstraintViolation<Order>> violations = validator.validate(order);
        for (ConstraintViolation<Order> violation : violations) {
            System.out.println(violation.getMessage());
        }
        assertEquals(1, violations.size());
    }

    @Test
    public void checkForZeroQuantity() throws Exception {
        Order order = new Order(10, 40, 0, ActionType.BUY);
        Set<ConstraintViolation<Order>> violations = validator.validate(order);
        for (ConstraintViolation<Order> violation : violations) {
            System.out.println(violation.getMessage());
        }
        assertEquals(1, violations.size());
    }

    @Test
    public void checkForPriceWithMoreThanTwoDecimalPlaces() throws Exception {
        Order order = new Order(10, 40.123, 30, ActionType.BUY);
        Set<ConstraintViolation<Order>> violations = validator.validate(order);
        for (ConstraintViolation<Order> violation : violations) {
            System.out.println(violation.getMessage());
        }
        assertEquals(1, violations.size());
    }

    @Test
    public void checkForPriceWithMoreThanNineIntegralDigits() throws Exception {
        Order order = new Order(10, 1000000000, 30, ActionType.BUY);
        Set<ConstraintViolation<Order>> violations = validator.validate(order);
        for (ConstraintViolation<Order> violation : violations) {
            System.out.println(violation.getMessage());
        }
        assertEquals(1, violations.size());
    }
}