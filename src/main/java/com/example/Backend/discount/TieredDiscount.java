package com.example.Backend.discount;

public class TieredDiscount implements DiscountStrategy {
    private double totalPrice;

    public TieredDiscount(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public double applyDiscount(double price) {
        if (totalPrice > 500) {
            return price * 0.85;
        } else if (totalPrice > 200) {
            return price * 0.90;
        } else if (totalPrice > 100) {
            return price * 0.95;
        }
        return price;
    }

    @Override
    public String getDescription() {
        if (totalPrice > 500) {
            return "15% VIP discount applied!";
        } else if (totalPrice > 200) {
            return "10% bulk discount applied!";
        } else if (totalPrice > 100) {
            return "5% starter discount applied!";
        }
        return "No discount applied";
    }
}