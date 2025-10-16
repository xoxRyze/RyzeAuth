package it.xoxryze.ryzeAuth.commands;

import it.xoxryze.ryzeAuth.RyzeAuth;
import it.xoxryze.ryzeAuth.database.DatabaseManager;
import it.xoxryze.ryzeAuth.utils.Palette;
import it.xoxryze.ryzeAuth.utils.PasswordUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class RegisterCommand implements CommandExecutor {

    private final DatabaseManager db;
    private final RyzeAuth main;

    public RegisterCommand(DatabaseManager db, RyzeAuth main) {
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

        String playerpassword;

        try {
            playerpassword = db.getPlayerPassword(player);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (main.authenticated.contains(player.getUniqueId())) {
            player.sendMessage(Component.text(main.ALREADY_AUTHENTICATED));
            return true;
        }

        if (playerpassword != null) {
            player.sendMessage(Component.text(
                    main.ALREADY_REGISTRED
            ));
            return true;
        }

        if (!args[0].equals(args[1])) {
            player.sendMessage(Component.text(main.PASSWORD_NON_COINCIDE));
            return true;
        }

        Integer lunghezzapw = args[0].length();

        if (lunghezzapw < main.PW_LENGHT_MIN) {
            player.sendMessage(Component.text(main.PASSWORD_CORTA));
            return true;
        }

        if (lunghezzapw > main.PW_LENGHT_MAX) {
            player.sendMessage(Component.text(main.PASSWORD_LUNGA));
            return true;
        }

        if (args[0].contains("ciao") || args[0].contains(player.getName()) || args[0].equals("12345")) {
            player.sendMessage(Component.text(main.PASSWORD_NON_SICURA));
            return true;
        }

        try {
            db.updatePlayerPassword(player, PasswordUtils.hashPassword(args[0]));
            db.updatePlayerAdress(player, player.getAddress().toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String registrated = main.getConfig().getString("messages.registrazione-compeltata",
                "§aHai effettuato la registrazione con successo!");
        player.sendMessage(Component.text(
                registrated));
        main.authenticated.add(player.getUniqueId());
        return true;
    }
}
