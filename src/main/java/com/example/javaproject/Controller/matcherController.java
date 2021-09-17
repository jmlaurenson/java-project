package com.example.javaproject.controller;
import com.example.javaproject.ActionType;
import com.example.javaproject.Matcher;
import com.example.javaproject.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;


@Controller
@EnableWebMvc
@RequestMapping("/matcher")
public class MatcherController {
    Matcher matcher = new Matcher();

    @RequestMapping(value = "/buyOrders", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<Order>> fetchBuyOrders() {
        matcher.completeTrade(new Order(2,3,4, ActionType.BUY));
        List<Order> orders =matcher.getBuyOrders();
        return new ResponseEntity<List<Order>>(orders, HttpStatus.OK);
    }

    @RequestMapping(value = "/sellOrders", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<List<Order>> fetchSellOrders() {
        matcher.completeTrade(new Order(2,6,4, ActionType.SELL));
        List<Order> orders =matcher.getSellOrders();
        return new ResponseEntity<List<Order>>(orders, HttpStatus.OK);
    }
}
