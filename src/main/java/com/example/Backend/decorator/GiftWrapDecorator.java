package com.example.Backend.decorator;

import com.example.Backend.Product.Product;

public class GiftWrapDecorator extends ProductWrapper {
    public GiftWrapDecorator(Product product){
        super(product);
        this.extraCost=5.0;
        this.extraDescription="Gift Wrap";
    }
}