package it.xoxryze.ryzeAuth.listeners;

import it.xoxryze.ryzeAuth.RyzeAuth;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerQuit implements Listener {

    private final RyzeAuth main;

    public PlayerQuit(RyzeAuth main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        UUID playerUUID = e.getPlayer().getUniqueId();
        main.getAuthenticated().remove(playerUUID);
    }
}