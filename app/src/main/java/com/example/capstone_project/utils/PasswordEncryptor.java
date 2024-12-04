package com.example.capstone_project.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordEncryptor {

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // Checking password equality is done by comparing the hashed passwords
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        String hashedInput = hashPassword(plainTextPassword);
        return hashedInput.equals(hashedPassword);
    }
}