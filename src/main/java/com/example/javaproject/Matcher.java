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

    private boolean compareOrders(Order order, Order a){
        return a.getAction() == ActionType.BUY ? a.getPrice() >= order.getPrice() : a.getPrice() <= order.getPrice();
    }

    public Optional<Order> findMatchingOrder(Order order) {
        List<Order> orderList = order.getAction() == ActionType.BUY ? this.sellOrders : this.buyOrders;

        return orderList
                .stream()
                .filter(a -> compareOrders(order, a))
                .findFirst();
    }

    /**
     * Looks for trades that match with the new order until there is no quantity left
     * @param order the order to be added
     */
    public void completeTrade(Order order){
        while (order.getQuantity()>0){
            Optional<Order> match = findMatchingOrder(order);
            //If no match is found
            if(match.isEmpty()) {
                addNewOrder(order);
                return;
            }
            this.trades.add(new Trade(match.get(), order));
            //If the quantity of the matched order is less than the new orders quantity
            if(match.get().getQuantity()-order.getQuantity()>0){
                match.get().setQuantity(match.get().getQuantity()-order.getQuantity());
                addNewOrder(match.get());
            }
            order.setQuantity(order.getQuantity()-match.get().getQuantity());

            //Remove the matched order
            if(order.getAction()==ActionType.BUY){

                this.sellOrders.remove(match.get());
            }
            else{
                this.buyOrders.remove(match.get());
            }
        }
    }
}


