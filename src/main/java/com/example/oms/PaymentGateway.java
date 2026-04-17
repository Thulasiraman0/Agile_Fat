package com.example.oms;

@FunctionalInterface
public interface PaymentGateway {
    boolean charge(double amount);
}