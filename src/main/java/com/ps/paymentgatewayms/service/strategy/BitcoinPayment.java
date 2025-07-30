package com.ps.paymentgatewayms.service.strategy;

public class BitcoinPayment implements PaymentStrategy {
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing Bitcoin payment of $" + amount);
    }
}
