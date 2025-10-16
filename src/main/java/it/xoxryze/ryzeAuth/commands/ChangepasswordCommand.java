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
            player.sendMessage(Component.text(main.getConfig().getString("messages.usage-changepassword",
                    "§cUtilizza /changepassword <password> <nuovapassword>")));
            return true;
        }

        String currentHashedPassword;
        String newPassword = args[1];

        try {
            currentHashedPassword = db.getPlayerPassword(player);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (!PasswordUtils.checkPassword(args[0], currentHashedPassword)) {
            player.sendMessage(Component.text(main.PASSWORD_SBAGLIATA));
            return true;
        }

        if (PasswordUtils.checkPassword(newPassword, currentHashedPassword)) {
            player.sendMessage(Component.text(main.PASSWORD_IDENTICA));
            return true;
        }

        if (newPassword.contains("ciao") || newPassword.contains(player.getName()) || newPassword.equals("12345")) {
            player.sendMessage(Component.text(main.PASSWORD_NON_SICURA));
            return true;
        }

        if (newPassword.length() < main.PW_LENGHT_MIN) {
            player.sendMessage(Component.text(main.PASSWORD_CORTA));
            return true;
        }

        if (newPassword.length() > main.PW_LENGHT_MAX) {
            player.sendMessage(Component.text(main.PASSWORD_LUNGA));
            return true;
        }

        try {
            String hashedNewPassword = PasswordUtils.hashPassword(newPassword);
            db.updatePlayerPassword(player, hashedNewPassword);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        player.sendMessage(Component.text(main.getConfig().getString("success-changepassword",
                "§aHai cambiato la password con successo.")));
        return true;
    }
}
