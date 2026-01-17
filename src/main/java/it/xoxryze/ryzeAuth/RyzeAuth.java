package it.xoxryze.ryzeAuth;

import it.xoxryze.ryzeAuth.database.DatabaseManager;
import it.xoxryze.ryzeAuth.database.tables.AuthTable;
import it.xoxryze.ryzeAuth.managers.ConfigManager;
import it.xoxryze.ryzeAuth.utils.CustomLoader;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RyzeAuth extends JavaPlugin {

    private ConfigManager configManager;
    private CustomLoader customLoader;
    private DatabaseManager dbManager;
    private AuthTable authTable;

    private final List<UUID> authenticated = new ArrayList<>();

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        saveDefaultConfig();
        dbManager = new DatabaseManager(this);
        authTable = new AuthTable(dbManager);
        configManager = new ConfigManager(this);
        customLoader = new CustomLoader(this, authTable);
        CustomLoader.initCommands();
        CustomLoader.initListener();
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