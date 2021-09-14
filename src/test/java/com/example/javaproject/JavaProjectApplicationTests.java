package com.example.javaproject;

import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class JavaProjectApplicationTests {
	private static Matcher matcher;

	@BeforeAll
	static void setUp(){
		System.out.println("Test data set up..");
		matcher = new Matcher();
	}

	@Test
	void matcherAddsOrders() {
		//Act
		matcher.addNewOrder(1,40,30,actionType.BUY);
		Order order = matcher.getOrders()[0];

		//Assert
		assertEquals(order.getPrice(), 40);
		assertEquals(order.getAccount(), 1);
		assertEquals(order.getQuantity(), 30);
		assertEquals(order.getAction(), actionType.BUY);
	}

}
