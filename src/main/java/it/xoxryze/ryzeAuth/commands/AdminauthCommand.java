package it.xoxryze.ryzeAuth.commands;

import it.xoxryze.ryzeAuth.RyzeAuth;
import it.xoxryze.ryzeAuth.database.tables.AuthTable;
import it.xoxryze.ryzeAuth.managers.ConfigManager.*;
import it.xoxryze.ryzeAuth.utils.Palette;
import it.xoxryze.ryzeAuth.utils.PasswordUtils;
import it.xoxryze.ryzeAuth.utils.Permission;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static it.xoxryze.ryzeAuth.managers.ConfigManager.*;

public class AdminauthCommand implements CommandExecutor {

    private final RyzeAuth main;
    private final AuthTable db;

    public AdminauthCommand(RyzeAuth main, AuthTable db) {
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
                player.sendMessage(Component.text("/adminauth kick <player> <reason>", Palette.AQUA)
                        .hoverEvent(HoverEvent.showText(Component.text("§7Espelli un player dal server"))));
                player.sendMessage(Component.text("/adminauth dupeip [<player> | <ip>]", Palette.AQUA)
                        .hoverEvent(HoverEvent.showText(Component.text("§7Controlla gli account collegati ad un ip"))));
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
            sender.sendMessage(Component.text("/adminauth kick <player> <reason>", Palette.AQUA));
            sender.sendMessage(Component.text("/adminauth dupeip <player>", Palette.AQUA));
            sender.sendMessage(Component.empty());
            return true;
        }

        if (sender instanceof Player player) {
            if (!Permission.hasPermission(player, "adminauth")) {
                player.sendMessage(Component.text(NO_PERMISSION));
                return true;
            }
        }

        if (args[0].equals("changepassword") || args[0].equalsIgnoreCase("unregister") ||
                args[0].equalsIgnoreCase("register") || args[0].equalsIgnoreCase("checkip") ||
                args[0].equalsIgnoreCase("kick") || args[0].equalsIgnoreCase("dupeip")) {

            if (args[0].equalsIgnoreCase("changepassword")) {

                if (sender instanceof Player player) {
                    if (!Permission.hasPermission(player, "adminauth.changepassword")) {
                        player.sendMessage(Component.text(NO_PERMISSION));
                        return true;
                    }
                }

                if (args.length != 3) {
                    sender.sendMessage(Component.text(main.getConfig().getString("messages.usage-adminauth-changepassword",
                            "Utilizza /adminauth changepassword <player> <password>")));
                    return true;
                }

                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

                if (!target.hasPlayedBefore()) {
                    sender.sendMessage(Component.text(PLAYER_MAI_ENTRATO));
                    return true;
                }

                Optional<String> passwordOpt = db.getPlayerPassword(target).join();

                if (passwordOpt.isEmpty()) {
                    sender.sendMessage(Component.text(main.getConfig().getString("messages.player-never-authenticated",
                            "§cIl player non si è mai autenticato!")));
                    return true;
                }

                if (args[2].contains("ciao") || (args[2].contains(sender.getName()) ||
                        (args[2].equals("12345")))) {
                    sender.sendMessage(Component.text(PASSWORD_NON_SICURA));
                    return true;
                }

                Integer lunghezzapw = args[2].length();

                if (lunghezzapw < PW_LENGTH_MIN) {
                    sender.sendMessage(Component.text(PASSWORD_CORTA));
                    return true;
                }

                if (lunghezzapw > PW_LENGTH_MAX) {
                    sender.sendMessage(Component.text(PASSWORD_LUNGA));
                    return true;
                }

                String hashed = PasswordUtils.hashPassword(args[2]);
                db.updatePlayerPassword(target, hashed);
                sender.sendMessage(Component.text("Hai cambiato la password di §a" + target.getName(),
                        Palette.GREEN));
                return true;
            }

            if (args[0].equalsIgnoreCase("unregister")) {

                if (sender instanceof Player player) {
                    if (!Permission.hasPermission(player, "adminauth.unregister")) {
                        player.sendMessage(Component.text(NO_PERMISSION));
                        return true;
                    }
                }

                if (args.length != 2) {
                    sender.sendMessage(Component.text(main.getConfig().getString("messages.usage-adminauth-unregister",
                            "§cUtilizza /adminauth unregister <player>")));
                    return true;
                }

                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

                if (!target.hasPlayedBefore()) {
                    sender.sendMessage(Component.text(PLAYER_MAI_ENTRATO));
                    return true;
                }

                Optional<String> playerpw = String.valueOf(db.getPlayerPassword(target)).describeConstable();

                if (!playerpw.isPresent()) {
                    sender.sendMessage(Component.text(main.getConfig().getString("messages.player-never-authenticated",
                            "§cIl player non si è mai autenticato!")));
                    return true;
                }


                db.updatePlayerPassword(target, null);
                sender.sendMessage(Component.text("Hai unregistrato con successo §a" + target.getName(),
                        Palette.GREEN));
                if (target.isOnline()) {
                    target = (Player) target;

                    if (main.getAuthenticated().contains(target.getUniqueId())) {
                        ((Player) target).kick(Component.text("Sei stato uregistrato forzatamente da uno Staffer", Palette.RED));
                        main.getAuthenticated().remove(target.getUniqueId());
                        return true;
                    }

                    main.getAuthenticated().remove(target.getUniqueId());
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("register")) {

                if (sender instanceof Player player) {
                    if (!Permission.hasPermission(player, "adminauth.register")) {
                        player.sendMessage(Component.text(NO_PERMISSION));
                        return true;
                    }
                }

                if (args.length != 3) {
                    sender.sendMessage(Component.text(main.getConfig().getString("messages.usage-adminauth-register",
                            "§cUtilizza /adminauth register <player> <password>")));
                    return true;
                }

                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

                if (!target.hasPlayedBefore()) {
                    sender.sendMessage(Component.text(PLAYER_MAI_ENTRATO));
                    return true;
                }

                String playerpw = String.valueOf(db.getPlayerPassword(target));

                if (args[2].contains("ciao") || (args[2].contains(sender.getName()) ||
                        (args[2].equals("12345")))) {
                    sender.sendMessage(Component.text(PASSWORD_NON_SICURA));
                    return true;
                }

                Integer lunghezzapw = args[2].length();

                if (lunghezzapw < PW_LENGTH_MIN) {
                    sender.sendMessage(Component.text(PASSWORD_CORTA));
                    return true;
                }

                if (lunghezzapw > PW_LENGTH_MAX) {
                    sender.sendMessage(Component.text(PASSWORD_LUNGA));
                    return true;
                }

                String hashed = PasswordUtils.hashPassword(args[2]);
                db.updatePlayerPassword(target, hashed);
                sender.sendMessage(Component.text("Hai registrato con successo §a" + target.getName(), Palette.GREEN));

                if (target.isOnline()) {
                    target = (Player) target;
                    main.getAuthenticated().add(target.getUniqueId());
                    ((Player) target).sendMessage(Component.text("Sei stato registrato forzatamente da uno Staffer"
                            , Palette.GREEN));
                }

                return true;
            }

            if (args[0].equalsIgnoreCase("checkip")) {

                if (sender instanceof Player player) {
                    if (!Permission.hasPermission(player, "adminauth.checkip")) {
                        player.sendMessage(Component.text(NO_PERMISSION));
                        return true;
                    }
                }

                if (args.length != 2) {
                    sender.sendMessage(Component.text(main.getConfig().getString("messages.usage-adminauth-checkip",
                            "§cUtilizza /adminauth checkip <player>")));
                    return true;
                }

                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

                if (target == null || !target.hasPlayedBefore()) {
                    sender.sendMessage(Component.text(PLAYER_MAI_ENTRATO));
                    return true;
                }

                CompletableFuture<Optional<String>> playerip = db.getPlayerAddress(target);

                if (playerip.join().isEmpty()) {
                    sender.sendMessage(Component.text("Si è verificato un errore. (No Adress)", Palette.RED));
                    return true;
                }

                String status = "§cOffline";

                if (target.isOnline()) {
                    status = "§aOnline";
                }

                sender.sendMessage(Component.text("\nPlayer: §f" + target.getName() + "\n§rStatus: §f" + status
                        + "\n§rLast Address: §f" + playerip + "\n", Palette.AQUA));

                return true;
            }

            if (args[0].equalsIgnoreCase("kick")) {

                if (sender instanceof Player player) {
                    if (!Permission.hasPermission(player, "adminauth.kick")) {
                        player.sendMessage(Component.text(NO_PERMISSION));
                        return true;
                    }
                }

                if (args.length < 2) {
                    sender.sendMessage(Component.text(main.getConfig().getString("messages.usage-adminauth-kick",
                            "§cUtilizza /adminauth kick <player> <motivo>")));
                    return true;
                }

                Player target = Bukkit.getPlayer(args[1]);

                if (target == null) {
                    return true;
                }

                if (args.length < 3) {
                    target.kick();
                    String sendername = "Console";
                    if (sender instanceof Player) {
                        sendername = sender.getName();
                    }
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (Permission.hasPermission(p, "adminauth")) {
                            p.sendMessage(Component.text(
                                    main.getConfig().getString("messages.success-kick",
                                                    "\n §aᴋɪᴄᴋ\n §f%player%§7 ha espluso §f%target%§7 dal server.")
                                            .replace("%player%", sendername)
                                            .replace("%target%", target.getName())));
                            return true;
                        }
                    }
                    return true;
                }
                String motivo = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                target.kick(Component.text(motivo));
                String sendername = "Console";
                if (sender instanceof Player) {
                    sendername = sender.getName();
                }
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (Permission.hasPermission(p, "adminauth")) {
                        p.sendMessage(Component.text(
                                        main.getConfig().getString("messages.success-kick",
                                                        "\n §aᴋɪᴄᴋ\n §f%player%§7 ha espluso §f%target%§7 dal server.")
                                                .replace("%player%", sendername)
                                                .replace("%target%", target.getName()))
                                .hoverEvent(HoverEvent.showText(Component.text("§7Motivo §8» §f" + args[2]))));
                        return true;
                    }
                }

            }

            if (args[0].equalsIgnoreCase("dupeip")) {
                if (args.length != 2) {
                    sender.sendMessage(Component.text(main.getConfig().getString("messages.usage-adminauth-dupeip",
                            "§cUtilizza /adminauth dupeip [<player> | <ip>]")));
                    return true;
                }

                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

                if (!target.hasPlayedBefore()) {
                    db.getAddress(args[1]).thenAccept(nicknames -> {
                        if (nicknames.isEmpty()) {
                            sender.sendMessage(Component.text("Nessun risultato trovato.", Palette.RED));
                        } else {
                            sender.sendMessage(Component.text("§7Player trovati con l'ip inserito:\n" +
                                    " §f" + String.join(", ", nicknames)));
                        }
                    });
                    return true;
                }
                String padress = String.valueOf(db.getPlayerAddress(target));

                db.getAddress(padress).thenAccept(nicknames -> {
                    if (nicknames.isEmpty()) {
                        sender.sendMessage(Component.text("Nessun risultato trovato.", Palette.RED));
                    } else {
                        sender.sendMessage(Component.text("§7Player trovati con l'ip di %target%:\n" +
                                " §f" + String.join(", ", nicknames)
                                .replace("%target%", target.getName())));
                    }
                });

            }
            return true;
        }

        sender.sendMessage(Component.text(main.getConfig().getString("messages.usage-adminauth",
                "§cComando errato, utilizza /adminauth per informazioni")));
        return true;
    }
}