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
    private float price;
    private int quantity;
    private Instant date = Instant.now();

    public Trade(Order oldOrder, Order newOrder) {
        this.oldOrder = oldOrder;
        this.newOrder = newOrder;
        this.price = oldOrder.getPrice();
        int quantity = newOrder.getQuantity();
        if(oldOrder.getQuantity()< newOrder.getQuantity()){
            quantity = oldOrder.getQuantity();
        }
        this.quantity = quantity;
    }
}
