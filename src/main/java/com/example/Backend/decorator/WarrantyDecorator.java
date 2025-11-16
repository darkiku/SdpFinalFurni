package com.example.Backend.decorator;

import com.example.Backend.Product.Product;

public class WarrantyDecorator extends ProductWrapper {
    private int years;

    public WarrantyDecorator(Product product, int years) {
        super(product);
        this.years = years;
        this.extraCost = 20.0 * years;
        this.extraDescription = years + " Year Extended Warranty";
    }
}