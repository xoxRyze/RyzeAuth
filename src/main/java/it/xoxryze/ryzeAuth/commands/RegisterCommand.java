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
            player.sendMessage(Component.text("Utilizza /register <password> <password>", Palette.RED));
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

        if (playerpassword != null) {
            player.sendMessage(Component.text(
                    "Il tuo account è già registrato!\nDigita /login <password> per autenticarti.", Palette.RED
            ));
            return true;
        }

        if (!args[0].equals(args[1])) {
            player.sendMessage(Component.text("Le password non coincidono!", Palette.RED));
            return true;
        }

        Integer lunghezzapw = args[0].length();

        if (lunghezzapw < 5) {
            player.sendMessage(Component.text("La password è troppo corta!", Palette.RED));
            return true;
        }

        if (lunghezzapw > 16) {
            player.sendMessage(Component.text("La password è troppo lunga!", Palette.RED));
            return true;
        }

        if (args[0].contains("ciao") || args[0].contains(player.getName()) || args[0].equals("12345")) {
            player.sendMessage(Component.text("La password non è abbastanza sicura!", Palette.RED));
            return true;
        }

        try {
            db.updatePlayerPassword(player, PasswordUtils.hashPassword(args[0]));
            db.updatePlayerAdress(player, player.getAddress().toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        player.sendMessage(Component.text(
                "Ti sei registrato con successo!\nNel caso dovessi perdere la password, attaccati al pisello",
                Palette.GREEN));
        main.authenticated.add(player.getUniqueId());
        return true;
    }
}
