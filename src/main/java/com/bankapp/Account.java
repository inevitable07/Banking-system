package com.bankapp;

import java.util.ArrayList;
import java.util.List;

public class Account {
    private String accountNumber;
    private String customerName;
    private double balance;
    private List<Transaction> transactions;
    private String passwordHash;
    private String pin;
    private boolean isLocked;

    public Account(String accountNumber, String customerName, String password, String pin) {
        this.accountNumber = accountNumber;
        this.customerName = customerName;
        this.passwordHash = AuthService.hashPassword(password);
        this.pin = pin;
        this.balance = 0.0;
        this.transactions = new ArrayList<>();
        this.isLocked = false;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public void lock() {
        this.isLocked = true;
    }

    public void unlock() {
        this.isLocked = false;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    public boolean deposit(double amount) {
        if (amount <= 0) {
            System.out.println("❌ Deposit amount must be greater than 0.");
            return false;
        }
        this.balance += amount;
        addTransaction(new Transaction("DEPOSIT", amount));
        AuditService.logDepositSuccess(this.accountNumber, amount);
        return true;
    }

    public boolean withdraw(double amount, String pin) {
        if (!AuthService.verifyPin(pin, this.pin)) {
            System.out.println("❌ Invalid PIN.");
            AuditService.logWrongPin(this.accountNumber);
            return false;
        }

        if (amount <= 0) {
            System.out.println("❌ Withdrawal amount must be greater than 0.");
            AuditService.logWithdrawFailure(this.accountNumber, "Amount <= 0");
            return false;
        }

        if (amount > this.balance) {
            System.out.println("❌ Insufficient balance. Current balance: $" +
                    String.format("%.2f", this.balance));
            AuditService.logWithdrawFailure(this.accountNumber, "Insufficient balance");
            return false;
        }

        this.balance -= amount;
        addTransaction(new Transaction("WITHDRAW", amount));
        AuditService.logWithdrawSuccess(this.accountNumber, amount);
        return true;
    }

    public void displayTransactions() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions found for this account.");
            return;
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("Transaction History - Account: " + accountNumber);
        System.out.println("Customer: " + customerName);
        System.out.println("=".repeat(60));
        System.out.println(String.format("%-10s | %-10s | %s",
                "Type", "Amount", "Date & Time"));
        System.out.println("-".repeat(60));

        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
        System.out.println("=".repeat(60));
    }

    @Override
    public String toString() {
        return String.format("Account[%s] - %s - Balance: $%.2f - Transactions: %d",
                accountNumber, customerName, balance, transactions.size());
    }


}