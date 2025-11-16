package com.example.Backend.Cart;

import com.example.Backend.Product.Product;
import com.example.Backend.Product.ProductRepository;
import com.example.Backend.discount.DiscountStrategy;
import com.example.Backend.discount.TieredDiscount;
import com.example.Backend.decorator.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;

    public Cart getCartById(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    public List<Product> getProductsFromCart(Cart cart) {
        return cart.getProducts();
    }

    public Cart addToCart(Long cartId, Long productId) {
        Cart cart = getCartById(cartId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        List<Product> products = cart.getProducts();
        if (products == null) {
            products = new ArrayList<>();
        }

        if (!products.contains(product)) {
            products.add(product);
        }

        if (!product.getCarts().contains(cart)) {
            product.getCarts().add(cart);
        }

        cart.setQuantity(products.size());
        cart.setTotalPrice(calculateCartTotal(products));

        cartRepository.save(cart);
        productRepository.save(product);
        return cart;
    }

    public void removeFromCart(Long id, Long productId) {
        Cart cart = getCartById(id);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        List<Product> products = cart.getProducts();
        products.remove(product);
        product.getCarts().remove(cart);

        cart.setQuantity(products.size());
        cart.setTotalPrice(calculateCartTotal(products));

        cartRepository.save(cart);
        productRepository.save(product);
    }

    public void clearCart(Long id) {
        Cart cart = getCartById(id);
        List<Product> products = cart.getProducts();

        for (Product product : products) {
            product.getCarts().remove(cart);
            productRepository.save(product);
        }

        products.clear();
        cart.setQuantity(0);
        cart.setTotalPrice(0.0);
        cartRepository.save(cart);
    }

    // Strategy Pattern - применение скидки
    public Double applyDiscount(Long cartId, DiscountStrategy strategy) {
        Cart cart = getCartById(cartId);
        Double originalPrice = cart.getTotalPrice();
        Double discountedPrice = strategy.applyDiscount(originalPrice);
        cart.setTotalPrice(discountedPrice);
        cartRepository.save(cart);
        return discountedPrice;
    }

    // Strategy Pattern - автоматическая скидка по уровням
    public Map<String, Object> calculateWithAutoDiscount(Long cartId) {
        Cart cart = getCartById(cartId);
        Double originalPrice = cart.getTotalPrice();

        TieredDiscount tieredDiscount = new TieredDiscount(originalPrice);
        Double finalPrice = tieredDiscount.applyDiscount(originalPrice);

        Map<String, Object> result = new HashMap<>();
        result.put("originalPrice", originalPrice);
        result.put("discount", originalPrice - finalPrice);
        result.put("finalPrice", finalPrice);
        result.put("message", tieredDiscount.getDescription());

        return result;
    }

    // Decorator Pattern - расчет цены с дополнительными услугами
    public Double calculateDecoratedPrice(Product product, List<String> extras) {
        ProductWrapper wrapped = new ProductWrapper(product) {
            @Override
            public double getTotalPrice() {
                return product.getPrice();
            }

            @Override
            public String getFullDescription() {
                return product.getDescription();
            }
        };

        for (String extra : extras) {
            switch (extra.toLowerCase()) {
                case "giftwrap":
                    wrapped = new GiftWrapDecorator(wrapped.getProduct());
                    break;
                case "express":
                    wrapped = new ExpressDeliveryDecorator(wrapped.getProduct());
                    break;
                case "warranty":
                    wrapped = new WarrantyDecorator(wrapped.getProduct(), 2);
                    break;
            }
        }

        return wrapped.getTotalPrice();
    }

    private Double calculateCartTotal(List<Product> products) {
        return products.stream()
                .mapToDouble(Product::getPrice)
                .sum();
    }
}