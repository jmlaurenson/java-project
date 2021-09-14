package com.example.javaproject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order {
    @Getter @Setter private int account;
    @Getter @Setter private float price;
    @Getter @Setter private int quantity;
    @Getter @Setter private actionType action;

    public Order(int account, float price, int quantity, actionType action) {
        this.account = account;
        this.price = price;
        this.quantity = quantity;
        this.action = action;
    }
}
