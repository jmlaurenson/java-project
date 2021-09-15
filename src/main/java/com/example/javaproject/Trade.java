package com.example.javaproject;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
public class Trade {
    private Order oldOrder;
    private  Order newOrder;
    private float price;
    private int quantity;
    private String date;
    private String time;


    public Trade(Order oldOrder, Order newOrder, int quantity) {
        this.oldOrder = oldOrder;
        this.newOrder = newOrder;
        this.price = oldOrder.getPrice();
        this.quantity = quantity;
    }

    public void setDataTime(){
        Date dNow = new Date( );
        SimpleDateFormat dateFormat =
                new SimpleDateFormat ("dd/MM/yyyy");
        SimpleDateFormat timeFormat =
                new SimpleDateFormat ("hh:mm");
        this.date = dateFormat.format(dNow);
        this.time = timeFormat.format(dNow);

    }
}
