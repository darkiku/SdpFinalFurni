package com.example.Backend.Cart;

import com.example.Backend.Product.Product;
import com.example.Backend.discount.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/cart")
@Transactional
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/showCart/{id}")
    public ResponseEntity<List<Product>> showCart(@PathVariable Long id) {
        Cart cart = cartService.getCartById(id);
        List<Product> products = cartService.getProductsFromCart(cart);
        return ResponseEntity.ok(products);
    }

    @PostMapping("/addToCart/{cartId}/{productId}")
    public ResponseEntity<Cart> addToCart(@PathVariable Long cartId, @PathVariable Long productId) {
        Cart cart = cartService.addToCart(cartId, productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(cart);
    }

    @DeleteMapping("/removeFromCart/{id}/{productId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long id, @PathVariable Long productId) {
        cartService.removeFromCart(id, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clearCart/{id}")
    public ResponseEntity<Void> clearCart(@PathVariable Long id) {
        cartService.clearCart(id);
        return ResponseEntity.noContent().build();
    }

    // ИСПРАВЛЕНО: Используем Strategy Pattern через TieredDiscount
    @GetMapping("/calculate/{cartId}")
    public ResponseEntity<Map<String, Object>> calculatePrice(@PathVariable Long cartId) {
        Map<String, Object> response = cartService.calculateWithAutoDiscount(cartId);
        return ResponseEntity.ok(response);
    }

    // Strategy Pattern - ручное применение скидки
    @PostMapping("/applyDiscount/{cartId}")
    public ResponseEntity<Map<String, Object>> applyDiscount(
            @PathVariable Long cartId,
            @RequestParam String type,
            @RequestParam double value) {

        Cart cart = cartService.getCartById(cartId);
        Double originalPrice = cart.getTotalPrice();

        DiscountStrategy discount;
        if (type.equals("percentage")) {
            discount = new PercentageDiscount(value);
        } else if (type.equals("fixed")) {
            discount = new FixedDiscount(value);
        } else {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Invalid discount type. Use 'percentage' or 'fixed'")
            );
        }

        Double finalPrice = cartService.applyDiscount(cartId, discount);

        Map<String, Object> response = new HashMap<>();
        response.put("originalPrice", originalPrice);
        response.put("finalPrice", finalPrice);
        response.put("saved", originalPrice - finalPrice);
        response.put("description", discount.getDescription());

        return ResponseEntity.ok(response);
    }

    // Decorator Pattern - расчет цены с дополнительными услугами
    @PostMapping("/calculateWithExtras/{cartId}/{productId}")
    public ResponseEntity<Map<String, Object>> calculateWithExtras(
            @PathVariable Long cartId,
            @PathVariable Long productId,
            @RequestBody List<String> extras) {

        Cart cart = cartService.getCartById(cartId);
        Product product = cart.getProducts().stream()
                .filter(p -> p.getId() == productId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not in cart"));

        Double basePrice = product.getPrice();
        Double decoratedPrice = cartService.calculateDecoratedPrice(product, extras);

        Map<String, Object> response = new HashMap<>();
        response.put("productName", product.getName());
        response.put("basePrice", basePrice);
        response.put("decoratedPrice", decoratedPrice);
        response.put("extras", extras);
        response.put("extraCost", decoratedPrice - basePrice);

        return ResponseEntity.ok(response);
    }
}