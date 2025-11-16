package com.example.Backend.Cart;

import com.example.Backend.Product.Product;
import com.example.Backend.discount.PercentageDiscount;
import com.example.Backend.discount.FixedDiscount;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@Transactional
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/showCart/{id}")
    public ResponseEntity<List<Product>> showCart(@PathVariable Long id){
        Cart cart=cartService.getCartById(id);
        List<Product> products=cartService.getProductsFromCart(cart);
        return ResponseEntity.ok(products);
    }
    @PostMapping("/addToCart/{cartId}/{productId}")
    public ResponseEntity<Cart> addToCart(@PathVariable Long cartId,@PathVariable Long productId){
        Cart cart=cartService.addToCart(cartId,productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(cart);
    }
    @DeleteMapping("/removeFromCart/{id}/{productId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long id,@PathVariable Long productId){
        cartService.removeFromCart(id,productId);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/clearCart/{id}")
    public ResponseEntity<Void> clearCart(@PathVariable Long id){
        cartService.clearCart(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/calculate/{cartId}")
    public ResponseEntity<Map<String,Object>> calculatePrice(@PathVariable Long cartId){
        Cart cart=cartService.getCartById(cartId);
        Double originalPrice=cart.getTotalPrice();
        Double discount=0.0;
        String message="No discount applied";
        if(originalPrice>500){
            discount=originalPrice*0.15;
            message="15% VIP discount applied!";
        }else if(originalPrice>200){
            discount=originalPrice*0.10;
            message="10% bulk discount applied!";
        }else if(originalPrice>100){
            discount=originalPrice*0.05;
            message="5% starter discount applied!";
        }
        Double finalPrice=originalPrice-discount;
        Map<String,Object> response=new HashMap<>();
        response.put("originalPrice",originalPrice);
        response.put("discount",discount);
        response.put("finalPrice",finalPrice);
        response.put("message",message);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/applyDiscount/{cartId}")
    public ResponseEntity<Map<String,Object>> applyDiscount(@PathVariable Long cartId,@RequestParam String type,@RequestParam double value){
        Cart cart=cartService.getCartById(cartId);
        Double originalPrice=cart.getTotalPrice();
        Double finalPrice=originalPrice;
        if(type.equals("percentage")){
            PercentageDiscount discount=new PercentageDiscount(value);
            finalPrice=cartService.applyDiscount(cartId,discount);
        }else if(type.equals("fixed")){
            FixedDiscount discount=new FixedDiscount(value);
            finalPrice=cartService.applyDiscount(cartId,discount);
        }
        Map<String,Object> response=new HashMap<>();
        response.put("originalPrice",originalPrice);
        response.put("finalPrice",finalPrice);
        response.put("saved",originalPrice-finalPrice);
        return ResponseEntity.ok(response);
    }
}