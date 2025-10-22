package it.xoxryze.ryzeAuth.listeners;

import it.xoxryze.ryzeAuth.RyzeAuth;
import it.xoxryze.ryzeAuth.database.tables.AuthTable;
import it.xoxryze.ryzeAuth.utils.Permission;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

public class PlayerJoin implements Listener {
    private final AuthTable db;
    private final RyzeAuth main;

    public PlayerJoin(AuthTable db, RyzeAuth main) {
        this.db = db;
        this.main = main;
    }

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent e) throws SQLException {
        Player player = e.getPlayer();

        if (Permission.hasNormPermission(player, "bypass")) {
            main.getAuthenticated().add(player.getUniqueId());
            player.sendMessage(Component.text("\n §aʙʏᴘᴀѕѕ \n §7Sei stato autenticato in automatico"));
            db.updatePlayerAddress(player, player.getAddress().toString());
            return;
        }

        String registered, lastaddress;
        registered = "Yes";

        lastaddress = String.valueOf(db.getPlayerAddress(player));
        if (lastaddress == null) {
            lastaddress = "Non-existent";
            registered = "No";
        }

        player.getMetadata("");

        main.getLogger().info(" ");
        main.getLogger().info("PLAYER JOIN");
        main.getLogger().info("Username: " + player.getName());
        main.getLogger().info("UuId: " + player.getUniqueId());
        main.getLogger().info("Address: " + player.getAddress());
        main.getLogger().info("Last Address: " + lastaddress);
        main.getLogger().info("Registered: " + registered);
        main.getLogger().info(" ");
        return;
    }


}
