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

import java.util.Optional;

import static it.xoxryze.ryzeAuth.managers.ConfigManager.*;

public class RegisterCommand implements CommandExecutor {

    private final AuthTable db;
    private final RyzeAuth main;

    public RegisterCommand(AuthTable db, RyzeAuth main) {
        this.db = db;
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (!(sender instanceof Player player)) {
            return true;
        }

        if (args.length != 2) {
            player.sendMessage(Component.text(main.getConfig().getString("messages.usage-register",
                    "§cUse /register <password> <password>")));
            return true;
        }

        if (main.getAuthenticated().contains(player.getUniqueId())) {
            player.sendMessage(Component.text(ALREADY_AUTHENTICATED));
            return true;
        }

         db.isRegistered(player).thenAccept(playerPasswordOpt -> {

            if (playerPasswordOpt.isPresent()) {
                player.sendMessage(Component.text(ALREADY_REGISTRED));
                return;
            }

             if (!args[0].equals(args[1])) {
                 player.sendMessage(Component.text(PASSWORD_NOT_MATCH));
                 return;
             }

             int passwordLength = args[0].length();

             if (passwordLength < PW_LENGTH_MIN) {
                 player.sendMessage(Component.text(SHORT_PASSWORD));
                 return;
             }

             if (passwordLength > PW_LENGTH_MAX) {
                 player.sendMessage(Component.text(LONG_PASSWORD));
                 return;
             }

             if (!PasswordUtils.isValidPassword(args[0], player.getName())) {
                 player.sendMessage(Component.text(UNSECURE_PASSWORD));
                 return;
             }

             db.updatePlayerPassword(player, PasswordUtils.hashPassword(args[0]));
             db.updatePlayerAddress(player, String.valueOf(player.getAddress()));

             player.sendMessage(Component.text(
                     main.getConfig().getString("messages.register-completed",
                             "§aYou have successfully registered!")));
             main.getAuthenticated().add(player.getUniqueId());

        });

        return true;
    }
}