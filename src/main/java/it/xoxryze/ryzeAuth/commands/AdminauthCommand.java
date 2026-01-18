package it.xoxryze.ryzeAuth.commands;

import it.xoxryze.ryzeAuth.RyzeAuth;
import it.xoxryze.ryzeAuth.database.tables.AuthTable;
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
                        .hoverEvent(HoverEvent.showText(Component.text("§7Change the password for a player"))));
                player.sendMessage(Component.text("/adminauth checkip <player>", Palette.AQUA)
                        .hoverEvent(HoverEvent.showText(Component.text("§7Check a user's IP"))));
                player.sendMessage(Component.text("/adminauth unregister <player>", Palette.AQUA)
                        .hoverEvent(HoverEvent.showText(Component.text("§7Unregister a player"))));
                player.sendMessage(Component.text("/adminauth register <player> <password>", Palette.AQUA)
                        .hoverEvent(HoverEvent.showText(Component.text("§7Force register a player"))));
                player.sendMessage(Component.text("/adminauth kick <player> <reason>", Palette.AQUA)
                        .hoverEvent(HoverEvent.showText(Component.text("§7Kick a player from the server"))));
                player.sendMessage(Component.text("/adminauth dupeip [<player> | <ip>]", Palette.AQUA)
                        .hoverEvent(HoverEvent.showText(Component.text("§7Check accounts linked to an IP"))));
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
                args[0].equalsIgnoreCase("kick") || args[0].equalsIgnoreCase("dupeip")
                || args[0].equalsIgnoreCase("reload")) {

            if (args[0].equalsIgnoreCase("changepassword")) {

                if (sender instanceof Player player) {
                    if (!Permission.hasPermission(player, "adminauth.changepassword")) {
                        player.sendMessage(Component.text(NO_PERMISSION));
                        return true;
                    }
                }

                if (args.length != 3) {
                    sender.sendMessage(Component.text(main.getConfig().getString(
                            "messages.usage-adminauth-changepassword",
                            "§cUse /adminauth changepassword <player> <password>")));
                    return true;
                }

                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

                if (!target.hasPlayedBefore()) {
                    sender.sendMessage(Component.text(PLAYER_NEVER_JOIN));
                    return true;
                }

                Optional<String> passwordOpt = db.getPlayerPassword(target).join();

                if (passwordOpt.isEmpty()) {
                    sender.sendMessage(Component.text(main.getConfig().getString(
                            "messages.player-never-authenticated",
                            "§cThe player never authenticated!")));
                    return true;
                }

                if (!PasswordUtils.isValidPassword(args[2], sender.getName())) {
                    sender.sendMessage(Component.text(UNSECURE_PASSWORD));
                    return true;
                }

                Integer lunghezzapw = args[2].length();

                if (lunghezzapw < PW_LENGTH_MIN) {
                    sender.sendMessage(Component.text(SHORT_PASSWORD));
                    return true;
                }

                if (lunghezzapw > PW_LENGTH_MAX) {
                    sender.sendMessage(Component.text(LONG_PASSWORD));
                    return true;
                }

                String hashed = PasswordUtils.hashPassword(args[2]);
                db.updatePlayerPassword(target, hashed);
                sender.sendMessage(Component.text(String.format("§aYou changed %s's password ", target.getName())));
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
                    sender.sendMessage(Component.text(main.getConfig().getString(
                            "messages.usage-adminauth-unregister",
                            "§cUse /adminauth unregister <player>")));
                    return true;
                }

                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

                if (!target.hasPlayedBefore()) {
                    sender.sendMessage(Component.text(PLAYER_NEVER_JOIN));
                    return true;
                }

                Optional<String> playerpw = String.valueOf(db.getPlayerPassword(target)).describeConstable();

                if (!playerpw.isPresent()) {
                    sender.sendMessage(Component.text(main.getConfig().getString(
                            "messages.player-never-authenticated",
                            "§cThe player never authenticated!")));
                    return true;
                }


                db.updatePlayerPassword(target, null);
                sender.sendMessage(Component.text("You have successfully unregistered §a" + target.getName(),
                        Palette.GREEN));
                if (target.isOnline()) {

                    if (main.getAuthenticated().contains(target.getUniqueId())) {
                        ((Player) target).kick(Component.text("You have been unregistered by a Staff Member", Palette.RED));
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
                    sender.sendMessage(Component.text(main.getConfig().getString(
                            "messages.usage-adminauth-register",
                            "§cUse /adminauth register <player> <password>")));
                    return true;
                }

                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

                if (!target.hasPlayedBefore()) {
                    sender.sendMessage(Component.text(PLAYER_NEVER_JOIN));
                    return true;
                }

                String playerpw = String.valueOf(db.getPlayerPassword(target));
                if (!PasswordUtils.isValidPassword(args[2].toLowerCase(), sender.getName())) {
                    sender.sendMessage(Component.text(UNSECURE_PASSWORD));
                    return true;
                }

                Integer lunghezzapw = args[2].length();

                if (lunghezzapw < PW_LENGTH_MIN) {
                    sender.sendMessage(Component.text(SHORT_PASSWORD));
                    return true;
                }

                if (lunghezzapw > PW_LENGTH_MAX) {
                    sender.sendMessage(Component.text(LONG_PASSWORD));
                    return true;
                }

                String hashed = PasswordUtils.hashPassword(args[2]);
                db.updatePlayerPassword(target, hashed);
                sender.sendMessage(Component.text("You have successfully registered §a" + target.getName(), Palette.GREEN));

                if (target.isOnline()) {
                    target = (Player) target;
                    main.getAuthenticated().add(target.getUniqueId());
                    ((Player) target).sendMessage(Component.text("You have been registered by a Staff Member"
                            , Palette.GREEN));
                }

                return true;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                if (sender instanceof Player player) {
                    if (!Permission.hasPermission(player, "adminauth.reload")) {
                        player.sendMessage(Component.text(NO_PERMISSION));
                        return true;
                    }
                }
                sender.sendMessage(Component.text("§aReloading configuration file..."));
                Long start = System.currentTimeMillis();
                main.reloadConfig();
                Long end = System.currentTimeMillis();
                sender.sendMessage(Component.text(String.format(main.getConfig().getString(
                        "messages.reload-success",
                        "§aReloaded configuration file succesfully in §2%s §ams"), end-start)));
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
                    sender.sendMessage(Component.text(main.getConfig().getString(
                            "messages.usage-adminauth-checkip",
                            "§cUse /adminauth checkip <player>")));
                    return true;
                }

                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

                if (target == null || !target.hasPlayedBefore()) {
                    sender.sendMessage(Component.text(PLAYER_NEVER_JOIN));
                    return true;
                }

                db.getPlayerAddress(target).thenAccept(playerip -> {
                    if (playerip.isEmpty()) {
                        sender.sendMessage(Component.text("An error occurred. (No Address)", Palette.RED));
                        return;
                    }

                    String status = "§cOffline";
                    if (target.isOnline()) {
                        status = "§aOnline";
                    }
                    sender.sendMessage(Component.empty());
                    sender.sendMessage(Component.text("Player: §f" + target.getName(), Palette.AQUA));
                    sender.sendMessage(Component.text("§rStatus: §f" + status, Palette.AQUA));
                    sender.sendMessage(Component.text("§rLast Address: §f" + playerip.get()
                            .replace("/", ""), Palette.AQUA));
                    sender.sendMessage(Component.empty());
                });
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
                    sender.sendMessage(Component.text(main.getConfig().getString(
                            "messages.usage-adminauth-kick",
                            "§cUse /adminauth kick <player> <reason>")));
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
                                                    "\n §aᴋɪᴄᴋ\n §f%player%§7 kicked §f%target%§7 from the server.")
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
                                                        "\n §aᴋɪᴄᴋ\n §f%player%§7 kicked §f%target%§7 from the server.")
                                                .replace("%player%", sendername)
                                                .replace("%target%", target.getName()))
                                .hoverEvent(HoverEvent.showText(Component.text("§7Reason §8» §f" + args[2]))));
                        return true;
                    }
                }

            }

            if (args[0].equalsIgnoreCase("dupeip")) {
                if (args.length != 2) {
                    sender.sendMessage(Component.text(main.getConfig().getString("messages.usage-adminauth-dupeip",
                            "§cUse /adminauth dupeip [<player> | <ip>]")));
                    return true;
                }

                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

                if (!target.hasPlayedBefore()) {
                    db.getAddress(args[1]).thenAccept(nicknames -> {
                        if (nicknames.isEmpty()) {
                            sender.sendMessage(Component.text("No results found.", Palette.RED));
                        } else {
                            sender.sendMessage(Component.text("§7Players found with the IP entered:\n §f" +
                                    String.join(", ", nicknames)));
                        }
                    });
                    return true;
                }

                db.getPlayerAddress(target).thenAccept(optionalIp -> {
                    if (optionalIp.isEmpty()) {
                        sender.sendMessage(Component.text("The player does not have a registered IP address.", Palette.RED));
                        return;
                    }

                    String playerIp = optionalIp.get();
                    db.getAddress(playerIp).thenAccept(nicknames -> {
                        if (nicknames.isEmpty()) {
                            sender.sendMessage(Component.text("No results found.", Palette.RED));
                        } else {
                            sender.sendMessage(Component.text("§7Players found with " + target.getName() + "'s IP:\n §f" +
                                    String.join(", ", nicknames)));
                        }
                    });
                });
                return true;
            }
            return true;
        }
        sender.sendMessage(Component.text(main.getConfig().getString("messages.usage-adminauth",
                "§cIncorrect command, use /adminauth for more information")));
        return true;
    }
}