package it.xoxryze.ryzeAuth.managers;

import it.xoxryze.ryzeAuth.RyzeAuth;

public class ConfigManager {
    private static RyzeAuth main;
    public static String PASSWORD_NON_SICURA = main.getConfig().getString("messages.password-non-sicura",
            "§cLa password non è abbastanza sicura!");
    public static String PASSWORD_CORTA = main.getConfig().getString("messages.password-corta",
            "§cLa password è troppo corta!");
    public static String PASSWORD_LUNGA = main.getConfig().getString("messages.password-lunga",
            "§cLa password è troppo lunga!");
    public static String PASSWORD_NON_COINCIDE = main.getConfig().getString("messages.password-non-coincide",
            "§cLe password non coincidono!");
    public static String ALREADY_REGISTRED = main.getConfig().getString("messages.already-registred",
            "§cIl tuo account è già registrato!\nDigita §l/login <password>§c per autenticarti.");
    public static String ALREADY_AUTHENTICATED = main.getConfig().getString("messages.already-authenticated",
            "§cHai già effettuato l'autenticazione!");
    public static String NOT_REGISTERED = main.getConfig().getString("messages.not-registered",
            "§cDevi essere registrato per poterlo fare!");
    public static String PASSWORD_SBAGLIATA = main.getConfig().getString("messages.password-sbagliata",
            "§cLa password che hai inserito non è corretta!");
    public static String NOT_AUTHENTICATED = main.getConfig().getString("messages.not-authenticated",
            "§cDevi essere autenticato per poterlo fare!");
    public static String PASSWORD_IDENTICA = main.getConfig().getString("messages.password-identica",
            "§cLa nuova password è identica a quella attuale!");
    public static String PLAYER_MAI_ENTRATO = main.getConfig().getString("messages.player-never-join",
            "§cIl player non è mai entrato nel server!");
    public static String NO_PERMISSION = main.getConfig().getString("messages.no-permission",
            "§cNon hai il permesso per poterlo fare!");
    public static Integer PW_LENGHT_MAX = main.getConfig().getInt("config.password-lunghezza-max", 16);
    public static Integer PW_LENGHT_MIN = main.getConfig().getInt("config.password-lunghezza-min", 3);
    public static String EVENT_NOT_AUTH = main.getConfig().getString("messages.event-not-authenticated",
            "§cNon sei autenticato, digita /login <password> o /register");

    public ConfigManager(RyzeAuth main) {
        this.main = main;
    }

    public static void getConfigMessage(String str, String defaultmsg) {
        main.getConfig().getString("messages." + str, defaultmsg);
        return;
    }

    public static void getConfigConfig(String str, String defaultmsg) {
        main.getConfig().getString("config." + str, defaultmsg);
        return;
    }

    public static void getConfig(String str, String defaultmsg) {
        main.getConfig().getString(str, defaultmsg);
        return;
    }

}
