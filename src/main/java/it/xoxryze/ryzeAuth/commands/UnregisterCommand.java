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

public class UnregisterCommand implements CommandExecutor {

    private final RyzeAuth main;
    private final DatabaseManager db;

    public UnregisterCommand(RyzeAuth main, DatabaseManager db) {
        this.main = main;
        this.db = db;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (!(sender instanceof Player player)) {
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(Component.text("Utilizza /unregister <password>", Palette.RED));
            return true;
        }

        String playerpw;

        try {
            playerpw = db.getPlayerPassword(player);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (playerpw == null) {
            player.sendMessage(Component.text("Non sei registrato, dunque non puoi farlo!", Palette.RED));
            return true;
        }

        if (main.authenticated.contains(player.getUniqueId())) {
            if (!args[0].equals(playerpw)) {
                player.sendMessage(Component.text("La password non è corretta!", Palette.RED));
                return true;
            }
            try {
                db.updatePlayerPassword(player, null);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            main.authenticated.remove(player.getUniqueId());
            player.sendMessage(Component.text("Ti sei unregistrato con successo!", Palette.GREEN));
            return true;
        }
        player.sendMessage(Component.text("Devi essere autenticato per poterlo fare!", Palette.RED));
        return true;
    }
}
