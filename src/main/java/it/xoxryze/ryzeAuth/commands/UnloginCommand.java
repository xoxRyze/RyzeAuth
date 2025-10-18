package it.xoxryze.ryzeAuth.commands;

import it.xoxryze.ryzeAuth.RyzeAuth;
import it.xoxryze.ryzeAuth.utils.Permission;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static it.xoxryze.ryzeAuth.managers.ConfigManager.NOT_AUTHENTICATED;
import static it.xoxryze.ryzeAuth.managers.ConfigManager.NO_PERMISSION;

public class UnloginCommand implements CommandExecutor {

    private final RyzeAuth main;

    public UnloginCommand(RyzeAuth main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (!(sender instanceof Player player)) {
            return true;
        }

        if (!Permission.hasPermission(player, "unlogin")) {
            player.sendMessage(Component.text(NO_PERMISSION));
            return true;
        }

        if (args.length >= 1) {
            player.sendMessage(Component.text(main.getConfig().getString("messages.usage-unlogin",
                    "§cUtilizza /unlogin")));
            return true;
        }

        if (!main.getAuthenticated().contains(player.getUniqueId())) {
            player.sendMessage(NOT_AUTHENTICATED);
            return true;
        }

        player.sendMessage(Component.text(main.getConfig().getString("messages.success.unlogin",
                "§aHai effettuato l'unlogin con successo.")));
        main.getAuthenticated().remove(player.getUniqueId());
        return true;
    }
}
