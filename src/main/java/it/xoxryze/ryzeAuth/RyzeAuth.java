package it.xoxryze.ryzeAuth;

import it.xoxryze.ryzeAuth.api.RyzeAuthAPI;
import it.xoxryze.ryzeAuth.database.DatabaseManager;
import it.xoxryze.ryzeAuth.database.tables.AuthTable;
import it.xoxryze.ryzeAuth.managers.ConfigManager;
import it.xoxryze.ryzeAuth.utils.CustomLoader;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class RyzeAuth extends JavaPlugin {

    private DatabaseManager dbManager;
    private AuthTable authTable;
    private RyzeAuthAPI ryzeAuthAPI;
    private final Set<UUID> authenticated = Collections.synchronizedSet(new HashSet<>());

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        saveDefaultConfig();
        dbManager = new DatabaseManager(this);
        authTable = new AuthTable(dbManager);
        ryzeAuthAPI = new RyzeAuthAPI(this);
        ConfigManager configManager = new ConfigManager(this);
        CustomLoader customLoader = new CustomLoader(this, authTable);
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

    public Set<UUID> getAuthenticated() {
        return authenticated;
    }

    public RyzeAuthAPI getAPI() {
        return ryzeAuthAPI;
    }

}