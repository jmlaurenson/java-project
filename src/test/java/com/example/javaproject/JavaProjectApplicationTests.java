package com.example.javaproject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class JavaProjectApplicationTests {

	@Test
	void matcherAddsOrders() {
		//Arrange
		Matcher matcher = new Matcher();

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
