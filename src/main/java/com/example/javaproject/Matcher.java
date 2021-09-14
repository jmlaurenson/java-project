package com.example.javaproject;
import java.util.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Matcher {
	private Order[] orders = {};



	public void addNewOrder(int account, float price, int quantity, actionType action){
		this.orders = Arrays.copyOf(this.orders, this.orders.length+1);
		int length = this.orders.length;
		this.orders[length-1] = new Order(account, price, quantity, action);
	}

	
}


