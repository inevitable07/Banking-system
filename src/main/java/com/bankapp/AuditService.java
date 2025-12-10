package com.bankapp;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AuditService {
    private static final String LOG_DIRECTORY = "logs";
    private static final String LOG_FILE = LOG_DIRECTORY + "/audit.log";
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    static {
        initializeLogDirectory();
    }

    private static void initializeLogDirectory() {
        File directory = new File(LOG_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    private static void writeLog(String logEntry) {
        try {
            String timestamp = LocalDateTime.now().format(FORMATTER);
            String fullEntry = String.format("[%s] %s%n", timestamp, logEntry);

            Files.write(
                    Paths.get(LOG_FILE),
                    fullEntry.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            System.err.println("Failed to write audit log: " + e.getMessage());
        }
    }

    public static void logLoginSuccess(String accountNumber) {
        writeLog(String.format("ACTION=LOGIN account=%s status=SUCCESS",
                accountNumber));
    }

    public static void logLoginFailure(String accountNumber, String reason) {
        writeLog(String.format("ACTION=LOGIN account=%s status=FAILED details=%s",
                accountNumber, reason));
    }

    public static void logWrongPassword(String accountNumber) {
        writeLog(String.format("ACTION=WRONG_PASSWORD account=%s status=FAILED details=Invalid password attempt",
                accountNumber));
    }

    public static void logWrongPin(String accountNumber) {
        writeLog(String.format("ACTION=WRONG_PIN account=%s status=FAILED details=Invalid PIN attempt",
                accountNumber));
    }

    public static void logWithdrawSuccess(String accountNumber, double amount) {
        writeLog(String.format("ACTION=WITHDRAW account=%s status=SUCCESS details=Amount=$%.2f",
                accountNumber, amount));
    }

    public static void logWithdrawFailure(String accountNumber, String reason) {
        writeLog(String.format("ACTION=WITHDRAW account=%s status=FAILED details=%s",
                accountNumber, reason));
    }

    public static void logDepositSuccess(String accountNumber, double amount) {
        writeLog(String.format("ACTION=DEPOSIT account=%s status=SUCCESS details=Amount=$%.2f",
                accountNumber, amount));
    }

    public static void logAccountLocked(String accountNumber, String adminUser) {
        writeLog(String.format("ACTION=ACCOUNT_LOCK account=%s status=SUCCESS details=Locked by admin=%s",
                accountNumber, adminUser));
    }

    public static void logAccountUnlocked(String accountNumber, String adminUser) {
        writeLog(String.format("ACTION=ACCOUNT_UNLOCK account=%s status=SUCCESS details=Unlocked by admin=%s",
                accountNumber, adminUser));
    }

    public static void logAdminLogin(String adminUser) {
        writeLog(String.format("ACTION=ADMIN_LOGIN admin=%s status=SUCCESS",
                adminUser));
    }

    public static void logAdminLoginFailure(String reason) {
        writeLog(String.format("ACTION=ADMIN_LOGIN status=FAILED details=%s",
                reason));
    }

    public static void logAdminLogout(String adminUser) {
        writeLog(String.format("ACTION=ADMIN_LOGOUT admin=%s status=SUCCESS",
                adminUser));
    }

    public static List<String> readAuditLogs() {
        List<String> logs = new ArrayList<>();
        File file = new File(LOG_FILE);

        if (!file.exists()) {
            return logs;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logs.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading audit logs: " + e.getMessage());
        }

        return logs;
    }

    public static void displayAuditLogs(int limit) {
        List<String> logs = readAuditLogs();

        if (logs.isEmpty()) {
            System.out.println("No audit logs found.");
            return;
        }

        System.out.println("\n" + "=".repeat(80));
        System.out.println("AUDIT LOGS (Showing last " +
                Math.min(limit, logs.size()) + " entries)");
        System.out.println("=".repeat(80));

        int startIndex = Math.max(0, logs.size() - limit);
        for (int i = startIndex; i < logs.size(); i++) {
            System.out.println(logs.get(i));
        }

        System.out.println("=".repeat(80));
        System.out.println("Total log entries: " + logs.size());
    }
}