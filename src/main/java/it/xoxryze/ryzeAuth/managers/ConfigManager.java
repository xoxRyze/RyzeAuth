package it.xoxryze.ryzeAuth.managers;

import it.xoxryze.ryzeAuth.RyzeAuth;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private final RyzeAuth main;
    private final FileConfiguration config;

    public static String PASSWORD_NON_SICURA;
    public static String PASSWORD_CORTA;
    public static String PASSWORD_LUNGA;
    public static String PASSWORD_NON_COINCIDE;
    public static String ALREADY_REGISTRED;
    public static String ALREADY_AUTHENTICATED;
    public static String NOT_REGISTERED;
    public static String PASSWORD_SBAGLIATA;
    public static String NOT_AUTHENTICATED;
    public static String PASSWORD_IDENTICA;
    public static String PLAYER_MAI_ENTRATO;
    public static String NO_PERMISSION;
    public static int PW_LENGTH_MAX;
    public static int PW_LENGTH_MIN;
    public static String EVENT_NOT_AUTH;

    public ConfigManager(RyzeAuth main) {
        this.main = main;
        this.config = main.getConfig();
        loadMessages();
    }

    private void loadMessages() {
        PASSWORD_NON_SICURA = config.getString("messages.password-non-sicura",
                "§cLa password non è abbastanza sicura!");
        PASSWORD_CORTA = config.getString("messages.password-corta",
                "§cLa password è troppo corta!");
        PASSWORD_LUNGA = config.getString("messages.password-lunga",
                "§cLa password è troppo lunga!");
        PASSWORD_NON_COINCIDE = config.getString("messages.password-non-coincide",
                "§cLe password non coincidono!");
        ALREADY_REGISTRED = config.getString("messages.already-registred",
                "§cIl tuo account è già registrato!\nDigita §l/login <password>§c per autenticarti.");
        ALREADY_AUTHENTICATED = config.getString("messages.already-authenticated",
                "§cHai già effettuato l'autenticazione!");
        NOT_REGISTERED = config.getString("messages.not-registered",
                "§cDevi essere registrato per poterlo fare!");
        PASSWORD_SBAGLIATA = config.getString("messages.password-sbagliata",
                "§cLa password che hai inserito non è corretta!");
        NOT_AUTHENTICATED = config.getString("messages.not-authenticated",
                "§cDevi essere autenticato per poterlo fare!");
        PASSWORD_IDENTICA = config.getString("messages.password-identica",
                "§cLa nuova password è identica a quella attuale!");
        PLAYER_MAI_ENTRATO = config.getString("messages.player-never-join",
                "§cIl player non è mai entrato nel server!");
        NO_PERMISSION = config.getString("messages.no-permission",
                "§cNon hai il permesso per poterlo fare!");
        PW_LENGTH_MAX = config.getInt("config.password-lunghezza-max",
                16);
        PW_LENGTH_MIN = config.getInt("config.password-lunghezza-min",
                3);
        EVENT_NOT_AUTH = config.getString("messages.event-not-authenticated",
                "§cNon sei autenticato, digita /login <password> o /register");
    }

}
