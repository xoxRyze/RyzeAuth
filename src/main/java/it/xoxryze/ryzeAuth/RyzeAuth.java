package it.xoxryze.ryzeAuth;

import it.xoxryze.ryzeAuth.commands.*;
import it.xoxryze.ryzeAuth.database.DatabaseManager;
import it.xoxryze.ryzeAuth.database.tables.AuthTable;
import it.xoxryze.ryzeAuth.listeners.*;
import it.xoxryze.ryzeAuth.managers.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RyzeAuth extends JavaPlugin {

    private ConfigManager configManager;

    private DatabaseManager dbManager;
    private AuthTable authTable;

    private final List<UUID> authenticated = new ArrayList<>();

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        dbManager = new DatabaseManager(this);
        authTable = new AuthTable(dbManager);
        saveDefaultConfig();
        configManager = new ConfigManager(this);

        getCommand("register").setExecutor(new RegisterCommand(authTable, this));
        getCommand("login").setExecutor(new LoginCommand(authTable, this));
        getCommand("changepassword").setExecutor(new ChangepasswordCommand(this, authTable));
        getCommand("unregister").setExecutor(new UnregisterCommand(this, authTable));
        getCommand("adminauth").setExecutor(new AdminauthCommand(this, authTable));
        getCommand("adminauth").setTabCompleter(new AdminauthTabCompleter());
        getCommand("unlogin").setExecutor(new UnloginCommand(this));

        getServer().getPluginManager().registerEvents(new PlayerQuit(this), this);
        getServer().getPluginManager().registerEvents(new PlayerMove(this), this);
        getServer().getPluginManager().registerEvents(new PlayerChat(this), this);
        getServer().getPluginManager().registerEvents(new PlayerCommand(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteract(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(authTable, this), this);

        getLogger().info("\nRyzeAuth è stato abilitato con successo!");
    }

    @Override
    public void onDisable() {
        getLogger().info("RyzeAuth è stato disabilitato.");
    }

    public AuthTable getAuthTable() {
        return authTable;
    }

    public List<UUID> getAuthenticated() {
        return authenticated;
    }
}