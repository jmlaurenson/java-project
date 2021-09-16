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

    public void addNewOrder(Order order) {
        if (order.getAction() == ActionType.BUY) {
            this.buyOrders.add(order);
        } else {
            this.sellOrders.add(order);
        }
    }


}


