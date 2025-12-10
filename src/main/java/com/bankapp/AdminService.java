package com.bankapp;

public class AdminService {
    private Bank bank;
    private boolean adminLoggedIn;
    private static final String ADMIN_USER = "ADMIN";

    public AdminService(Bank bank) {
        this.bank = bank;
        this.adminLoggedIn = false;
    }

    public boolean login(String password) {
        if (AuthService.authenticateAdmin(password)) {
            adminLoggedIn = true;
            AuditService.logAdminLogin(ADMIN_USER);
            System.out.println("\n✅ Admin login successful!");
            return true;
        } else {
            AuditService.logAdminLoginFailure("Invalid password");
            System.out.println("\n❌ Invalid admin password!");
            return false;
        }
    }

    public void logout() {
        if (adminLoggedIn) {
            AuditService.logAdminLogout(ADMIN_USER);
            adminLoggedIn = false;
            System.out.println("\n✅ Admin logged out successfully.");
        }
    }

    public void showAdminMenu() {
        if (!adminLoggedIn) {
            System.out.println("❌ Unauthorized access. Please login first.");
            return;
        }

        boolean running = true;
        while (running) {
            displayAdminMenu();
            int choice = InputUtil.readInt("Choose admin option: ");
            System.out.println();

            switch (choice) {
                case 1:
                    viewAllAccounts();
                    break;
                case 2:
                    searchAccount();
                    break;
                case 3:
                    viewTotalBankBalance();
                    break;
                case 4:
                    viewAccountTransactions();
                    break;
                case 5:
                    lockUnlockAccount();
                    break;
                case 6:
                    viewAuditLogs();
                    break;
                case 7:
                    running = false;
                    logout();
                    break;
                default:
                    System.out.println("❌ Invalid option. Please choose between 1-7.");
            }
        }
    }

    private void displayAdminMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("           🔐 ADMIN CONTROL PANEL 🔐");
        System.out.println("=".repeat(60));
        System.out.println("1. View All Accounts");
        System.out.println("2. Search Account by Number");
        System.out.println("3. View Total Bank Balance");
        System.out.println("4. View All Transactions of Any Account");
        System.out.println("5. Lock/Unlock Account");
        System.out.println("6. View Audit Logs");
        System.out.println("7. Exit Admin Panel");
        System.out.println("=".repeat(60));
    }

    private void viewAllAccounts() {
        System.out.println("\n--- All Bank Accounts ---");
        bank.displayAllAccountsAdmin();
    }

    private void searchAccount() {
        System.out.println("\n--- Search Account ---");
        String accountNumber = InputUtil.readString("Enter account number: ");
        bank.displayAccountDetailsAdmin(accountNumber);
    }

    private void viewTotalBankBalance() {
        System.out.println("\n--- Total Bank Balance ---");
        double totalBalance = bank.getTotalBankBalance();
        System.out.println("\n" + "=".repeat(50));
        System.out.println("Total Bank Balance: $" + String.format("%.2f", totalBalance));
        System.out.println("Total Accounts: " + bank.getTotalAccounts());
        System.out.println("=".repeat(50));
    }

    private void viewAccountTransactions() {
        System.out.println("\n--- View Account Transactions ---");
        String accountNumber = InputUtil.readString("Enter account number: ");
        bank.viewTransactions(accountNumber);
    }

    private void lockUnlockAccount() {
        System.out.println("\n--- Lock/Unlock Account ---");
        String accountNumber = InputUtil.readString("Enter account number: ");

        if (!bank.accountExists(accountNumber)) {
            System.out.println("❌ Account not found: " + accountNumber);
            return;
        }

        System.out.println("\n1. Lock Account");
        System.out.println("2. Unlock Account");
        int choice = InputUtil.readInt("Choose option: ");

        if (choice == 1) {
            if (bank.lockAccount(accountNumber)) {
                AuditService.logAccountLocked(accountNumber, ADMIN_USER);
                System.out.println("✅ Account locked successfully.");
            }
        } else if (choice == 2) {
            if (bank.unlockAccount(accountNumber)) {
                AuditService.logAccountUnlocked(accountNumber, ADMIN_USER);
                System.out.println("✅ Account unlocked successfully.");
            }
        } else {
            System.out.println("❌ Invalid option.");
        }
    }

    private void viewAuditLogs() {
        System.out.println("\n--- Audit Logs ---");
        int limit = InputUtil.readInt("How many recent logs to display? (default 50): ");
        if (limit <= 0) {
            limit = 50;
        }
        AuditService.displayAuditLogs(limit);
    }
}