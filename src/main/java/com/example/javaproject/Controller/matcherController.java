package com.example.javaproject.controller;
import com.example.javaproject.Matcher;
import com.example.javaproject.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    ResponseEntity addOrder(@Valid @RequestBody Order order, Errors errors, BindingResult result) {
        if (result.hasErrors()) {
            System.out.println("Errors present");
            for (ObjectError objectError : errors.getAllErrors()) {
                System.out.println("errors : " + objectError.getDefaultMessage());

            }
            return new ResponseEntity(errors.getAllErrors().toString(), HttpStatus.BAD_REQUEST);
        }
        matcher.completeTrade(order);
        return new ResponseEntity<>(order, HttpStatus.CREATED);

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageConversionException.class)
    String handleException(HttpMessageConversionException e){
        return "Error reading order values: "+e.getLocalizedMessage();
    }
}
