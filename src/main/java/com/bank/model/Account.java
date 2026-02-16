package com.bank.model;

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
    
    private int monthlyWithdrawals;
    
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
        return "SAVINGS";
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
        if (amount <= 0) {
            return false;
        }
        
        if (balance - amount < MINIMUM_BALANCE) {
            return false;
        }
        
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
     * Apply monthly interest to account balance
     * Interest = balance * (annual rate / 12)
     */
    @Override
    public void applyInterest() {
        double monthlyInterest = balance * (INTEREST_RATE / 12);
        if (monthlyInterest > 0) {
            balance += monthlyInterest;
            addTransaction(new Transaction(
                TransactionType.INTEREST,
                monthlyInterest,
                "Monthly interest accrual"
            ));
        }
    }
    
    /**
     * Reset monthly withdrawal counter
     * Should be called at the start of each month
     */
    public void resetMonthlyWithdrawals() {
        this.monthlyWithdrawals = 0;
    }
    
    /**
     * Get current monthly withdrawal count
     */
    public int getMonthlyWithdrawals() {
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