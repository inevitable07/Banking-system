package com.bankapp;

public class Main {
    private static Bank bank;
    private static Account loggedInAccount;
    private static boolean running = true;

    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("    🏦 Welcome to Simple Banking System 🏦");
        System.out.println("=".repeat(60));

        bank = new Bank();
        bank.loadFromFile();

        showInitialMenu();
    }

    private static void showInitialMenu() {
        while (running) {
            displayInitialMenu();
            int choice = InputUtil.readInt("Choose option: ");
            System.out.println();

            switch (choice) {
                case 1:
                    customerLogin();
                    break;
                case 2:
                    createNewAccount();
                    break;
                case 3:
                    AccountMigrationHelper.runMigrationWizard(bank);  // NEW
                    break;
                case 4:
                    adminLogin();
                    break;
                case 5:
                    exitSystem();
                    break;
                default:
                    System.out.println("❌ Invalid option. Please choose between 1-5.");
            }
        }
    }

    private static void displayInitialMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("        🏦 Simple Banking System 🏦");
        System.out.println("=".repeat(60));
        System.out.println("1. Customer Login");
        System.out.println("2. Create New Account");
        System.out.println("3. Migrate Old Account");
        System.out.println("4. Admin Login");
        System.out.println("5. Exit");
        System.out.println("=".repeat(60));
    }

    private static void customerLogin() {
        System.out.println("\n--- Customer Login ---");

        String accountNumber = InputUtil.readString("Enter account number: ");

        // Use the new migration-aware authentication
        loggedInAccount = bank.authenticateUserWithMigration(accountNumber);

        if (loggedInAccount == null) {
            System.out.println("❌ Login failed. Please try again.");
            return;
        }

        System.out.println("\n✅ Login successful!");
        System.out.println("Welcome, " + loggedInAccount.getCustomerName() + "!");

        showCustomerMenu();
    }

    private static void createNewAccount() {
        System.out.println("\n--- Create New Account ---");

        String customerName = InputUtil.readString("Enter customer name: ");
        String password = InputUtil.readString("Create password: ");
        String pin = InputUtil.readString("Create 4-digit PIN: ");

        if (!AuthService.isValidPin(pin)) {
            System.out.println("❌ PIN must be exactly 4 digits!");
            return;
        }

        System.out.println("\nAccount Number Options:");
        System.out.println("1. Auto-generate account number");
        System.out.println("2. Enter custom account number");

        int choice = InputUtil.readInt("Choose option (1 or 2): ");

        String accountNumber = null;
        if (choice == 2) {
            accountNumber = InputUtil.readString("Enter account number: ");
        }

        String createdAccountNumber = bank.createAccount(customerName, accountNumber, password, pin);

        if (createdAccountNumber != null) {
            bank.saveToFile();
        }
    }

    private static void adminLogin() {
        System.out.println("\n--- Admin Login ---");
        System.out.println("ℹ️  " + AuthService.getAdminPasswordHint());

        String password = InputUtil.readString("Enter admin password: ");

        AdminService adminService = new AdminService(bank);
        if (adminService.login(password)) {
            adminService.showAdminMenu();
            bank.saveToFile();
        }
    }

    private static void showCustomerMenu() {
        boolean customerSession = true;

        while (customerSession) {
            displayCustomerMenu();
            int choice = InputUtil.readInt("Choose option: ");
            System.out.println();

            switch (choice) {
                case 1:
                    deposit();
                    break;
                case 2:
                    withdraw();
                    break;
                case 3:
                    checkBalance();
                    break;
                case 4:
                    viewTransactions();
                    break;
                case 5:
                    customerSession = false;
                    logout();
                    break;
                default:
                    System.out.println("❌ Invalid option. Please choose between 1-5.");
            }
        }
    }

    private static void displayCustomerMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("        Banking Menu - " + loggedInAccount.getCustomerName());
        System.out.println("=".repeat(60));
        System.out.println("1. Deposit");
        System.out.println("2. Withdraw");
        System.out.println("3. Check Balance");
        System.out.println("4. View Transactions");
        System.out.println("5. Logout");
        System.out.println("=".repeat(60));
    }

    private static void deposit() {
        System.out.println("\n--- Deposit Money ---");

        double amount = InputUtil.readDouble("Enter deposit amount: $");

        if (bank.deposit(loggedInAccount.getAccountNumber(), amount)) {
            bank.saveToFile();
        }
    }

    private static void withdraw() {
        System.out.println("\n--- Withdraw Money ---");

        double amount = InputUtil.readDouble("Enter withdrawal amount: $");
        String pin = InputUtil.readString("Enter your 4-digit PIN: ");

        if (bank.withdraw(loggedInAccount.getAccountNumber(), amount, pin)) {
            bank.saveToFile();
        }
    }

    private static void checkBalance() {
        System.out.println("\n--- Check Balance ---");
        bank.checkBalance(loggedInAccount.getAccountNumber());
    }

    private static void viewTransactions() {
        System.out.println("\n--- View Transaction History ---");
        bank.viewTransactions(loggedInAccount.getAccountNumber());
    }

    private static void logout() {
        System.out.println("\n✅ Logged out successfully.");
        loggedInAccount = null;
    }

    private static void exitSystem() {
        System.out.println("\n--- Exiting System ---");
        bank.saveToFile();

        System.out.println("\n" + "=".repeat(60));
        System.out.println("Thank you for using Simple Banking System!");
        System.out.println("All data has been saved.");
        System.out.println("=".repeat(60));

        InputUtil.closeScanner();
        running = false;
    }
}