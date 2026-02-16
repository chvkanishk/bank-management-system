package com.bank.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Immutable transaction record using Java Record (Java 14+)
 * Or use regular class if you prefer
 */
public class Transaction {
    
    private final String transactionId;
    private final TransactionType type;
    private final double amount;
    private String description;
    private final LocalDateTime timestamp;
    
    public Transaction(TransactionType type, double amount, String description) {
        this.transactionId = "TXN" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters
    public String getTransactionId() { return transactionId; }
    public TransactionType getType() { return type; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
    public LocalDateTime getTimestamp() { return timestamp; }
    
    // Allow description update for transfers
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s: $%.2f - %s",
            timestamp.toString(), type, amount, description);
    }
}