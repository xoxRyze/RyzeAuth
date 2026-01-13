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
                    "§cUtilizza /register <password> <password>")));
            return true;
        }

        if (main.getAuthenticated().contains(player.getUniqueId())) {
            player.sendMessage(Component.text(ALREADY_AUTHENTICATED));
            return true;
        }

        Optional<String> playerPasswordOpt = db.getPlayerPassword(player).join();

        if (playerPasswordOpt.isPresent()) {
            player.sendMessage(Component.text(ALREADY_REGISTRED));
            return true;
        }

        if (!args[0].equals(args[1])) {
            player.sendMessage(Component.text(PASSWORD_NON_COINCIDE));
            return true;
        }

        Integer lunghezzapw = args[0].length();

        if (lunghezzapw < PW_LENGTH_MIN) {
            player.sendMessage(Component.text(PASSWORD_CORTA));
            return true;
        }

        if (lunghezzapw > PW_LENGTH_MAX) {
            player.sendMessage(Component.text(PASSWORD_LUNGA));
            return true;
        }

        if (args[0].contains("ciao") || args[0].contains(player.getName()) || args[0].equals("12345")) {
            player.sendMessage(Component.text(PASSWORD_NON_SICURA));
            return true;
        }

        db.updatePlayerPassword(player, PasswordUtils.hashPassword(args[0]));
        db.updatePlayerAddress(player, player.getAddress().toString());
        String registrated = main.getConfig().getString("messages.registrazione-compeltata",
                "§aHai effettuato la registrazione con successo!");
        player.sendMessage(Component.text(
                registrated));
        main.getAuthenticated().add(player.getUniqueId());
        return true;
    }
}