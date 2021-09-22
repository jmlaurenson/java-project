package com.example.javaproject;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
public class Trade {
    private Order oldOrder;
    private  Order newOrder;

    @DecimalMin("0.01")
    @Digits(integer=9, fraction=2)
    @NotNull
    private double price;

    @DecimalMin("1")
    @NotNull
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
