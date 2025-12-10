package com.bankapp;

public class AccountMigrationHelper {

    /**
     * Checks if an account needs migration (missing password or PIN)
     */
    public static boolean needsMigration(Account account) {
        return account.getPasswordHash() == null ||
                account.getPin() == null ||
                account.getPasswordHash().isEmpty() ||
                account.getPin().isEmpty();
    }

    /**
     * Migrates an old account by setting up password and PIN
     */
    public static boolean migrateAccount(Account account) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("⚠️  ACCOUNT MIGRATION REQUIRED");
        System.out.println("=".repeat(60));
        System.out.println("Your account needs to be upgraded with security features.");
        System.out.println("Account: " + account.getAccountNumber());
        System.out.println("Customer: " + account.getCustomerName());
        System.out.println("\nPlease set up your security credentials:");
        System.out.println("=".repeat(60));

        // Verification - ask for account number to confirm identity
        String verification = InputUtil.readString("Enter your account number to confirm: ");
        if (!verification.equals(account.getAccountNumber())) {
            System.out.println("❌ Account number mismatch. Migration cancelled.");
            return false;
        }

        // Set password
        String password = InputUtil.readString("Create a new password: ");
        String confirmPassword = InputUtil.readString("Confirm password: ");

        if (!password.equals(confirmPassword)) {
            System.out.println("❌ Passwords don't match. Migration cancelled.");
            return false;
        }

        // Set PIN
        String pin = InputUtil.readString("Create a 4-digit PIN: ");

        if (!AuthService.isValidPin(pin)) {
            System.out.println("❌ PIN must be exactly 4 digits. Migration cancelled.");
            return false;
        }

        String confirmPin = InputUtil.readString("Confirm PIN: ");
        if (!pin.equals(confirmPin)) {
            System.out.println("❌ PINs don't match. Migration cancelled.");
            return false;
        }

        // Update account
        account.setPasswordHash(AuthService.hashPassword(password));
        account.setPin(pin);
        account.setLocked(false); // Ensure account is not locked

        System.out.println("\n✅ Account migration successful!");
        System.out.println("Your account has been upgraded with password and PIN protection.");
        System.out.println("=".repeat(60));

        return true;
    }

    /**
     * Interactive migration wizard for existing accounts
     */
    public static void runMigrationWizard(Bank bank) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🔧 ACCOUNT MIGRATION WIZARD");
        System.out.println("=".repeat(60));
        System.out.println("This wizard will help you upgrade old accounts.");
        System.out.println();

        String accountNumber = InputUtil.readString("Enter account number to migrate: ");

        if (!bank.accountExists(accountNumber)) {
            System.out.println("❌ Account not found: " + accountNumber);
            return;
        }

        Account account = bank.getAccount(accountNumber);

        if (!needsMigration(account)) {
            System.out.println("✅ Account already has password and PIN set up.");
            return;
        }

        if (migrateAccount(account)) {
            bank.saveToFile();
            System.out.println("✅ Account migration saved successfully!");
            AuditService.logDepositSuccess(accountNumber, 0); // Log migration
        }
    }
}