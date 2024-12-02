package com.example.capstone_project.utils;

import org.mindrot.jbcrypt.BCrypt;

// BCrypt
public class PasswordEncryptor {

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Method to check if the provided password matches the hashed password
    public static boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
