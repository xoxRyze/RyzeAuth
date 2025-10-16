package it.xoxryze.ryzeAuth.listeners;

import it.xoxryze.ryzeAuth.RyzeAuth;
import it.xoxryze.ryzeAuth.database.DatabaseManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    private final RyzeAuth main;
    private final DatabaseManager db;

    public PlayerQuit(RyzeAuth main, DatabaseManager db) {
        this.main = main;
        this.db = db;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {

        Player player = e.getPlayer();

        if (main.authenticated.contains(player.getUniqueId())) {
            main.authenticated.remove(player.getUniqueId());
        }


    }

}
