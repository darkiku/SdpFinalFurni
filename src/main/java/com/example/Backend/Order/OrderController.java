package com.example.Backend.Order;

import com.example.Backend.Cart.Cart;
import com.example.Backend.User.User;
import com.example.Backend.visitor.JSONExportVisitor;
import com.example.Backend.visitor.CSVExportVisitor;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService=orderService;
    }
    @PostMapping("/save")
    public ResponseEntity<Order> save(@Valid @RequestBody Order order){
        orderService.createOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
    @PostMapping("/createWithFactory")
    public ResponseEntity<Order> createWithFactory(@RequestBody Map<String,Object> request){
        String type=(String)request.get("type");
        Long userId=Long.parseLong(request.get("userId").toString());
        Double totalPrice=Double.parseDouble(request.get("totalPrice").toString());
        Order order=orderService.createOrderWithFactory(type,null,null,totalPrice);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
    @GetMapping("/findById/{id}")
    public ResponseEntity<Order> findById(@PathVariable Long id){
        Order order=orderService.findById(id);
        return ResponseEntity.ok(order);
    }
    @GetMapping("/findAll")
    public ResponseEntity<List<Order>> findAll(){
        List<Order> orders=orderService.findAll();
        return ResponseEntity.ok(orders);
    }
    @GetMapping("/export/{id}")
    public ResponseEntity<Map<String,String>> exportOrder(@PathVariable Long id,@RequestParam String format){
        String result="";
        if(format.equals("json")){
            result=orderService.exportOrder(id,new JSONExportVisitor());
        }else if(format.equals("csv")){
            result=orderService.exportOrder(id,new CSVExportVisitor());
        }
        Map<String,String> response=new HashMap<>();
        response.put("format",format);
        response.put("data",result);
        return ResponseEntity.ok(response);
    }
}