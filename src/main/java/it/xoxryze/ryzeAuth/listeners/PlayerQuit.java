package it.xoxryze.ryzeAuth.listeners;

import it.xoxryze.ryzeAuth.RyzeAuth;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    private final RyzeAuth main;

    public PlayerQuit(RyzeAuth main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {

        Player player = e.getPlayer();

        if (main.getAuthenticated().contains(player.getUniqueId())) {
            main.getAuthenticated().remove(player.getUniqueId());
        }


    }

}