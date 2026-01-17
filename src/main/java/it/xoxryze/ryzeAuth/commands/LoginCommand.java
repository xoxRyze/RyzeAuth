package it.xoxryze.ryzeAuth.commands;

import it.xoxryze.ryzeAuth.RyzeAuth;
import it.xoxryze.ryzeAuth.database.tables.AuthTable;
import it.xoxryze.ryzeAuth.utils.PasswordUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static it.xoxryze.ryzeAuth.managers.ConfigManager.*;

public class LoginCommand implements CommandExecutor {

    private final AuthTable db;
    private final RyzeAuth main;

    public LoginCommand(AuthTable db, RyzeAuth main) {
        this.db = db;
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (!(sender instanceof Player player)) {
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(Component.text(main.getConfig().getString("messages.usage-login",
                    "§cUse /login <password>")));
            return true;
        }

        CompletableFuture<Optional<String>> passwordFuture = db.getPlayerPassword(player);

        passwordFuture.thenAccept(playerPasswordOpt -> {
            if (main.getAuthenticated().contains(player.getUniqueId())) {
                player.sendMessage(Component.text(ALREADY_AUTHENTICATED));
                return;
            }

            if (playerPasswordOpt.isEmpty()) {
                player.sendMessage(Component.text(NOT_REGISTERED));
                return;
            }

            String hashedPassword = playerPasswordOpt.get();

            if (hashedPassword == null || hashedPassword.trim().isEmpty()) {
                player.sendMessage(Component.text(NOT_REGISTERED));
                return;
            }

            if (PasswordUtils.checkPassword(args[0], hashedPassword)) {
                player.sendMessage(Component.text(main.getConfig().getString("messages.success-login",
                        "§aYou have successfully logged in!")));
                main.getAuthenticated().add(player.getUniqueId());
                db.updatePlayerAddress(player, player.getAddress().toString());
            } else {
                player.sendMessage(Component.text(PASSWORD_UNCORRECT));
            }
        }).exceptionally(throwable -> {
            player.sendMessage(Component.text("§cAn error occurred while logging in. Retry later."));
            throwable.printStackTrace();
            return null;
        });

        return true;
    }
}