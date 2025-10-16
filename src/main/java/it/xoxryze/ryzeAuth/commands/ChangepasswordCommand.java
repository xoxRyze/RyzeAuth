package it.xoxryze.ryzeAuth.commands;

import it.xoxryze.ryzeAuth.RyzeAuth;
import it.xoxryze.ryzeAuth.database.DatabaseManager;
import it.xoxryze.ryzeAuth.utils.Palette;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class ChangepasswordCommand implements CommandExecutor {

    private final RyzeAuth main;
    private final DatabaseManager db;

    public ChangepasswordCommand(RyzeAuth main, DatabaseManager db) {
        this.main = main;
        this.db = db;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (!(sender instanceof Player player)) {
            return true;
        }

        if (!player.hasPermission("ryzeauth.changepassword")) {
            player.sendMessage(Component.text("Non hai il permesso per poterlo fare!", Palette.RED));
            return true;
        }

        if (!main.authenticated.contains(player.getUniqueId())) {
            player.sendMessage(Component.text("Devi prima effettuare il login!", Palette.RED));
            return true;
        }

        if (args.length != 2) {
            player.sendMessage(Component.text("Utilizza /changepassword <password> <newpassword>", Palette.RED));
            return true;
        }

        String playerNowPassword;
        String playerNewPassword = args[1];
        try {
            playerNowPassword = db.getPlayerPassword(player);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (!args[0].equals(playerNowPassword)) {
            player.sendMessage(Component.text("La password è errata.", Palette.RED));
            return true;
        }

        if (playerNowPassword.equals(playerNewPassword)) {
            player.sendMessage(Component.text("Le password sono identiche!", Palette.RED));
            return true;
        }

        if (playerNewPassword.contains("ciao") || (playerNewPassword.contains(player.getName()) ||
                (playerNewPassword.equals("12345")))) {
            player.sendMessage(Component.text("La password è troppo debole!", Palette.RED));
            return true;
        }

        Integer lunghezzapw = playerNewPassword.length();

        if (lunghezzapw < 5) {
            player.sendMessage(Component.text("La password è troppo corta!", Palette.RED));
            return true;
        }

        if (lunghezzapw > 16) {
            player.sendMessage(Component.text("La password è troppo lunga!", Palette.RED));
            return true;
        }

        try {
            db.updatePlayerPassword(player, playerNewPassword);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        player.sendMessage(Component.text("Hai cambiato la password con successo.", Palette.GREEN));
        return true;
    }
}
