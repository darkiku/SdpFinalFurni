package com.example.Backend.observer;

public interface Observer {
    void update(String message);
    String getIdentifier();}