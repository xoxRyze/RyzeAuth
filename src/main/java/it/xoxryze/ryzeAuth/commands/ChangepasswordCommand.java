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
import java.util.Optional;

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
            player.sendMessage(Component.text("You must have to log in first!", Palette.RED));
            return true;
        }

        if (args.length != 2) {
            player.sendMessage(Component.text(main.getConfig().getString("messages.usage-changepassword",
                    "§cUse /changepassword <password> <new password>")));
            return true;
        }

        Optional<String> currentHashedOpt = db.getPlayerPassword(player).join();
        String newPassword = args[1];

        if (currentHashedOpt.isEmpty()) {
            player.sendMessage(Component.text("§cAn error occurred, please contact a Staff Member."));
            return true;
        }

        String currentHashedPassword = currentHashedOpt.get();

        if (!PasswordUtils.checkPassword(args[0], currentHashedPassword)) {
            player.sendMessage(Component.text(PASSWORD_UNCORRECT));
            return true;
        }

        if (PasswordUtils.checkPassword(newPassword, currentHashedPassword)) {
            player.sendMessage(Component.text(SAME_PASSWORD));
            return true;
        }

        if (!PasswordUtils.isValidPassword(newPassword, player.getName())) {
            player.sendMessage(Component.text(UNSECURE_PASSWORD));
            return true;
        }

        if (newPassword.length() < PW_LENGTH_MIN) {
            player.sendMessage(Component.text(SHORT_PASSWORD));
            return true;
        }

        if (newPassword.length() > PW_LENGTH_MAX) {
            player.sendMessage(Component.text(LONG_PASSWORD));
            return true;
        }

        String hashedNewPassword = PasswordUtils.hashPassword(newPassword);
        db.updatePlayerPassword(player, hashedNewPassword);

        player.sendMessage(Component.text(main.getConfig().getString("messages.success-changepassword",
                "§aYou have successfully changed your password..")));
        return true;
    }
}