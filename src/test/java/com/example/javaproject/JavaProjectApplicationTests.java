package com.example.javaproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class JavaProjectApplicationTests {
	private Matcher matcher;

	@BeforeEach
	void setUp(){
		matcher = new Matcher();
	}

	@Test
	void matcherAddsOrders() {
		//Act
		matcher.addNewOrder(1,40,30,actionType.BUY);
		Order order = matcher.getBuyOrders()[0];

		//Assert
		assertEquals(order.getPrice(), 40);
		assertEquals(order.getAccount(), 1);
		assertEquals(order.getQuantity(), 30);
		assertEquals(order.getAction(), actionType.BUY);
	}

	@Test
	void invalidOrdersAreCaught() {
		//Act
		matcher.addNewOrder(-10,40,30,actionType.BUY);
		matcher.addNewOrder(1,0,30,actionType.BUY);
		matcher.addNewOrder(1,40,0,actionType.BUY);
		Order[] orders = matcher.getBuyOrders();

		//Assert
		assertEquals(orders[0].isValid(), false);
		assertEquals(orders[1].isValid(), false);
		assertEquals(orders[2].isValid(), false);
	}
	@Test
	void matcherAddsBuyOrdersToCorrectPlace() {
		//Act
		matcher.addNewOrder(1,40,30,actionType.BUY);
		matcher.addNewOrder(1,30,30,actionType.BUY);
		Order order0 = matcher.getBuyOrders()[0];
		Order order1 = matcher.getBuyOrders()[1];

		//Assert
		assertEquals(order0.getPrice(), 30);
		assertEquals(order1.getPrice(), 40);
	}
}
