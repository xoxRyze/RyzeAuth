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
                "§cLa password non è abbastanza sicura!");
        SHORT_PASSWORD = config.getString("messages.short-password",
                "§cLa password è troppo corta!");
        LONG_PASSWORD = config.getString("messages.long-password",
                "§cLa password è troppo lunga!");
        PASSWORD_NOT_MATCH = config.getString("messages.password-not-match",
                "§cLe password non coincidono!");
        ALREADY_REGISTRED = config.getString("messages.already-registred",
                "§cIl tuo account è già registrato!\nDigita §l/login <password>§c per autenticarti.");
        ALREADY_AUTHENTICATED = config.getString("messages.already-authenticated",
                "§cHai già effettuato l'autenticazione!");
        NOT_REGISTERED = config.getString("messages.not-registered",
                "§cDevi essere registrato per poterlo fare!");
        PASSWORD_UNCORRECT = config.getString("messages.password-uncorrect",
                "§cLa password che hai inserito non è corretta!");
        NOT_AUTHENTICATED = config.getString("messages.not-authenticated",
                "§cDevi essere autenticato per poterlo fare!");
        SAME_PASSWORD = config.getString("messages.same-password",
                "§cLa nuova password è identica a quella attuale!");
        PLAYER_NEVER_JOIN = config.getString("messages.player-never-join",
                "§cIl player non è mai entrato nel server!");
        NO_PERMISSION = config.getString("messages.no-permission",
                "§cNon hai il permesso per poterlo fare!");
        PW_LENGTH_MAX = config.getInt("config.password-max-lenght",
                16);
        PW_LENGTH_MIN = config.getInt("config.password-min-lenght",
                3);
        EVENT_NOT_AUTH = config.getString("messages.event-not-authenticated",
                "§cYou are not logged in, type /login <password>");
        EVENT_NOT_REG = config.getString("messages.event-not-registered",
                "§cYou are not registered, type /register <password> <password>");
    }

}