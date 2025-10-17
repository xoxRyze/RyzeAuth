package it.xoxryze.ryzeAuth.commands;

import it.xoxryze.ryzeAuth.RyzeAuth;
import it.xoxryze.ryzeAuth.database.DatabaseManager;
import it.xoxryze.ryzeAuth.utils.Palette;
import it.xoxryze.ryzeAuth.utils.PasswordUtils;
import it.xoxryze.ryzeAuth.utils.Permission;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class UnregisterCommand implements CommandExecutor {

    private final RyzeAuth main;
    private final DatabaseManager db;

    public UnregisterCommand(RyzeAuth main, DatabaseManager db) {
        this.main = main;
        this.db = db;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (!(sender instanceof Player player)) {
            return true;
        }

        if (!Permission.hasPermission(player, "unregister")) {
            player.sendMessage(Component.text(main.NO_PERMISSION));
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(Component.text(main.getConfig().getString("messages.usage-unregister",
                    "§cUtilizza /unregister <password>")));
            return true;
        }

        String playerpw;

        try {
            playerpw = db.getPlayerPassword(player);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (playerpw == null) {
            player.sendMessage(Component.text(main.NOT_REGISTERED));
            return true;
        }

        if (main.authenticated.contains(player.getUniqueId())) {
            if (!PasswordUtils.checkPassword(args[0], playerpw)) {
                player.sendMessage(Component.text(main.PASSWORD_SBAGLIATA));
                return true;
            }
            try {
                db.updatePlayerPassword(player, null);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            main.authenticated.remove(player.getUniqueId());
            player.sendMessage(Component.text(main.getConfig().getString("messages.success-unregistered",
                    "§aTi sei unregistrato con successo!")));
            return true;
        }
        player.sendMessage(Component.text(main.NOT_AUTHENTICATED));
        return true;
    }
}
