package it.xoxryze.ryzeAuth.listeners;

import it.xoxryze.ryzeAuth.RyzeAuth;
import it.xoxryze.ryzeAuth.data.AuthPlayer;
import it.xoxryze.ryzeAuth.database.tables.AuthTable;
import it.xoxryze.ryzeAuth.utils.MojangSessionAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.Objects;

public class PlayerJoin implements Listener {
    private final AuthTable db;
    private final RyzeAuth main;

    public PlayerJoin(AuthTable db, RyzeAuth main) {
        this.db = db;
        this.main = main;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) throws SQLException {
        Player player = e.getPlayer();

        db.getPlayerAddress(player).thenAccept(optionalAddress -> {
            String registered = "Yes";
            String lastaddress = "Non-existent";

            if (optionalAddress.isPresent()) {
                lastaddress = optionalAddress.get();
            } else {
                registered = "No";
            }

            String currentIp = Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress();

            db.updatePlayerAddress(player, currentIp);
            main.getLogger().info(" ");
            main.getLogger().info("PLAYER JOIN");
            main.getLogger().info("Username: " + player.getName());
            main.getLogger().info("UuId: " + player.getUniqueId());
            main.getLogger().info("Current Address: " + currentIp);
            main.getLogger().info("Last Address: " + lastaddress);
            main.getLogger().info("Registered: " + registered);
            MojangSessionAPI.isAuthenticated(player.getName()).thenAccept(isPremium -> {
                main.getLogger().info("Premium: " + isPremium);
                main.getLogger().info(" ");

                if (main.getConfig().getBoolean("config.premium-auto-authentication", false)) {
                    if (isPremium) {
                        main.getCacheManager().getAuthenticated().add(new AuthPlayer(player.getUniqueId(), true));
                        Bukkit.getScheduler().runTask(main,
                                () -> player.sendMessage(Component.text(main.getConfig().getString("messages.success-premium-login",
                                        "§aYou have successfully logged in automatically!"))));
                    }
                }
            });
        });
    }
}