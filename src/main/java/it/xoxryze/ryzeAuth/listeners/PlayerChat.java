package it.xoxryze.ryzeAuth.listeners;

import it.xoxryze.ryzeAuth.RyzeAuth;
import it.xoxryze.ryzeAuth.database.tables.AuthTable;
import it.xoxryze.ryzeAuth.utils.Palette;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;

import static it.xoxryze.ryzeAuth.managers.ConfigManager.EVENT_NOT_AUTH;
import static it.xoxryze.ryzeAuth.managers.ConfigManager.EVENT_NOT_REG;

public class PlayerChat implements Listener {

    private final RyzeAuth main;
    private final AuthTable authTable;

    public PlayerChat(RyzeAuth main, AuthTable authTable) {
        this.main = main;
        this.authTable = authTable;
    }

    @EventHandler
    public void onPlayerChat (PlayerChatEvent e) {
        Player player = e.getPlayer();

        if (!main.getAuthenticated().contains(player.getUniqueId())) {
            e.setCancelled(true);
            authTable.isRegistered(player).thenAccept(optionalPassword -> {
                if (optionalPassword.isPresent()) {
                    player.sendMessage(Component.text(
                            EVENT_NOT_AUTH));
                } else {
                    player.sendMessage(Component.text(
                            EVENT_NOT_REG));
                }
            });
            return;
        }
    }
}