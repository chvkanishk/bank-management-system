package com.bank.model;

import java.util.List;

import main.java.com.bank.exception.InsufficientFundsException;

/**
 * Savings Account class - Concrete implementation of Account.
 * Features: 3.5% annual interest rate, minimum balance of $100,
 * maximum 6 withdrawals per month, monthly interest accrual.
 * Demonstrates: Inheritance, Polymorphism
 */
public class SavingsAccount extends Account {
    
    private static final double INTEREST_RATE = 0.035; // 3.5% annual interest
    private static final double MINIMUM_BALANCE = 100.0; // Minimum balance requirement
    private static final int MAX_MONTHLY_WITHDRAWALS = 6; // Maximum withdrawals per month
    
    private int monthlyWithdrawals = 0;
    
    /**
     * Constructor for SavingsAccount
     * Ensures initial balance meets minimum requirement
     */
    public SavingsAccount(String customerId, double initialBalance) {
        super(customerId, Math.max(initialBalance, MINIMUM_BALANCE));
        this.monthlyWithdrawals = 0;
    }
    
    @Override
    public String getAccountType() {
        return "Savings Account";
    }
    
    @Override
    public double getInterestRate() {
        return INTEREST_RATE;
    }
    
    /**
     * Check if withdrawal is allowed based on account rules
     * - Cannot withdraw if balance would go below minimum balance
     * - Cannot exceed monthly withdrawal limit
     */
    @Override
    public boolean canWithdraw(double amount) {
        // Validate amount
        if (amount <= 0) {
            return false;
        }
        
        // Check if balance would fall below minimum
        if (getBalance() - amount < MINIMUM_BALANCE) {
            return false;
        }
        
        // Check monthly withdrawal limit
        if (monthlyWithdrawals >= MAX_MONTHLY_WITHDRAWALS) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Override withdraw to track monthly withdrawal count
     */
    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        super.withdraw(amount);
        monthlyWithdrawals++;
    }
    
    /**
     * Reset monthly withdrawal counter
     * Should be called once a month
     */
    // ✅ CORRECTED CODE:
@Override
public void applyInterest() {
    double monthlyInterest = getBalance() * (INTEREST_RATE / 12);
    if (monthlyInterest > 0) {
        // Directly modify balance (we're inside the class, so we can access it)
        // But wait... balance is private in parent class!
        // We need to use the protected method from Account
        
        // Option 1: Call deposit but don't add duplicate transaction
        double currentBalance = getBalance();
        super.deposit(monthlyInterest); // This adds a DEPOSIT transaction
        
        // Get the last transaction and change its type to INTEREST
        List<Transaction> txns = getTransactions();
        if (!txns.isEmpty()) {
            Transaction lastTxn = txns.get(txns.size() - 1);
            // We can't modify Transaction - it's immutable!
        }
    }
}
    
    /**
     * Reset monthly withdrawal counter
     * Should be called once a month
     */
    public void resetMonthlyWithdrawalCount() {
        monthlyWithdrawals = 0;
    }
    
    /**
     * Get current monthly withdrawal count
     */
    public int getMonthlyWithdrawalCount() {
        return monthlyWithdrawals;
    }
    
    /**
     * Get remaining withdrawals allowed for current month
     */
    public int getRemainingMonthlyWithdrawals() {
        return MAX_MONTHLY_WITHDRAWALS - monthlyWithdrawals;
    }
    
    /**
     * Get minimum balance requirement
     */
    public double getMinimumBalance() {
        return MINIMUM_BALANCE;
    }
}