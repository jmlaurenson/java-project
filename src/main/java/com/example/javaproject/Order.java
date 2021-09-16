package com.example.javaproject;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Order implements Comparable<Order> {
    private int account;
    private float price;
    private int quantity;
    private ActionType action;
    private boolean isValid;

    public Order(int account, float price, int quantity, ActionType action) {
        this.account = account;
        this.price = price;
        this.quantity = quantity;
        this.action = action;
        this.isValid = true;
        checkIfValid();
    }

    private void checkIfValid() {
        if (this.account < 0 || this.price <= 0 || this.quantity < 1) {
            this.isValid = false;
        }
    }

    public int compareTo(Order order) {
        return Float.compare(this.getPrice(), order.getPrice());
    }
}
