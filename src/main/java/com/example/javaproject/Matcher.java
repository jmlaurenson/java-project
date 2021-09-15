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

    public int traverseList(Order order, List<Order> orderList){
        return Collections.binarySearch(orderList, order);
    }

    public Order findMatchingOrder(Order order) {
        List<Order> orderList;
        int index = -1;
        while(index == -1){
            if (order.getAction() == ActionType.BUY) {
                orderList = this.sellOrders;
            }
            else {
                orderList = this.buyOrders;
            }
            if(orderList.size()==0){
                return null;
            }
            index = traverseList(order, orderList);
            // If no match is found
            if(index == -1){
                if(order.getAction()==ActionType.BUY) {
                    order.setPrice(order.getPrice() - 1);
                }
                else{
                    order.setPrice(order.getPrice() + 1);
                }
            }
            else{
                return orderList.get(index);
            }

        }
        return order;
    }





}


