package com.example.javaproject;

import java.util.*;
import java.util.stream.Collectors;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Matcher {

    private List<Order> buyOrders = new ArrayList<Order>();
    private List<Order> sellOrders = new ArrayList<Order>();
    private List<Trade> trades = new ArrayList<Trade>();

    public void addNewOrder(Order order) {
        if (order.getAction() == ActionType.BUY) {
            this.buyOrders.add(order);
            Collections.sort(this.buyOrders);
        } else {
            this.sellOrders.add(order);
            Collections.sort(this.sellOrders);
        }
    }

    public Optional<Order> traverseList(Order order, List<Order> orderList) {
        return(orderList
                .stream()
                .filter(a -> (
                a.getPrice() >= order.getPrice() && a.getAction() == ActionType.BUY)
                || (a.getPrice() <= order.getPrice() && a.getAction() == ActionType.SELL) )
                .findFirst());
    }

    public Order findMatchingOrder(Order order) {
        List<Order> orderList;
        if (order.getAction() == ActionType.BUY) {
            orderList = this.sellOrders;
        } else {
            orderList = this.buyOrders;
        }
        if (orderList.size() == 0) {
            return null;
        }
        Optional<Order> matchList = traverseList(order, orderList);

        // If a match is found
        if (matchList.isPresent()) {
            System.out.println(matchList.get());
            return matchList.get();
        } else {
            return null;
        }
    }

    /**
     * Looks for trades that match with the new order until there is no quantity left
     * @param order the order to be added
     */
    public void completeTrade(Order order){
        while (order.getQuantity()>0){
            Order match = findMatchingOrder(order);
            //If no match is found
            if(match==null) {
                addNewOrder(order);
                return;
            }
            this.trades.add(new Trade(match, order));
            //If the quantity of the matched order is less than the new orders quantity
            if(match.getQuantity()-order.getQuantity()>0){
                match.setQuantity(match.getQuantity()-order.getQuantity());
                addNewOrder(match);
            }
            order.setQuantity(order.getQuantity()-match.getQuantity());

            //Remove the matched order
            if(order.getAction()==ActionType.BUY){

                this.sellOrders.remove(match);
            }
            else{
                this.buyOrders.remove(match);
            }

        }
    }
}


