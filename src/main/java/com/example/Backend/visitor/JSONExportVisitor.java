package com.example.Backend.visitor;

import com.example.Backend.Order.Order;

public class JSONExportVisitor implements OrderVisitor {
    public String export(Order order){
        return "{\"orderId\":"+order.getId()+",\"total\":"+order.getTotalPrice()+",\"status\":\""+order.getStatus()+"\"}";
    }
}