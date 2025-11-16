package com.example.Backend.visitor;

import com.example.Backend.Order.Order;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONExportVisitor implements OrderVisitor {
    @Override
    public String export(Order order) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(order);
        } catch (Exception e) {
            // Fallback
            return String.format(
                    "{\"orderId\":%d,\"total\":%.2f,\"status\":\"%s\",\"type\":\"%s\"}",
                    order.getId(),
                    order.getTotalPrice(),
                    order.getStatus(),
                    order.getOrderType()
            );
        }
    }
}
