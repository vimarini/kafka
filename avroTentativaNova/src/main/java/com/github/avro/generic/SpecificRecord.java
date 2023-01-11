package com.github.avro.generic;

import com.example.Order;

public class SpecificRecord {
    public static void main(String[] args) {
        Order.Builder orderBuilder = Order.newBuilder();
        orderBuilder.setId(3);
        orderBuilder.setInfo("Duzia de ovos");
        Order order = orderBuilder.build();
        System.out.println(order);
    }
}
