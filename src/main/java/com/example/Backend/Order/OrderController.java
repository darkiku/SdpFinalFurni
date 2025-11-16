package com.example.Backend.Order;

import com.example.Backend.visitor.CSVExportVisitor;
import com.example.Backend.visitor.JSONExportVisitor;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Transactional
@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/save")
    public ResponseEntity<Order> save(@Valid @RequestBody Order order) {
        orderService.createOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
    @PostMapping("/createWithFactory")
    public ResponseEntity<?> createWithFactory(@RequestBody Map<String, Object> request) {
        try {
            String type = (String) request.get("type");
            Long userId = request.containsKey("userId") ?
                    Long.parseLong(request.get("userId").toString()) : null;
            Double totalPrice = Double.parseDouble(request.get("totalPrice").toString());

            Order order = orderService.createOrderWithFactory(type, null, null, totalPrice);

            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to create order: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Order> findById(@PathVariable Long id) {
        try {
            Order order = orderService.findById(id);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Order>> findAll() {
        List<Order> orders = orderService.findAll();
        return ResponseEntity.ok(orders);
    }
    @GetMapping("/export/{id}")
    public ResponseEntity<Map<String, String>> exportOrder(
            @PathVariable Long id,
            @RequestParam String format) {

        try {
            String result;

            if (format.equalsIgnoreCase("json")) {
                result = orderService.exportOrder(id, new JSONExportVisitor());
            } else if (format.equalsIgnoreCase("csv")) {
                result = orderService.exportOrder(id, new CSVExportVisitor());
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid format. Use 'json' or 'csv'");
                return ResponseEntity.badRequest().body(error);
            }

            Map<String, String> response = new HashMap<>();
            response.put("format", format);
            response.put("data", result);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Order not found");
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/exportAll")
    public ResponseEntity<Map<String, Object>> exportAllOrders(@RequestParam String format) {
        List<Order> orders = orderService.findAll();
        List<String> exportedOrders = new ArrayList<>();

        if (format.equalsIgnoreCase("json")) {
            JSONExportVisitor visitor = new JSONExportVisitor();
            for (Order order : orders) {
                exportedOrders.add(order.accept(visitor));
            }
        } else if (format.equalsIgnoreCase("csv")) {
            CSVExportVisitor visitor = new CSVExportVisitor();
            exportedOrders.add("OrderID,Type,TotalPrice,Status,CreatedAt"); // Header
            for (Order order : orders) {
                exportedOrders.add(order.accept(visitor));
            }
        } else {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Invalid format. Use 'json' or 'csv'");
            return ResponseEntity.badRequest().body(error);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("format", format);
        response.put("count", orders.size());
        response.put("data", exportedOrders);

        return ResponseEntity.ok(response);
    }
}