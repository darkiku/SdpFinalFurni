package com.example.Backend.Order;

import com.example.Backend.Cart.Cart;
import com.example.Backend.User.User;
import com.example.Backend.visitor.OrderVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderService {
    private OrderRepository orderRepository;
    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void createOrder(Order order) {
        orderRepository.save(order);
    }

    public Order createOrderWithFactory(String type, User user, List<Cart> items, Double totalPrice) {
        Order order = OrderFactory.createOrder(type, user, items, totalPrice);
        orderRepository.save(order);
        return order;
    }
    public String exportOrder(Long orderId, OrderVisitor visitor) {
        Order order = findById(orderId);
        return order.accept(visitor);
    }
    public Order findById(long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }
}