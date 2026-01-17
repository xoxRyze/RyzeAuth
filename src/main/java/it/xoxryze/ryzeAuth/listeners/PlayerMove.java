package it.xoxryze.ryzeAuth.listeners;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import it.xoxryze.ryzeAuth.RyzeAuth;
import it.xoxryze.ryzeAuth.database.tables.AuthTable;
import it.xoxryze.ryzeAuth.utils.Palette;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static it.xoxryze.ryzeAuth.managers.ConfigManager.EVENT_NOT_AUTH;
import static it.xoxryze.ryzeAuth.managers.ConfigManager.EVENT_NOT_REG;

public class PlayerMove implements Listener {

    private final RyzeAuth main;
    private final AuthTable authTable;

    public PlayerMove(RyzeAuth main, AuthTable authTable) {
        this.main = main;
        this.authTable = authTable;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
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

    @EventHandler
    public void onPlayerJump(PlayerJumpEvent e) {

        Player player = e.getPlayer();

        if (!main.getAuthenticated().contains(player.getUniqueId())) {
            e.setCancelled(true);
            player.sendMessage(Component.text(EVENT_NOT_AUTH));
            return;
        }
    }
}