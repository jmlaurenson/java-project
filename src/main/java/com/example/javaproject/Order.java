package com.example.javaproject;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
@Validated
public class Order implements Comparable<Order> {
    @Min(0)
    @NotNull
    private int account;

    @DecimalMin("0.01")
    @Digits(integer=9, fraction=2)
    @NotNull
    private BigDecimal price;

    @DecimalMin("1")
    @NotNull
    private BigDecimal quantity;

    @NotNull
    private ActionType action;
    private boolean isValid;

    public Order(int account, double price, double quantity, ActionType action) {
        this.account = account;
        this.price = new BigDecimal(price+"");
        this.quantity = new BigDecimal(quantity+"");
        this.action = action;
        this.isValid = true;
    }

    public int compareTo(Order order) {
        return Double.compare(this.getPrice().doubleValue(), order.getPrice().doubleValue());
    }
}
