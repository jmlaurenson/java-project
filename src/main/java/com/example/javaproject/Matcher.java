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
            Collections.sort(this.buyOrders);
        } else {
            this.sellOrders.add(order);
            Collections.sort(this.sellOrders);
        }
    }

    public void traverseList(Order order, List<Order> orderList){
        System.out.println(Collections.binarySearch(orderList, order));
    }

    public void findMatchingOrder(Order order) {
        if (order.getAction() == ActionType.BUY) {
            traverseList(order, this.sellOrders);
        } else {
            //Look in buy orders List
        }
    }





}


