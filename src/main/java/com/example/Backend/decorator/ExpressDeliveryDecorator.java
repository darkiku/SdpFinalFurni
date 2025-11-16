package com.example.Backend.decorator;

import com.example.Backend.Product.Product;

public class ExpressDeliveryDecorator extends ProductWrapper {
    public ExpressDeliveryDecorator(Product product) {
        super(product);
        this.extraCost = 15.0;
        this.extraDescription = "Express Delivery";
    }
}