package com.bank.exception;

public class InsufficientFundsException extends Exception {
    
    private final double attempted;
    private final double available;
    
    public InsufficientFundsException(double attempted, double available) {
        super(String.format("Insufficient funds: Attempted $%.2f, Available $%.2f",
            attempted, available));
        this.attempted = attempted;
        this.available = available;
    }
    
    public double getAttempted() { return attempted; }
    public double getAvailable() { return available; }
}