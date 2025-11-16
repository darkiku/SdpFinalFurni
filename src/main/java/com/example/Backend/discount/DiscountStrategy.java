package com.example.Backend.discount;

public interface DiscountStrategy {
    double applyDiscount(double price);
    String getDescription();
}