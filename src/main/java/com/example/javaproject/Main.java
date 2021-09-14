package com.example.javaproject;


import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        Matcher matcher = new Matcher();
        Order[] matcherData = {new Order(1,1,1,actionType.BUY), new Order(1,2,1,actionType.BUY), new Order(1,3,1,actionType.BUY), new Order(1,4,1,actionType.BUY)};
        matcher.setBuyOrders(matcherData);
        matcher.addNewOrder(3,2,3,actionType.SELL);
    }
}
