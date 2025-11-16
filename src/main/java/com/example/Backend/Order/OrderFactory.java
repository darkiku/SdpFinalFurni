package com.example.Backend.Order;

import com.example.Backend.Cart.Cart;
import com.example.Backend.User.User;
import java.time.LocalDateTime;
import java.util.List;

public class OrderFactory {

    public static Order createOrder(String type, User user, List<Cart> items, Double totalPrice) {
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("Order type cannot be null or empty");
        }
        if (totalPrice == null || totalPrice < 0) {
            throw new IllegalArgumentException("Total price must be non-negative");
        }
        Order order = new Order();
        order.setUser(user);
        order.setItems(items);
        order.setTotalPrice(totalPrice);
        order.setOrderType(type);
        order.setCreatedAt(LocalDateTime.now());
        switch (type.toUpperCase()) {
            case "ONLINE":
                order.setStatus("Processing");
                order.setDeliveryMethod("Standard Shipping");
                break;
            case "INSTORE":
                order.setStatus("Ready for Pickup");
                order.setDeliveryMethod("In-Store Pickup");
                break;
            case "SUBSCRIPTION":
                order.setStatus("Active");
                order.setDeliveryMethod("Recurring Delivery");
                break;
            case "EXPRESS":
                order.setStatus("Priority Processing");
                order.setDeliveryMethod("Express Delivery");
                break;
            default:
                order.setStatus("Pending");
                order.setDeliveryMethod("Standard");
        }

        return order;
    }
}
