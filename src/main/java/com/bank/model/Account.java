package com.bank.model;

import com.bank.exception.InsufficientFundsException;
import com.bank.exception.InvalidTransactionException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public abstract class Account {
    
    private final String accountNumber;
    private final String customerId;
    protected double balance;
    private final LocalDateTime createdAt;
    private AccountStatus status;
    private final List<Transaction> transactions;
    
    protected Account(String customerId, double initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        
        this.accountNumber = generateAccountNumber();
        this.customerId = customerId;
        this.balance = initialBalance;
        this.createdAt = LocalDateTime.now();
        this.status = AccountStatus.ACTIVE;
        this.transactions = new ArrayList<>();
        
        if (initialBalance > 0) {
            addTransaction(new Transaction(
                TransactionType.DEPOSIT, 
                initialBalance, 
                "Initial deposit"
            ));
        }
    }
    
    private String generateAccountNumber() {
        return "ACC" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public AccountStatus getStatus() {
        return status;
    }
    
    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }
    
    public void deposit(double amount) {
        validateAmount(amount);
        
        if (status != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Account is not active");
        }
        
        balance += amount;
        addTransaction(new Transaction(TransactionType.DEPOSIT, amount, "Deposit"));
    }
    
    public void withdraw(double amount) throws InsufficientFundsException {
        validateAmount(amount);
        
        if (status != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Account is not active");
        }
        
        if (balance < amount) {
            throw new InsufficientFundsException(amount, balance);
        }
        
        if (!canWithdraw(amount)) {
            throw new InvalidTransactionException("Withdrawal not allowed per account rules");
        }
        
        balance -= amount;
        addTransaction(new Transaction(TransactionType.WITHDRAWAL, amount, "Withdrawal"));
    }
    
    public void transferTo(Account targetAccount, double amount) 
            throws InsufficientFundsException {
        
        if (targetAccount == null) {
            throw new IllegalArgumentException("Target account cannot be null");
        }
        
        if (this.equals(targetAccount)) {
            throw new IllegalArgumentException("Cannot transfer to same account");
        }
        
        this.withdraw(amount);
        
        try {
            targetAccount.deposit(amount);
            
            this.transactions.get(this.transactions.size() - 1)
                .setDescription("Transfer to " + targetAccount.getAccountNumber());
            
            targetAccount.transactions.get(targetAccount.transactions.size() - 1)
                .setDescription("Transfer from " + this.accountNumber);
            
        } catch (Exception e) {
            this.deposit(amount);
            throw new InvalidTransactionException("Transfer failed: " + e.getMessage());
        }
    }
    
    protected void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }
    
    private void validateAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }
    
    public void close() {
        if (balance > 0) {
            throw new IllegalStateException("Cannot close account with positive balance");
        }
        this.status = AccountStatus.CLOSED;
    }
    
    public abstract String getAccountType();
    public abstract boolean canWithdraw(double amount);
    public abstract void applyInterest();
    public abstract double getInterestRate();
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return accountNumber.equals(account.accountNumber);
    }
    
    @Override
    public int hashCode() {
        return accountNumber.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("%s[number=%s, balance=$%.2f, status=%s]",
            getAccountType(), accountNumber, balance, status);
    }
}
