package com.example.Backend.visitor;

import com.example.Backend.Order.Order;

public interface OrderVisitor {
    String export(Order order);
}