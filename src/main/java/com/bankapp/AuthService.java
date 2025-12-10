package com.bankapp;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AuthService {
    private static final String ADMIN_PASSWORD_HASH = hashPassword("admin123");

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error hashing password: " + e.getMessage());
            return null;
        }
    }

    public static boolean verifyPassword(String inputPassword, String storedHash) {
        String inputHash = hashPassword(inputPassword);
        return inputHash != null && inputHash.equals(storedHash);
    }

    public static boolean verifyPin(String inputPin, String storedPin) {
        return inputPin != null && inputPin.equals(storedPin);
    }

    public static boolean isValidPin(String pin) {
        if (pin == null || pin.length() != 4) {
            return false;
        }
        for (char c : pin.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public static boolean authenticateAdmin(String password) {
        return verifyPassword(password, ADMIN_PASSWORD_HASH);
    }

    public static String getAdminPasswordHint() {
        return "Default admin password: admin123";
    }
}