package com.example.javaproject;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
public class Trade {
    private Order oldOrder;
    private  Order newOrder;
    private double price;
    private double quantity;
    private Instant date = Instant.now();

    public Trade(Order oldOrder, Order newOrder) {
        this.oldOrder = oldOrder;
        this.newOrder = newOrder;
        this.price = oldOrder.getPrice().doubleValue();
        double quantity = newOrder.getQuantity().doubleValue();
        if(oldOrder.getQuantity().doubleValue()< newOrder.getQuantity().doubleValue()){
            quantity = oldOrder.getQuantity().doubleValue();
        }
        this.quantity = quantity;
    }
}
