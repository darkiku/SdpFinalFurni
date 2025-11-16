package com.example.Backend.observer;

public class EmailObserver implements Observer {
    private String email;

    public EmailObserver(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email address");
        }
        this.email = email;
    }

    @Override
    public void update(String message) {
        System.out.println("📧 Email to " + email + ": " + message);
    }

    @Override
    public String getIdentifier() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailObserver that = (EmailObserver) o;
        return email.equals(that.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }
}