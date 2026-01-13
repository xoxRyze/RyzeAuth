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
}