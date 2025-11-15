package com.example.Backend.visitor;

public interface Visitable {
    String accept(OrderVisitor visitor);
}