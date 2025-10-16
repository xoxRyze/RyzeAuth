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

public class LoginCommand implements CommandExecutor {

    private final DatabaseManager db;
    private final RyzeAuth main;

    public LoginCommand(DatabaseManager db, RyzeAuth main) {
        this.db = db;
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (!(sender instanceof Player player)) {
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(Component.text("Utilizza /login <password>", Palette.RED));
            return true;
        }

        String playerpassword;

        try {
            playerpassword = db.getPlayerPassword(player);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (main.authenticated.contains(player.getUniqueId())) {
            player.sendMessage(Component.text("Sei già autenticato!", Palette.RED));
            return true;
        }

        if (playerpassword == null) {
            player.sendMessage(Component.text("Devi prima registrarti, utilizza /register", Palette.RED));
            return true;
        }

        if (args[0].equals(playerpassword)) {
            player.sendMessage(Component.text("Ti sei autenticato con successo!", Palette.GREEN));
            main.authenticated.add(player.getUniqueId());
            try {
                db.updatePlayerAdress(player, player.getAddress().toString());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return true;
        }
        player.sendMessage(Component.text("La password è errata! Riprova.", Palette.RED));
        return true;
    }
}
