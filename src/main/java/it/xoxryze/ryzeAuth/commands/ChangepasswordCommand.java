package it.xoxryze.ryzeAuth.commands;

import it.xoxryze.ryzeAuth.RyzeAuth;
import it.xoxryze.ryzeAuth.database.tables.AuthTable;
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

import static it.xoxryze.ryzeAuth.managers.ConfigManager.*;

public class ChangepasswordCommand implements CommandExecutor {

    private final RyzeAuth main;
    private final AuthTable db;

    public ChangepasswordCommand(RyzeAuth main, AuthTable db) {
        this.main = main;
        this.db = db;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (!(sender instanceof Player player)) {
            return true;
        }

        if (!Permission.hasPermission(player, "changepassword")) {
            player.sendMessage(Component.text(NO_PERMISSION));
            return true;
        }

        if (!main.getAuthenticated().contains(player.getUniqueId())) {
            player.sendMessage(Component.text("Devi prima effettuare il login!", Palette.RED));
            return true;
        }

        if (args.length != 2) {
            player.sendMessage(Component.text(main.getConfig().getString("messages.usage-changepassword",
                    "§cUtilizza /changepassword <password> <nuovapassword>")));
            return true;
        }

        String currentHashedPassword = String.valueOf(db.getPlayerPassword(player));
        String newPassword = args[1];

        if (!PasswordUtils.checkPassword(args[0], currentHashedPassword)) {
            player.sendMessage(Component.text(PASSWORD_SBAGLIATA));
            return true;
        }

        if (PasswordUtils.checkPassword(newPassword, currentHashedPassword)) {
            player.sendMessage(Component.text(PASSWORD_IDENTICA));
            return true;
        }

        if (newPassword.contains("ciao") || newPassword.contains(player.getName()) || newPassword.equals("12345")) {
            player.sendMessage(Component.text(PASSWORD_NON_SICURA));
            return true;
        }

        if (newPassword.length() < PW_LENGHT_MIN) {
            player.sendMessage(Component.text(PASSWORD_CORTA));
            return true;
        }

        if (newPassword.length() > PW_LENGHT_MAX) {
            player.sendMessage(Component.text(PASSWORD_LUNGA));
            return true;
        }

        String hashedNewPassword = PasswordUtils.hashPassword(newPassword);
        db.updatePlayerPassword(player, hashedNewPassword);

        player.sendMessage(Component.text(main.getConfig().getString("messages.success-changepassword",
                "§aHai cambiato la password con successo.")));
        return true;
    }
}
