package it.xoxryze.ryzeAuth.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogUtils {

    private static final Logger logger = Logger.getLogger("RyzeAuth");

    public static void logError(Throwable throwable, String message) {
        logger.log(Level.SEVERE, throwable, () -> message);
    }
}
