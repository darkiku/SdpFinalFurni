package com.example.Backend.Product;

import com.example.Backend.observer.EmailObserver;
import com.example.Backend.observer.ProductSubject;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private ProductSubject productSubject=new ProductSubject();

    @Autowired
    public ProductService(ProductRepository productRepository){
        this.productRepository=productRepository;
    }
    public Product getProductById(long id){
        return productRepository.findById(id);
    }
    public List<Product> findAll(){
        return productRepository.findAll();
    }
    public void saveProduct(Product product){
        productRepository.save(product);
    }
    public Product createCustomProduct(String name,String category,Double price,String color,String size,String material,String imageUrl){
        Product product=new ProductBuilder()
                .setName(name)
                .setCategory(category)
                .setPrice(price)
                .setColor(color)
                .setSize(size)
                .setMaterial(material)
                .setImageUrl(imageUrl)
                .setDescription("Custom "+size+" "+color+" "+material+" "+category)
                .build();
        productRepository.save(product);
        return product;
    }
    public void deleteProduct(long id){
        productRepository.deleteById(id);
    }
    public void updateProduct(Product product){
        Product updateProduct=productRepository.findById(product.getId());
        if(updateProduct!=null){
            updateProduct.setName(product.getName());
            updateProduct.setPrice(product.getPrice());
            updateProduct.setDescription(product.getDescription());
            updateProduct.setImageUrl(product.getImageUrl());
            updateProduct.setCategory(product.getCategory());
            updateProduct.setColor(product.getColor());
            updateProduct.setSize(product.getSize());
            updateProduct.setMaterial(product.getMaterial());
            productRepository.save(updateProduct);
        }
    }
    public void updateProductPrice(long id,Double newPrice){
        Product product=productRepository.findById(id);
        if(product!=null){
            Double oldPrice=product.getPrice();
            product.setPrice(newPrice);
            productRepository.save(product);
            productSubject.notifyObservers("Product "+product.getName()+" price changed from $"+oldPrice+" to $"+newPrice);
        }
    }
    public void subscribeToProduct(String email){
        productSubject.attach(new EmailObserver(email));
    }
}