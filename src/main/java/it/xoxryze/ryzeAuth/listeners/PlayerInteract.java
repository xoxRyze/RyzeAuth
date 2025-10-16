package it.xoxryze.ryzeAuth.listeners;

import it.xoxryze.ryzeAuth.RyzeAuth;
import it.xoxryze.ryzeAuth.utils.Palette;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {
    private final RyzeAuth main;

    public PlayerInteract(RyzeAuth main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerInteract (PlayerInteractEvent e) {

        Player player = e.getPlayer();

        if (!main.authenticated.contains(player.getUniqueId())) {
            e.setCancelled(true);
            player.sendMessage(Component.text(main.EVENT_NOT_AUTH));
            return;
        }

    }

}
