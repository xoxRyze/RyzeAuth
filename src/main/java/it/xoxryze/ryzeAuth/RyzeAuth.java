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
        getLogger().info("RyzeAuth è stato abilitato!");

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
