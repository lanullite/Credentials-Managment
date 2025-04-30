import java.util.Scanner;
import java.util.Random;

public class PasswordManager {
    // Shared scanner for user input across all methods
    static Scanner scanner = new Scanner(System.in);

    // Encrypted password storage using ROT13 (demo purposes only - not secure!)
    // Format: "website|username|password;;" (each entry separated by ;;)
    static String passwordData =
        rot13("google.com|user1@gmail.com|MySecure123;;") +
        rot13("facebook.com|john@fb.com|Pass@2024;;") +
        rot13("github.com|dev@gh.com|Code!rocks;;");

    public static void main(String[] args) {
        // Main program loop
        while (true) {
            System.out.println("\nPassword Manager Menu:");
            System.out.println("1. Show passwords");
            System.out.println("2. Generate/Add password");
            System.out.println("3. Edit password");
            System.out.println("4. Close");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            // Handle user selection
            switch (choice) {
                case "1":
                    showPasswords();
                    break;
                case "2":
                    addPassword();
                    break;
                case "3":
                    editPassword();
                    break;
                case "4":
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    /**
     * Displays all stored passwords after decryption
     * Splits entries using ;; delimiter and decrypts each with ROT13
     */
    static void showPasswords() {
        if (passwordData.trim().isEmpty()) {
            System.out.println("No passwords saved.");
            return;
        }

        String[] entries = passwordData.split(";;");
        System.out.println("\nSaved Passwords:");
        for (int i = 0; i < entries.length; i++) {
            if (entries[i].trim().isEmpty()) continue;
            
            // Decrypt and parse entry components
            String decryptedEntry = rot13(entries[i]);
            String[] parts = decryptedEntry.split("\\|");
            
            if (parts.length == 3) {
                System.out.println((i + 1) + ". Website: " + parts[0]);
                System.out.println("   Username: " + parts[1]);
                System.out.println("   Password: " + parts[2]);
            }
        }
    }

    /**
     * Adds new password entry with ROT13 encryption
     * Supports both manual entry and generated passwords
     */
    static void addPassword() {
        System.out.print("Enter website: ");
        String website = scanner.nextLine();

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        // Password generation flow
        System.out.print("Do you want to generate a password? (y/n): ");
        String generate = scanner.nextLine().toLowerCase();

        String password;
        if (generate.equals("y")) {
            System.out.print("Enter desired password length: ");
            int length = Integer.parseInt(scanner.nextLine());
            password = generatePassword(length);
            System.out.println("Generated Password: " + password);
        } else {
            System.out.print("Enter password: ");
            password = scanner.nextLine();
        }

        // Encrypt and append new entry
        String encryptedData = rot13(website + "|" + username + "|" + password + ";;");
        passwordData += encryptedData;
        System.out.println("Password added successfully.");
    }

    /**
     * Edits existing password entry
     * Decrypts selected entry, updates password, then re-encrypts all data
     */
    static void editPassword() {
        String[] entries = passwordData.split(";;");

        if (entries.length == 0) {
            System.out.println("No passwords to edit.");
            return;
        }

        // Display selectable entries
        System.out.println("\nSelect a password to edit:");
        for (int i = 0; i < entries.length; i++) {
            if (entries[i].trim().isEmpty()) continue;
            
            String decryptedEntry = rot13(entries[i]);
            String[] parts = decryptedEntry.split("\\|");
            
            if (parts.length == 3) {
                System.out.println((i + 1) + ". " + parts[0] + " | " + parts[1]);
            }
        }

        // Get and validate entry selection
        System.out.print("Enter entry number: ");
        int index = Integer.parseInt(scanner.nextLine()) - 1;

        if (index < 0 || index >= entries.length || entries[index].trim().isEmpty()) {
            System.out.println("Invalid entry number.");
            return;
        }

        // Update password in selected entry
        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();

        String decryptedEntry = rot13(entries[index]);
        String[] parts = decryptedEntry.split("\\|");
        if (parts.length == 3) {
            entries[index] = parts[0] + "|" + parts[1] + "|" + newPassword;
        }

        // Rebuild encrypted data store
        passwordData = "";
        for (String entry : entries) {
            if (entry.trim().isEmpty()) continue;
            passwordData += rot13(entry) + ";;";
        }
        System.out.println("Password updated successfully.");
    }

    /**
     * Generates random password with mixed characters
     * @param length Desired password length
     * @return Generated password string
     */
    static String generatePassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%&*";
        StringBuilder password = new StringBuilder();
        Random rand = new Random();
        
        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(rand.nextInt(chars.length())));
        }
        return password.toString();
    }

    /**
     * Simple ROT13 encryption (Caesar cipher with 13-shift)
     * Note: Not secure for real-world use - demonstration only!
     * @param text Input string to encrypt/decrypt
     * @return ROT13-transformed string
     */
    public static String rot13(String text) {
        final int SHIFT = 13;
        char[] result = new char[text.length()]; 
        
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            
            // Handle uppercase and lowercase letters only
            if (Character.isUpperCase(c)) {
                c = (char) ((c - 'A' + SHIFT) % 26 + 'A');
            } else if (Character.isLowerCase(c)) {
                c = (char) ((c - 'a' + SHIFT) % 26 + 'a');
            }
            result[i] = c; 
        }
        
        return new String(result); 
    }
}