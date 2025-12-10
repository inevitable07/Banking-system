package com.bankapp;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Bank {
    private Map<String, Account> accounts;

    public Bank() {
        this.accounts = new HashMap<>();
    }

    public void loadFromFile() {
        this.accounts = FileStorage.loadFromFile();
    }

    public void saveToFile() {
        FileStorage.saveToFile(this.accounts);
    }

    public String createAccount(String customerName, String accountNumber, String password, String pin) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            accountNumber = generateAccountNumber();
        } else {
            accountNumber = accountNumber.trim();
            if (accounts.containsKey(accountNumber)) {
                System.out.println("❌ Account number already exists. Please choose a different number.");
                return null;
            }
        }

        if (!AuthService.isValidPin(pin)) {
            System.out.println("❌ PIN must be exactly 4 digits.");
            return null;
        }

        Account account = new Account(accountNumber, customerName, password, pin);
        accounts.put(accountNumber, account);

        System.out.println("\n✅ Account created successfully!");
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Customer Name: " + customerName);
        System.out.println("Initial Balance: $0.00");
        System.out.println("⚠️  Keep your password and PIN safe!");

        return accountNumber;
    }

    public boolean deposit(String accountNumber, double amount) {
        Account account = accounts.get(accountNumber);

        if (account == null) {
            System.out.println("❌ Account not found: " + accountNumber);
            return false;
        }

        if (account.deposit(amount)) {
            System.out.println("✅ Deposit successful!");
            System.out.println("Amount deposited: $" + String.format("%.2f", amount));
            System.out.println("New balance: $" + String.format("%.2f", account.getBalance()));
            return true;
        }

        return false;
    }

    public boolean withdraw(String accountNumber, double amount, String pin) {
        Account account = accounts.get(accountNumber);

        if (account == null) {
            System.out.println("❌ Account not found: " + accountNumber);
            return false;
        }

        if (account.withdraw(amount, pin)) {
            System.out.println("✅ Withdrawal successful!");
            System.out.println("Amount withdrawn: $" + String.format("%.2f", amount));
            System.out.println("New balance: $" + String.format("%.2f", account.getBalance()));
            return true;
        }

        return false;
    }

    public void checkBalance(String accountNumber) {
        Account account = accounts.get(accountNumber);

        if (account == null) {
            System.out.println("❌ Account not found: " + accountNumber);
            return;
        }

        System.out.println("\n" + "=".repeat(50));
        System.out.println("Account Number: " + account.getAccountNumber());
        System.out.println("Customer Name: " + account.getCustomerName());
        System.out.println("Current Balance: $" + String.format("%.2f", account.getBalance()));
        System.out.println("Total Transactions: " + account.getTransactions().size());
        System.out.println("=".repeat(50));
    }

    public void viewTransactions(String accountNumber) {
        Account account = accounts.get(accountNumber);

        if (account == null) {
            System.out.println("❌ Account not found: " + accountNumber);
            return;
        }

        account.displayTransactions();
    }

    public boolean accountExists(String accountNumber) {
        return accounts.containsKey(accountNumber);
    }

    public int getTotalAccounts() {
        return accounts.size();
    }

    private String generateAccountNumber() {
        String accountNumber;
        do {
            accountNumber = String.format("%010d",
                    (int)(Math.random() * 10000000000L));
        } while (accounts.containsKey(accountNumber));

        return accountNumber;
    }

    public void displayAllAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts in the system.");
            return;
        }

        System.out.println("\n" + "=".repeat(70));
        System.out.println("All Accounts in the System");
        System.out.println("=".repeat(70));

        for (Account account : accounts.values()) {
            System.out.println(account);
        }

        System.out.println("=".repeat(70));
        System.out.println("Total Accounts: " + accounts.size());
    }

    public Account authenticateUser(String accountNumber, String password) {
        Account account = accounts.get(accountNumber);

        if (account == null) {
            AuditService.logLoginFailure(accountNumber, "Account not found");
            return null;
        }

        if (account.isLocked()) {
            System.out.println("❌ Account is locked. Please contact admin.");
            AuditService.logLoginFailure(accountNumber, "Account locked");
            return null;
        }

        if (!AuthService.verifyPassword(password, account.getPasswordHash())) {
            AuditService.logWrongPassword(accountNumber);
            return null;
        }

        AuditService.logLoginSuccess(accountNumber);
        return account;
    }

    public boolean lockAccount(String accountNumber) {
        Account account = accounts.get(accountNumber);
        if (account != null) {
            account.lock();
            return true;
        }
        return false;
    }

    public boolean unlockAccount(String accountNumber) {
        Account account = accounts.get(accountNumber);
        if (account != null) {
            account.unlock();
            return true;
        }
        return false;
    }

    public double getTotalBankBalance() {
        double total = 0.0;
        for (Account account : accounts.values()) {
            total += account.getBalance();
        }
        return total;
    }

    public void displayAllAccountsAdmin() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts in the system.");
            return;
        }

        System.out.println("\n" + "=".repeat(90));
        System.out.println("All Accounts in the System");
        System.out.println("=".repeat(90));
        System.out.println(String.format("%-15s | %-20s | %-12s | %-10s | %s",
                "Account No", "Customer Name", "Balance", "Status", "Transactions"));
        System.out.println("-".repeat(90));

        for (Account account : accounts.values()) {
            String status = account.isLocked() ? "🔒 LOCKED" : "✅ ACTIVE";
            System.out.println(String.format("%-15s | %-20s | $%-11.2f | %-10s | %d",
                    account.getAccountNumber(),
                    account.getCustomerName(),
                    account.getBalance(),
                    status,
                    account.getTransactions().size()));
        }

        System.out.println("=".repeat(90));
        System.out.println("Total Accounts: " + accounts.size());
    }

    public void displayAccountDetailsAdmin(String accountNumber) {
        Account account = accounts.get(accountNumber);

        if (account == null) {
            System.out.println("❌ Account not found: " + accountNumber);
            return;
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("Account Details");
        System.out.println("=".repeat(60));
        System.out.println("Account Number: " + account.getAccountNumber());
        System.out.println("Customer Name: " + account.getCustomerName());
        System.out.println("Balance: $" + String.format("%.2f", account.getBalance()));
        System.out.println("Status: " + (account.isLocked() ? "🔒 LOCKED" : "✅ ACTIVE"));
        System.out.println("Total Transactions: " + account.getTransactions().size());
        System.out.println("=".repeat(60));
    }

    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    /**
     * Authenticate user with migration support
     */
    public Account authenticateUserWithMigration(String accountNumber) {
        Account account = accounts.get(accountNumber);

        if (account == null) {
            System.out.println("❌ Account not found: " + accountNumber);
            return null;
        }

        // Check if account needs migration
        if (AccountMigrationHelper.needsMigration(account)) {
            System.out.println("\n⚠️  This account needs to be upgraded.");
            System.out.println("Please complete the migration process.");

            if (AccountMigrationHelper.migrateAccount(account)) {
                this.saveToFile();
                System.out.println("\n✅ You can now login with your new credentials.");
                return null; // Return null to force re-login with new credentials
            } else {
                System.out.println("❌ Migration failed. Please try again.");
                return null;
            }
        }

        // Normal authentication with password
        String password = InputUtil.readString("Enter password: ");

        if (account.isLocked()) {
            System.out.println("❌ Account is locked. Please contact admin.");
            AuditService.logLoginFailure(accountNumber, "Account locked");
            return null;
        }

        if (!AuthService.verifyPassword(password, account.getPasswordHash())) {
            AuditService.logWrongPassword(accountNumber);
            System.out.println("❌ Invalid password.");
            return null;
        }

        AuditService.logLoginSuccess(accountNumber);
        return account;
    }
}