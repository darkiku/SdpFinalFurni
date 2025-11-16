package com.example.Backend.observer;

import java.util.ArrayList;
import java.util.List;

public class ProductSubject {
    private List<Observer> observers = new ArrayList<>();

    public void attach(Observer observer) {
        if (observer == null) {
            throw new IllegalArgumentException("Observer cannot be null");
        }
        if (!observers.contains(observer)) {
            observers.add(observer);
            System.out.println("✅ Observer attached: " + observer.getIdentifier());
        }
    }
    public void detach(Observer observer) {
        if (observers.remove(observer)) {
            System.out.println("❌ Observer detached: " + observer.getIdentifier());
        }
    }
    public void detachByEmail(String email) {
        observers.removeIf(obs -> obs.getIdentifier().equals(email));
    }
    public void notifyObservers(String message) {
        System.out.println("📢 Notifying " + observers.size() + " observers...");
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
    public int getObserverCount() {
        return observers.size();
    }
}