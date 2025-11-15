package com.example.Backend.visitor;

import com.example.Backend.Order.Order;

public class CSVExportVisitor implements OrderVisitor {
    public String export(Order order){
        return order.getId()+","+order.getTotalPrice()+","+order.getStatus();
    }
}