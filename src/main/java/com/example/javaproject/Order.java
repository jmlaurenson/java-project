package com.example.javaproject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order {
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

    private void checkIfValid(){
        if(this.account<0 || this.price<=0 || this.quantity<1){
            this.isValid=false;
        }
    }
}
