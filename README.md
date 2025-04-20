# Credentials-Managment
The software helps you save your credentials.



import java.util.Scanner;
import java.util.Random;
public class PasswordManager {
    static Scanner scanner = new Scanner(System.in);

    //ROT13 encrypted
    static String passwordData =
        rot13("google.com|user1@gmail.com|MySecure123;;") +
        rot13("facebook.com|john@fb.com|Pass@2024;;") +
        rot13("github.com|dev@gh.com|Code!rocks;;");

    public static void main(String[] args) {
        while (true) {
            System.out.println("\nPassword Manager Menu:");
            System.out.println("1. Show passwords");
            System.out.println("2. Generate/Add password");
            System.out.println("3. Edit password");
            System.out.println("4. Close");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

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

    // Show stored passwords (decrypt using ROT13 before displaying)
    static void showPasswords() {
        if (passwordData.trim().isEmpty()) {
            System.out.println("No passwords saved.");
            return;
        }

        String[] entries = passwordData.split(";;");
        System.out.println("\nSaved Passwords:");
        for (int i = 0; i < entries.length; i++) {
            if (entries[i].trim().isEmpty()) continue;
            String decryptedEntry = rot13(entries[i]);
            String[] parts = decryptedEntry.split("\\|");
            if (parts.length == 3) {
                System.out.println((i + 1) + ". Website: " + parts[0]);
                System.out.println("   Username: " + parts[1]);
                System.out.println("   Password: " + parts[2]);
            }
        }
    }

    // Add a new password (encrypt using ROT13 before storing)
    static void addPassword() {
        System.out.print("Enter website: ");
        String website = scanner.nextLine();

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

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

        String encryptedData = rot13(website + "|" + username + "|" + password + ";;");
        passwordData += encryptedData;
        System.out.println("Password added successfully.");
    }

    // Edit an existing password (decrypt, edit, then re-encrypt)
    static void editPassword() {
        String[] entries = passwordData.split(";;");

        if (entries.length == 0) {
            System.out.println("No passwords to edit.");
            return;
        }

        System.out.println("\nSelect a password to edit:");
        for (int i = 0; i < entries.length; i++) {
            if (entries[i].trim().isEmpty()) continue;
            String decryptedEntry = rot13(entries[i]);
            String[] parts = decryptedEntry.split("\\|");
            if (parts.length == 3) {
                System.out.println((i + 1) + ". " + parts[0] + " | " + parts[1]);
            }
        }

        System.out.print("Enter entry number: ");
        int index = Integer.parseInt(scanner.nextLine()) - 1;

        if (index < 0 || index >= entries.length || entries[index].trim().isEmpty()) {
            System.out.println("Invalid entry number.");
            return;
        }

        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();

        String decryptedEntry = rot13(entries[index]);
        String[] parts = decryptedEntry.split("\\|");
        if (parts.length == 3) {
            entries[index] = parts[0] + "|" + parts[1] + "|" + newPassword;
        }

        // Re-encrypt the updated data
        passwordData = "";
        for (String entry : entries) {
            if (entry.trim().isEmpty()) continue;
            passwordData += rot13(entry) + ";;";
        }
        System.out.println("Password updated successfully.");
    }

    // Generate a random password of the specified length
    static String generatePassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%&*";
        StringBuilder password = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(rand.nextInt(chars.length())));
        }
        return password.toString();
    }

    // ROT13 encryption
    public static String rot13(String text) {
        final int SHIFT = 13;
        char[] result = new char[text.length()]; 
        
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            
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
