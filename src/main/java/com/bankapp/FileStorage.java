package com.bankapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class FileStorage {
    private static final String DATA_DIRECTORY = "data";
    private static final String DATA_FILE = DATA_DIRECTORY + "/bank_data.json";
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public static void saveToFile(Map<String, Account> accounts) {
        try {
            File directory = new File(DATA_DIRECTORY);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String json = gson.toJson(accounts);

            try (FileWriter writer = new FileWriter(DATA_FILE)) {
                writer.write(json);
            }

            System.out.println("✅ Data saved successfully to " + DATA_FILE);
        } catch (IOException e) {
            System.err.println("❌ Error saving data to file: " + e.getMessage());
        }
    }

    public static Map<String, Account> loadFromFile() {
        File file = new File(DATA_FILE);

        if (!file.exists()) {
            System.out.println("ℹ️  No existing data file found. Starting fresh.");
            return new HashMap<>();
        }

        try {
            String json = new String(Files.readAllBytes(Paths.get(DATA_FILE)));

            Type type = new TypeToken<HashMap<String, Account>>(){}.getType();
            Map<String, Account> accounts = gson.fromJson(json, type);

            if (accounts == null) {
                return new HashMap<>();
            }

            System.out.println("✅ Data loaded successfully. " + accounts.size() +
                    " account(s) found.");
            return accounts;
        } catch (IOException e) {
            System.err.println("❌ Error reading data file: " + e.getMessage());
            return new HashMap<>();
        } catch (Exception e) {
            System.err.println("❌ Error parsing data file: " + e.getMessage());
            return new HashMap<>();
        }
    }

    public static boolean dataFileExists() {
        return new File(DATA_FILE).exists();
    }
}