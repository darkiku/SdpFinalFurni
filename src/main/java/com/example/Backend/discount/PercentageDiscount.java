package com.example.Backend.discount;

public class PercentageDiscount implements DiscountStrategy {
    private double percent;

    public PercentageDiscount(double percent) {
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }
        this.percent = percent;
    }

    @Override
    public double applyDiscount(double price) {
        return price * (1 - percent / 100);
    }

    @Override
    public String getDescription() {
        return percent + "% discount applied!";
    }
}