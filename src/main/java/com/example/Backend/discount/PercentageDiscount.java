package com.example.Backend.discount;

public class PercentageDiscount implements DiscountStrategy {
    private double percent;
    public PercentageDiscount(double percent){
        this.percent=percent;
    }
    public double applyDiscount(double price){
        return price*(1-percent/100);
    }
}