package com.example.Backend.Product;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/product")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/allProducts")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.findAll();
        if (products != null) {
            return ResponseEntity.ok(products);
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping("/addProduct")
    public ResponseEntity<Product> addProduct(@Valid @RequestBody Product product) {
        productService.saveProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }
    @PostMapping("/createCustom")
    public ResponseEntity<?> createCustomProduct(@RequestBody Map<String, Object> request) {
        try {
            String name = (String) request.get("name");
            String category = (String) request.get("category");
            Double price = Double.parseDouble(request.get("price").toString());
            String color = (String) request.getOrDefault("color", "Blue");
            String size = (String) request.getOrDefault("size", "Medium");
            String material = (String) request.getOrDefault("material", "Wood");
            String imageUrl = (String) request.getOrDefault("imageUrl", "");

            Product product = productService.createCustomProduct(
                    name, category, price, color, size, material, imageUrl
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(product);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to create product: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/updateProduct")
    public ResponseEntity<Product> updateProduct(@Valid @RequestBody Product product) {
        productService.updateProduct(product);
        return ResponseEntity.ok(product);
    }
    @PutMapping("/updatePrice/{id}")
    public ResponseEntity<Map<String, String>> updatePrice(
            @PathVariable long id,
            @RequestParam Double newPrice) {

        productService.updateProductPrice(id, newPrice);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Price updated and " +
                productService.getSubscriberCount() + " observers notified");

        return ResponseEntity.ok(response);
    }
    @PostMapping("/subscribe")
    public ResponseEntity<Map<String, String>> subscribe(@RequestParam String email) {
        try {
            productService.subscribeToProduct(email);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Subscribed to product updates");
            response.put("email", email);
            response.put("totalSubscribers", String.valueOf(productService.getSubscriberCount()));

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    @PostMapping("/unsubscribe")
    public ResponseEntity<Map<String, String>> unsubscribe(@RequestParam String email) {
        productService.unsubscribeFromProduct(email);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Unsubscribed from product updates");
        response.put("email", email);
        return ResponseEntity.ok(response);
    }
}
