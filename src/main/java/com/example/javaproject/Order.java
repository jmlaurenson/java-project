package com.example.javaproject;

public class Order {
    private int account;
    private float price;
    private int quantity;
    private actionType action;

    public Order(int account, float price, int quantity, actionType action) {
        this.account = account;
        this.price = price;
        this.quantity = quantity;
        this.action = action;
    }


    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public actionType getAction() {
        return action;
    }

    public void setAction(actionType action) {
        this.action = action;
    }
}
