package it.xoxryze.ryzeAuth;

import it.xoxryze.ryzeAuth.api.RyzeAuthAPI;
import it.xoxryze.ryzeAuth.data.AuthPlayer;
import it.xoxryze.ryzeAuth.database.DatabaseManager;
import it.xoxryze.ryzeAuth.database.tables.AuthTable;
import it.xoxryze.ryzeAuth.managers.ConfigManager;
import it.xoxryze.ryzeAuth.managers.CacheManager;
import it.xoxryze.ryzeAuth.utils.CustomLoader;
import it.xoxryze.ryzeAuth.utils.Metrics;
import it.xoxryze.ryzeAuth.utils.MojangSessionAPI;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class RyzeAuth extends JavaPlugin {

    private DatabaseManager dbManager;
    private AuthTable authTable;
    private RyzeAuthAPI ryzeAuthAPI;
    private CacheManager cacheManager;
    private MojangSessionAPI mojangSessionAPI;
    private it.xoxryze.ryzeAuth.utils.Metrics metrics;

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        saveDefaultConfig();
        cacheManager = new CacheManager();
        dbManager = new DatabaseManager(this);
        authTable = new AuthTable(dbManager);
        ryzeAuthAPI = new RyzeAuthAPI(this);
        ConfigManager configManager = new ConfigManager(this);
        CustomLoader customLoader = new CustomLoader(this, authTable);
        metrics = new Metrics(this, 30816);
        mojangSessionAPI = new MojangSessionAPI();
        CustomLoader.initCommands();
        CustomLoader.initListener();
        if (getServer().getOnlineMode()) {
            getConfig().set("config.premium-auto-authentication", false);
            saveConfig();
            reloadConfig();
            getLogger().info(
                    "config.premium-auto-authentication has been set to false (Server must be in offline mode!)");
        }

        getLogger().info(
                "RyzeAuth v" + getPluginMeta().getVersion() + " has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("RyzeAuth has been disabled.");
    }

    public AuthTable getAuthTable() {
        return authTable;
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public List<AuthPlayer> getAuthenticated() {
        return getCacheManager().getAuthenticated();
    }

    public RyzeAuthAPI getAPI() {
        return ryzeAuthAPI;
    }

}