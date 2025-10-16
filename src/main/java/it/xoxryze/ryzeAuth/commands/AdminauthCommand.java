package it.xoxryze.ryzeAuth.commands;

import it.xoxryze.ryzeAuth.RyzeAuth;
import it.xoxryze.ryzeAuth.database.DatabaseManager;
import it.xoxryze.ryzeAuth.utils.Palette;
import it.xoxryze.ryzeAuth.utils.PasswordUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class AdminauthCommand implements CommandExecutor {

    private final RyzeAuth main;
    private final DatabaseManager db;

    public AdminauthCommand(RyzeAuth main, DatabaseManager db) {
        this.main = main;
        this.db = db;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (args.length < 1) {
            if (sender instanceof Player player) {
                player.sendMessage(Component.empty());
                player.sendMessage(Component.text("\uD83D\uDEC8 /adminauth", Palette.AQUA)
                        .hoverEvent(HoverEvent.showText(Component.text("§7RyzeAuth by @RyzeProjects on Telegram"))));
                player.sendMessage(Component.empty());
                player.sendMessage(Component.text("/adminauth changepassword <player> <password>", Palette.AQUA)
                        .hoverEvent(HoverEvent.showText(Component.text("§7Cambia la password ad un player"))));
                player.sendMessage(Component.text("/adminauth checkip <player>", Palette.AQUA)
                        .hoverEvent(HoverEvent.showText(Component.text("§7Controlla l'ip di un utente"))));
                player.sendMessage(Component.text("/adminauth unregister <player>", Palette.AQUA)
                        .hoverEvent(HoverEvent.showText(Component.text("§7Unregistra forzatamente un player"))));
                player.sendMessage(Component.text("/adminauth register <player> <password>", Palette.AQUA)
                        .hoverEvent(HoverEvent.showText(Component.text("§7Registra forzatamente un player"))));
                player.sendMessage(Component.empty());
                return true;
            }
            sender.sendMessage(Component.empty());
            sender.sendMessage(Component.text("\uD83D\uDEC8 /adminauth", Palette.AQUA));
            sender.sendMessage(Component.empty());
            sender.sendMessage(Component.text("/adminauth changepassword <player> <password>", Palette.AQUA));
            sender.sendMessage(Component.text("/adminauth checkip <player>", Palette.AQUA));
            sender.sendMessage(Component.text("/adminauth unregister <player>", Palette.AQUA));
            sender.sendMessage(Component.text("/adminauth register <player> <password>", Palette.AQUA));
            sender.sendMessage(Component.empty());
            return true;
        }

        if (sender instanceof Player player) {
            if (!player.hasPermission("ryzeauth.command.adminauth")) {
                player.sendMessage(Component.text(main.NO_PERMISSION));
                return true;
            }
        }

        if (args[0].equals("changepassword") || args[0].equalsIgnoreCase("unregister") ||
                args[0].equalsIgnoreCase("register") || args[0].equalsIgnoreCase("checkip")) {

            if (args[0].equalsIgnoreCase("changepassword")) {

                if (args.length != 3) {
                    sender.sendMessage(Component.text(main.getConfig().getString("messages.usage-adminauth-changepassword",
                            "Utilizza /adminauth changepassword <player> <password>")));
                    return true;
                }

                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

                if (!target.hasPlayedBefore()) {
                    sender.sendMessage(Component.text(main.PLAYER_MAI_ENTRATO));
                    return true;
                }

                String targetpw;
                try {
                    targetpw = db.getPlayerPassword(target);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                if (targetpw == null) {
                    sender.sendMessage(Component.text(main.getConfig().getString("messages.player-never-authenticated",
                            "§cIl player non si è mai autenticato!")));
                    return true;
                }

                if (args[2].contains("ciao") || (args[2].contains(sender.getName()) ||
                        (args[2].equals("12345")))) {
                    sender.sendMessage(Component.text(main.PASSWORD_NON_SICURA));
                    return true;
                }

                Integer lunghezzapw = args[2].length();

                if (lunghezzapw < main.PW_LENGHT_MIN) {
                    sender.sendMessage(Component.text(main.PASSWORD_CORTA));
                    return true;
                }

                if (lunghezzapw > main.PW_LENGHT_MAX) {
                    sender.sendMessage(Component.text(main.PASSWORD_LUNGA));
                    return true;
                }

                try {
                    String hashed = PasswordUtils.hashPassword(args[2]);
                    db.updatePlayerPassword(target, hashed);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                sender.sendMessage(Component.text("Hai cambiato la password di §a" + target.getName(),
                        Palette.GREEN));
                return true;
            }

            if (args[0].equalsIgnoreCase("unregister")) {

                if (args.length != 2) {
                    sender.sendMessage(Component.text(main.getConfig().getString("messages.usage-adminauth-unregister",
                            "§cUtilizza /adminauth unregister <player>")));
                    return true;
                }

                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

                if (!target.hasPlayedBefore()) {
                    sender.sendMessage(Component.text(main.PLAYER_MAI_ENTRATO));
                    return true;
                }

                String playerpw;

                try {
                    playerpw = db.getPlayerPassword(target);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                if (playerpw == null) {
                    sender.sendMessage(Component.text(main.getConfig().getString("messages.player-never-authenticated",
                            "§cIl player non si è mai autenticato!")));
                    return true;
                }

                try {
                    db.updatePlayerPassword(target, null);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                sender.sendMessage(Component.text("Hai unregistrato con successo §a" + target.getName(),
                        Palette.GREEN));
                if (target.isOnline()) {
                    target = (Player) target;

                    if (main.authenticated.contains(target.getUniqueId())) {
                        ((Player) target).kick(Component.text("Sei stato uregistrato forzatamente da uno Staffer", Palette.RED));
                        main.authenticated.remove(target.getUniqueId());
                        return true;
                    }

                    main.authenticated.remove(target.getUniqueId());
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("register")) {

                if (args.length != 3) {
                    sender.sendMessage(Component.text(main.getConfig().getString("messages.usage-adminauth-register",
                            "§cUtilizza /adminauth register <player> <password>")));
                    return true;
                }

                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

                if (!target.hasPlayedBefore()) {
                    sender.sendMessage(Component.text(main.PLAYER_MAI_ENTRATO));
                    return true;
                }

                String playerpw;

                try {
                    playerpw = db.getPlayerPassword(target);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                if (args[2].contains("ciao") || (args[2].contains(sender.getName()) ||
                        (args[2].equals("12345")))) {
                    sender.sendMessage(Component.text(main.PASSWORD_NON_SICURA));
                    return true;
                }

                Integer lunghezzapw = args[2].length();

                if (lunghezzapw < main.PW_LENGHT_MIN) {
                    sender.sendMessage(Component.text(main.PASSWORD_CORTA));
                    return true;
                }

                if (lunghezzapw > main.PW_LENGHT_MAX) {
                    sender.sendMessage(Component.text(main.PASSWORD_LUNGA));
                    return true;
                }

                try {
                    String hashed = PasswordUtils.hashPassword(args[2]);
                    db.updatePlayerPassword(target, hashed);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                sender.sendMessage(Component.text("Hai registrato con successo §a" + target.getName(), Palette.GREEN));

                if (target.isOnline()) {
                    target = (Player) target;
                    main.authenticated.add(target.getUniqueId());
                    ((Player) target).sendMessage(Component.text("Sei stato registrato forzatamente da uno Staffer"
                            , Palette.GREEN));
                }

                return true;
            }

            if (args[0].equalsIgnoreCase("checkip")) {

                if (args.length != 2) {
                    sender.sendMessage(Component.text(main.getConfig().getString("messages.usage-adminauth-checkip",
                            "§cUtilizza /adminauth checkip <player>")));
                    return true;
                }

                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

                if (target == null || !target.hasPlayedBefore()) {
                    sender.sendMessage(Component.text(main.PLAYER_MAI_ENTRATO));
                    return true;
                }

                String playerip;
                try {
                    playerip = db.getPlayerAdress(target);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                String status = "§cOffline";

                if (target.isOnline()) {
                    status = "§aOnline";
                }

                sender.sendMessage(Component.text("\nPlayer: §f" + target.getName() + "\nStatus: §f" + status
                        + "\n§rLast Adress: §f" + playerip + "\n", Palette.AQUA));

                return true;
            }

        }

        sender.sendMessage(Component.text(main.getConfig().getString("messages.usage-adminauth",
                "§cComando errato, utilizza /adminauth per informazioni")));
        return true;
    }
}
