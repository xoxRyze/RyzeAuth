package it.xoxryze.ryzeAuth.commands;

import it.xoxryze.ryzeAuth.RyzeAuth;
import it.xoxryze.ryzeAuth.database.tables.AuthTable;
import it.xoxryze.ryzeAuth.utils.PasswordUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

import static it.xoxryze.ryzeAuth.managers.ConfigManager.*;

public class LoginCommand implements CommandExecutor {

    private final AuthTable db;
    private final RyzeAuth main;

    public LoginCommand(AuthTable db, RyzeAuth main) {
        this.db = db;
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (!(sender instanceof Player player)) {
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(Component.text(main.getConfig().getString("messages.usage-login",
                    "§cUtilizza /login <password>")));
            return true;
        }

        String playerpassword = String.valueOf(db.getPlayerPassword(player));

        if (main.getAuthenticated().contains(player.getUniqueId())) {
            player.sendMessage(Component.text(ALREADY_AUTHENTICATED));
            return true;
        }

        if (playerpassword == null) {
            player.sendMessage(Component.text(NOT_REGISTERED));
            return true;
        }

        if (PasswordUtils.checkPassword(args[0], playerpassword)) {
            player.sendMessage(Component.text(main.getConfig().getString("messages.success-login",
                    "§aHai effettuato il login con successo!")));
            main.getAuthenticated().add(player.getUniqueId());
            db.updatePlayerAddress(player, player.getAddress().toString());

            return true;
        }
        player.sendMessage(Component.text(PASSWORD_SBAGLIATA));
        return true;
    }
}
