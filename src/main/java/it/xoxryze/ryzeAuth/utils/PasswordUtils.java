package it.xoxryze.ryzeAuth.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    public static boolean checkPassword(String password, String hashed) {
        if (hashed == null || hashed.trim().isEmpty()) {
            return false;
        }

        if (!hashed.startsWith("$2a$") && !hashed.startsWith("$2b$") && !hashed.startsWith("$2y$")) {
            return false;
        }

        try {
            return BCrypt.checkpw(password, hashed);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static Boolean isValidPassword(String password, String playerName) {
        password = password.toLowerCase();
        playerName = playerName.toLowerCase();
        if (password.contains("ciao") || password.contains(playerName) ||
                password.equals("12345") || password.contains("hello")) {
            return false;
        }
        return true;
    }
}