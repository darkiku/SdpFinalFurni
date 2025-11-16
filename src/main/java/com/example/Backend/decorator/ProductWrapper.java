package com.example.Backend.decorator;

import com.example.Backend.Product.Product;

public abstract class ProductWrapper {
    protected Product product;
    protected double extraCost;
    protected String extraDescription;
    public ProductWrapper(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        this.product = product;
    }
    public double getTotalPrice() {
        return product.getPrice() + extraCost;
    }
    public String getFullDescription() {
        return product.getDescription() + " + " + extraDescription;
    }
    public Product getProduct() {
        return product;
    }
    public double getExtraCost() {
        return extraCost;
    }
    public String getExtraDescription() {return extraDescription;}
}
