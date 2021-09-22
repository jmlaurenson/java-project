package com.example.javaproject.controller;
import com.example.javaproject.Matcher;
import com.example.javaproject.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class MatcherController {
    private Matcher matcher;

    //tells Spring to automatically use its own dependency features
    @Autowired
    public void setMatcher(Matcher matcher) {
        this.matcher = matcher;
    }

    @GetMapping("/")
    public ResponseEntity index() {
        List<Order> orders =  Stream.of(matcher.getBuyOrders(), matcher.getSellOrders())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        return orders.isEmpty()
                ? new ResponseEntity<>("Order list is empty", HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(orders, HttpStatus.OK);
    }


    @GetMapping(value = "/buyOrders")
    ResponseEntity fetchBuyOrders() {
        List<Order> orders =matcher.getBuyOrders();
        //If order list is empty, return a 204 response
        return orders.isEmpty()
                ? new ResponseEntity<>("Order list is empty", HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping(value = "/sellOrders")
    ResponseEntity fetchSellOrders() {
        List<Order> orders =matcher.getSellOrders();
        //If order list is empty, return a 204 response
        return orders.isEmpty()
                ? new ResponseEntity<>("Order list is empty",HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PostMapping(value = "/addOrder")
    ResponseEntity addOrder(@RequestBody Order order) {
        matcher.completeTrade(order);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
}
