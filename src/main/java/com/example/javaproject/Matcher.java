package com.example.javaproject;

import java.util.*;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Matcher {

    private List<Order> buyOrders = new ArrayList<Order>();
    private List<Order> sellOrders = new ArrayList<Order>();

    public void addNewOrder(int account, float price, int quantity, ActionType action) {
        if (action == ActionType.BUY) {
            this.buyOrders.add(new Order(account, price, quantity, action));
        } else {
            this.sellOrders.add(new Order(account, price, quantity, action));
        }
    }
}


