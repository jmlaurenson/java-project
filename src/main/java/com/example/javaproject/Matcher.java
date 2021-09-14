package com.example.javaproject;
import java.util.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Matcher {
	private Order[] buyOrders = {};
	private Order[] sellOrders = {};

	private Order[] insertOrder(Order[] orders, int account, float price, int quantity, actionType action){
		//Insert order in the correct position
		orders = Arrays.copyOf(orders, orders.length+1);
		int length = orders.length;
		orders[length-1] = new Order(account, price, quantity, action);
		System.out.println(orders.length);
		return orders;
	}

	public void addNewOrder(int account, float price, int quantity, actionType action){
		if(action==actionType.BUY){
			this.buyOrders = insertOrder(this.buyOrders, account, price, quantity, action);
		}
		else {
			this.sellOrders = insertOrder(this.sellOrders, account, price, quantity, action);
		}
	}
}


