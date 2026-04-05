package it.xoxryze.ryzeAuth.managers;

import it.xoxryze.ryzeAuth.RyzeAuth;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private final RyzeAuth main;
    private final FileConfiguration config;

    public static String UNSECURE_PASSWORD;
    public static String SHORT_PASSWORD;
    public static String LONG_PASSWORD;
    public static String PASSWORD_NOT_MATCH;
    public static String ALREADY_REGISTRED;
    public static String ALREADY_AUTHENTICATED;
    public static String NOT_REGISTERED;
    public static String PASSWORD_UNCORRECT;
    public static String NOT_AUTHENTICATED;
    public static String SAME_PASSWORD;
    public static String PLAYER_NEVER_JOIN;
    public static String NO_PERMISSION;
    public static String EVENT_NOT_REG;
    public static int PW_LENGTH_MAX;
    public static int PW_LENGTH_MIN;
    public static String EVENT_NOT_AUTH;

    public ConfigManager(RyzeAuth main) {
        this.main = main;
        this.config = main.getConfig();
        loadMessages();
    }

    private void loadMessages() {
        UNSECURE_PASSWORD = config.getString("messages.password-unsecure",
                "§cThe password is not secure enough!");
        SHORT_PASSWORD = config.getString("messages.short-password",
                "§cThe password is too short!");
        LONG_PASSWORD = config.getString("messages.long-password",
                "§cThe password is too long!");
        PASSWORD_NOT_MATCH = config.getString("messages.password-not-match",
                "§cThe passwords do not match!");
        ALREADY_REGISTRED = config.getString("messages.already-registered",
                "§cYour account is already registered!\n§cType §l/login <password>§c to authenticate.");
        ALREADY_AUTHENTICATED = config.getString("messages.already-authenticated",
                "§cYou are already authenticated!");
        NOT_REGISTERED = config.getString("messages.not-registered",
                "§cYou must be registered to do this!");
        PASSWORD_UNCORRECT = config.getString("messages.password-incorrect",
                "§cThe password you entered is incorrect!");
        NOT_AUTHENTICATED = config.getString("messages.not-authenticated",
                "§cYou must be logged in to do this.!");
        SAME_PASSWORD = config.getString("messages.same-password",
                "§cThe new password is identical to the current one!");
        PLAYER_NEVER_JOIN = config.getString("messages.player-never-join",
                "§cThe player never joined the server!");
        NO_PERMISSION = config.getString("messages.no-permission",
                "§cYou don't have permission to do that!");
        PW_LENGTH_MAX = config.getInt("config.password-max-length",
                16);
        PW_LENGTH_MIN = config.getInt("config.password-min-length",
                3);
        EVENT_NOT_AUTH = config.getString("messages.event-not-authenticated",
                "§cYou are not logged in, type /login <password>");
        EVENT_NOT_REG = config.getString("messages.event-not-registered",
                "§cYou are not registered, type /register <password> <password>");
    }

}