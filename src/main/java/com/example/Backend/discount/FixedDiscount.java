package com.example.Backend.discount;

public class FixedDiscount implements DiscountStrategy {
    private double amount;

    public FixedDiscount(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Discount amount cannot be negative");
        }
        this.amount = amount;}

    @Override
    public double applyDiscount(double price) {return Math.max(0, price - amount);}
    @Override
    public String getDescription() {return "$" + amount + " discount applied!";}
}