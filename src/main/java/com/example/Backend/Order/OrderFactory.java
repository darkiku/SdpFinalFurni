package com.example.Backend.Order;

import com.example.Backend.Cart.Cart;
import com.example.Backend.User.User;

import java.util.List;

public class OrderFactory {
    public static Order createOrder(String type,User user,List<Cart> items,Double totalPrice){
        Order order=new Order();
        order.setUser(user);
        order.setItems(items);
        order.setTotalPrice(totalPrice);
        order.setOrderType(type);
        switch(type){
            case "ONLINE":
                order.setStatus("Processing");
                break;
            case "INSTORE":
                order.setStatus("Ready for Pickup");
                break;
            case "SUBSCRIPTION":
                order.setStatus("Active");
                break;
            default:
                order.setStatus("Pending");
        }
        return order;
    }
}