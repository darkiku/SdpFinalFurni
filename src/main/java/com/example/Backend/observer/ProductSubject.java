package com.example.Backend.observer;

import java.util.ArrayList;
import java.util.List;

public class ProductSubject {
    private List<Observer> observers=new ArrayList<>();
    public void attach(Observer observer){
        observers.add(observer);
    }
    public void detach(Observer observer){
        observers.remove(observer);
    }
    public void notifyObservers(String message){
        for(Observer observer:observers){
            observer.update(message);
        }
    }
}