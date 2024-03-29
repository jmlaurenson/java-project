package com.example.javaproject;

import java.math.BigDecimal;
import java.util.*;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@EqualsAndHashCode
@Service
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
        return a.getAction() == ActionType.BUY ? order.getPrice().compareTo(a.getPrice())<1   : order.getPrice().compareTo(a.getPrice())>-1 ;
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
        while (order.getQuantity().compareTo(BigDecimal.ZERO)>-1){
            Optional<Order> optionalMatch = findMatchingOrder(order);
            //If no match is found
            if(optionalMatch.isEmpty()) {
                addNewOrder(order);
                return;
            }
            Order match = optionalMatch.get();
            this.trades.add(new Trade(match, order));
            double matchQuantity = match.getQuantity().doubleValue();
            double orderQuantity = order.getQuantity().doubleValue();
            //If the quantity of the matched order is less than the new orders quantity
            if(matchQuantity-orderQuantity>0){
                match.setQuantity(new BigDecimal(matchQuantity-orderQuantity));
                addNewOrder(match);
            }
            order.setQuantity(new BigDecimal(orderQuantity-matchQuantity));

            //Remove the matched order
            if(order.getAction()== ActionType.BUY){

                this.sellOrders.remove(match);
            }
            else{
                this.buyOrders.remove(match);
            }
        }
    }
}


