package com.example.javaproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.*;

@SpringBootApplication
public class Matcher {
	private Order[] orders = {};



	public void addNewOrder(int account, float price, int quantity, actionType action){
		this.orders = Arrays.copyOf(this.orders, this.orders.length+1);
		int length = this.orders.length;
		this.orders[length-1] = new Order(account, price, quantity, action);
	}

	public Order[] getOrders() {
		return orders;
	}

	public void setOrders(Order[] orders) {
		this.orders = orders;
	}
}


