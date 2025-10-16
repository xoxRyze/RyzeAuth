package it.xoxryze.ryzeAuth.listeners;

import it.xoxryze.ryzeAuth.RyzeAuth;
import it.xoxryze.ryzeAuth.utils.Palette;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;

public class PlayerCommand implements Listener {

    private final RyzeAuth main;

    public PlayerCommand(RyzeAuth main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerCommand (PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        String message = e.getMessage().toLowerCase();

        if (message.startsWith("/login") || message.startsWith("/register") ||
                message.startsWith("/ryzeauth:login") || message.startsWith("/ryzeauth:register")) {
            return;
        }

        if (!main.authenticated.contains(player.getUniqueId())) {
            e.setCancelled(true);
            player.sendMessage(Component.text(main.EVENT_NOT_AUTH));
            return;
        }


    }

}
