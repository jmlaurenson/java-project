package com.example.javaproject.controller;
import com.example.javaproject.ActionType;
import com.example.javaproject.Matcher;
import com.example.javaproject.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class MatcherController {
    Matcher matcher = new Matcher();

    @GetMapping("/")
    public List<Order> index() {
        return Stream.of(matcher.getBuyOrders(), matcher.getSellOrders())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/buyOrders")
    ResponseEntity<List<Order>> fetchBuyOrders() {
        List<Order> orders =matcher.getBuyOrders();
        return orders.isEmpty()
                ? new ResponseEntity<List<Order>>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<List<Order>>(orders, HttpStatus.OK);
    }

    @GetMapping(value = "/sellOrders")
    ResponseEntity<List<Order>> fetchSellOrders() {
        List<Order> orders =matcher.getSellOrders();
        return orders.isEmpty()
                ? new ResponseEntity<List<Order>>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<List<Order>>(orders, HttpStatus.OK);
    }

    @PostMapping(value = "/addOrder")
    ResponseEntity<Order> addOrder(@RequestBody Order order) {
        if (order == null) {
            return new ResponseEntity<Order>(HttpStatus.NO_CONTENT);
        } else {
            matcher.completeTrade(order);
            return new ResponseEntity<>(order, HttpStatus.CREATED);
        }
    }
}
