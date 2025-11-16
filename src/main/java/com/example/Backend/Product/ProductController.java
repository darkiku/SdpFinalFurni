package com.example.Backend.Product;

import com.example.Backend.observer.EmailObserver;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/product")
@CrossOrigin(origins="http://localhost:3000",allowCredentials="true")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id){
        Product product=productService.getProductById(id);
        return ResponseEntity.ok(product);
    }
    @GetMapping("/allProducts")
    public ResponseEntity<List<Product>> getAllProducts(){
        List<Product> products=productService.findAll();
        if(products!=null){
            return ResponseEntity.ok(products);
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping("/addProduct")
    public ResponseEntity<Product> addProduct(@Valid @RequestBody Product product){
        productService.saveProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }
    @PostMapping("/createCustom")
    public ResponseEntity<?> createCustomProduct(@RequestBody Map<String,Object> request){
        try{
            System.out.println("Received request: "+request);
            String name=(String)request.get("name");
            String category=(String)request.get("category");
            Double price=Double.parseDouble(request.get("price").toString());
            String color=(String)request.getOrDefault("color","Blue");
            String size=(String)request.getOrDefault("size","Medium");
            String material=(String)request.getOrDefault("material","Wood");
            String imageUrl=(String)request.getOrDefault("imageUrl","");
            System.out.println("Creating product: "+name+" "+category+" $"+price);
            Product product=productService.createCustomProduct(name,category,price,color,size,material,imageUrl);
            System.out.println("Product created with ID: "+product.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(product);
        }catch(Exception e){
            System.err.println("Error creating product: "+e.getMessage());
            e.printStackTrace();
            Map<String,String> error=new HashMap<>();
            error.put("error",e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/updateProduct")
    public ResponseEntity<Product> updateProduct(@Valid @RequestBody Product product){
        productService.updateProduct(product);
        return ResponseEntity.ok(product);
    }
    @PutMapping("/updatePrice/{id}")
    public ResponseEntity<Map<String,String>> updatePrice(@PathVariable long id,@RequestParam Double newPrice){
        productService.updateProductPrice(id,newPrice);
        Map<String,String> response=new HashMap<>();
        response.put("message","Price updated and observers notified");
        return ResponseEntity.ok(response);
    }
    @PostMapping("/subscribe")
    public ResponseEntity<Map<String,String>> subscribe(@RequestParam String email){
        productService.subscribeToProduct(email);
        Map<String,String> response=new HashMap<>();
        response.put("message","Subscribed to product updates");
        return ResponseEntity.ok(response);
    }
}