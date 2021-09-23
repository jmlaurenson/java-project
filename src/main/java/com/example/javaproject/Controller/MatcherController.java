package com.example.javaproject.Controller;
import com.example.javaproject.Matcher;
import com.example.javaproject.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.FieldError;
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
    public ResponseEntity<List<Order>> index() {
        List<Order> orders =  Stream.of(matcher.getBuyOrders(), matcher.getSellOrders())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }


    @GetMapping(value = "/buyOrders")
    ResponseEntity<List<Order>> fetchBuyOrders() {
        List<Order> orders =matcher.getBuyOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping(value = "/sellOrders")
    ResponseEntity<List<Order>> fetchSellOrders() {
        List<Order> orders =matcher.getSellOrders();
        return ResponseEntity.ok(orders);
    }

    @PostMapping(value = "/addOrder")
    ResponseEntity<Order> addOrder(@Valid @RequestBody Order order) {
        matcher.completeTrade(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
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
