package it.xoxryze.ryzeAuth.listeners;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import it.xoxryze.ryzeAuth.RyzeAuth;
import it.xoxryze.ryzeAuth.utils.Palette;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

    private final RyzeAuth main;

    public PlayerMove(RyzeAuth main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {

        Player player = e.getPlayer();

        if (!main.authenticated.contains(player.getUniqueId())) {
            e.setCancelled(true);
            player.sendMessage(Component.text("Non sei autenticato, digita /login <password>", Palette.RED));
            return;
        }

    }

    @EventHandler
    public void onPlayerJump(PlayerJumpEvent e) {

        Player player = e.getPlayer();

        if (!main.authenticated.contains(player.getUniqueId())) {
            e.setCancelled(true);
            player.sendMessage(Component.text(main.EVENT_NOT_AUTH));
            return;
        }

    }

}
