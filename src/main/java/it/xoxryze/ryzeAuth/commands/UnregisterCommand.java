package it.xoxryze.ryzeAuth.commands;

import it.xoxryze.ryzeAuth.RyzeAuth;
import it.xoxryze.ryzeAuth.database.tables.AuthTable;
import it.xoxryze.ryzeAuth.utils.PasswordUtils;
import it.xoxryze.ryzeAuth.utils.Permission;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

import static it.xoxryze.ryzeAuth.managers.ConfigManager.*;

public class UnregisterCommand implements CommandExecutor {

    private final RyzeAuth main;
    private final AuthTable db;

    public UnregisterCommand(RyzeAuth main, AuthTable db) {
        this.main = main;
        this.db = db;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (!(sender instanceof Player player)) {
            return true;
        }

        if (!Permission.hasPermission(player, "unregister")) {
            player.sendMessage(Component.text(NO_PERMISSION));
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(Component.text(main.getConfig().getString("messages.usage-unregister",
                    "§cUtilizza /unregister <password>")));
            return true;
        }

        String playerpw;

        playerpw = String.valueOf(db.getPlayerPassword(player));

        if (playerpw == null) {
            player.sendMessage(Component.text(NOT_REGISTERED));
            return true;
        }

        if (main.getAuthenticated().contains(player.getUniqueId())) {
            if (!PasswordUtils.checkPassword(args[0], playerpw)) {
                player.sendMessage(Component.text(PASSWORD_SBAGLIATA));
                return true;
            }
            db.updatePlayerPassword(player, null);
            main.getAuthenticated().remove(player.getUniqueId());
            player.sendMessage(Component.text(main.getConfig().getString("messages.success-unregistered",
                    "§aTi sei unregistrato con successo!")));
            return true;
        }
        player.sendMessage(Component.text(NOT_AUTHENTICATED));
        return true;
    }
}
