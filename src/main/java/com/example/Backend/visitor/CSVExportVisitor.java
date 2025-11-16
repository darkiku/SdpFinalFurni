package com.example.Backend.visitor;

import com.example.Backend.Order.Order;

public class CSVExportVisitor implements OrderVisitor {
    @Override
    public String export(Order order) {
        return String.format("%d,%s,%.2f,%s,%s",
                order.getId(),
                order.getOrderType(),
                order.getTotalPrice(),
                order.getStatus(),
                order.getCreatedAt()
        );
    }
}