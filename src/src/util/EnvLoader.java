package util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class EnvLoader {
    private static Map<String, String> envVars = new HashMap<>();

    static {
        loadEnvFile();
    }

    private static void loadEnvFile() {
        // Try multiple possible paths
        String[] possiblePaths = {
                ".env",           // Current working directory
                "../.env",       // From project root
                "../../.env",         // Explicit current directory
        };

        for (String path : possiblePaths) {
            if (Files.exists(Paths.get(path))) {
                System.out.println("Found .env file at: " + path);
                try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        line = line.trim();
                        if (line.isEmpty() || line.startsWith("#")) {
                            continue;
                        }

                        String[] parts = line.split("=", 2);
                        if (parts.length == 2) {
                            String key = parts[0].trim();
                            String value = parts[1].trim();
                            // Remove quotes if present
                            if (value.startsWith("\"") && value.endsWith("\"")) {
                                value = value.substring(1, value.length() - 1);
                            }
                            envVars.put(key, value);
                            System.out.println("Loaded env var: " + key + " = " + value);
                        }
                    }
                    return; // Successfully loaded, exit
                } catch (IOException e) {
                    System.err.println("Error reading .env file at " + path + ": " + e.getMessage());
                }
            }
        }
        System.err.println("Error loading environment variables from .env file. File not found in any expected location.");
    }

    public static String get(String key) {
        String value = System.getenv(key);
        if (value == null) {
            value = envVars.get(key);
        }
        System.out.println("Env var: " + key + " = " + value);
        return value;
    }
}