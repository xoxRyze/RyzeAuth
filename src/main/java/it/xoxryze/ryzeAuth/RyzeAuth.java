package it.xoxryze.ryzeAuth;

import it.xoxryze.ryzeAuth.commands.*;
import it.xoxryze.ryzeAuth.database.DatabaseManager;
import it.xoxryze.ryzeAuth.listeners.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RyzeAuth extends JavaPlugin {
    public List<UUID> authenticated = new ArrayList<>();
    private DatabaseManager db;
    public String PASSWORD_NON_SICURA = getConfig().getString("messages.password-non-sicura",
            "§cLa password non è abbastanza sicura!");
    public String PASSWORD_CORTA = getConfig().getString("messages.password-corta",
            "§cLa password è troppo corta!");
    public String PASSWORD_LUNGA = getConfig().getString("messages.password-lunga",
            "§cLa password è troppo lunga!");
    public String PASSWORD_NON_COINCIDE = getConfig().getString("messages.password-non-coincide",
            "§cLe password non coincidono!");
    public String ALREADY_REGISTRED = getConfig().getString("messages.already-registred",
            "§cIl tuo account è già registrato!\nDigita §l/login <password>§c per autenticarti.");
    public String ALREADY_AUTHENTICATED = getConfig().getString("messages.already-authenticated",
            "§cHai già effettuato l'autenticazione!");
    public String NOT_REGISTERED = getConfig().getString("messages.not-registered",
            "§cDevi essere registrato per poterlo fare!");
    public String PASSWORD_SBAGLIATA = getConfig().getString("messages.password-sbagliata",
            "§cLa password che hai inserito non è corretta!");
    public String NOT_AUTHENTICATED = getConfig().getString("messages.not-authenticated",
            "§cDevi essere autenticato per poterlo fare!");
    public String PASSWORD_IDENTICA = getConfig().getString("messages.password-identica",
            "§cLa nuova password è identica a quella attuale!");
    public String PLAYER_MAI_ENTRATO = getConfig().getString("messages.player-never-join",
            "§cIl player non è mai entrato nel server!");
    public String NO_PERMISSION = getConfig().getString("messages.no-permission",
            "§cNon hai il permesso per poterlo fare!");
    public Integer PW_LENGHT_MAX = getConfig().getInt("config.password-lunghezza-max", 16);
    public Integer PW_LENGHT_MIN = getConfig().getInt("config.password-lunghezza-min", 3);
    public String EVENT_NOT_AUTH = getConfig().getString("messages.event-not-authenticated",
            "§cNon sei autenticato, digita /login <password> o /register");

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        try {
            db = new DatabaseManager(getDataFolder().getAbsolutePath() + "/ryzeauth.db");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        saveDefaultConfig();
        getCommand("register").setExecutor(new RegisterCommand(db, this));
        getCommand("login").setExecutor(new LoginCommand(db, this));
        getCommand("changepassword").setExecutor(new ChangepasswordCommand(this, db));
        getCommand("unregister").setExecutor(new UnregisterCommand(this, db));
        getCommand("adminauth").setExecutor(new AdminauthCommand(this, db));
        getCommand("adminauth").setTabCompleter(new AdminauthTabCompleter());
        getServer().getPluginManager().registerEvents(new PlayerQuit(this, db), this);
        getServer().getPluginManager().registerEvents(new PlayerMove(this), this);
        getServer().getPluginManager().registerEvents(new PlayerChat(this), this);
        getServer().getPluginManager().registerEvents(new PlayerCommand(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteract(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(db, this), this);
        getLogger().info("\nRyzeAuth è stato abilitato con successo!");

    }

    @Override
    public void onDisable() {
        try {
            db.closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        getLogger().info("RyzeAuth è stato disabilitato.");
    }
}
