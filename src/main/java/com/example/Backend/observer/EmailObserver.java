package com.example.Backend.observer;

public class EmailObserver implements Observer {
    private String email;
    public EmailObserver(String email){
        this.email=email;
    }
    public void update(String message){
        System.out.println("Email to "+email+": "+message);
    }
}