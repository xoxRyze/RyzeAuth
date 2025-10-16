package it.xoxryze.ryzeAuth.listeners;

import it.xoxryze.ryzeAuth.RyzeAuth;
import it.xoxryze.ryzeAuth.database.DatabaseManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

public class PlayerJoin implements Listener {
    private final DatabaseManager db;
    private final RyzeAuth main;

    public PlayerJoin(DatabaseManager db, RyzeAuth main) {
        this.db = db;
        this.main = main;
    }

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent e) throws SQLException {
        Player player = e.getPlayer();

        String registered, lastadress;
        registered = "Yes";
        lastadress = db.getPlayerAdress(player);

        if (lastadress == null) {
            lastadress = "Non-existent";
            registered = "No";
        }

        main.getLogger().info(" ");
        main.getLogger().info("PLAYER JOIN");
        main.getLogger().info("Username: " + player.getName());
        main.getLogger().info("UuId: " + player.getUniqueId());
        main.getLogger().info("Adress: " + player.getAddress());
        main.getLogger().info("Last Adress: " + lastadress);
        main.getLogger().info("Registered: " + registered);
        main.getLogger().info(" ");
        return;
    }

}
