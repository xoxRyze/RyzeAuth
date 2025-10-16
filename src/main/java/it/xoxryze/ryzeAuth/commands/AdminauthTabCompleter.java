package it.xoxryze.ryzeAuth.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AdminauthTabCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        List<String> completitions = new ArrayList<>();

        if (args.length == 1) {
            completitions.add("changepassword");
            completitions.add("unregister");
            completitions.add("register");
            completitions.add("checkip");
        }
        if (args.length == 2) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                completitions.add(p.getName());
            }
        }

        return completitions;
    }
}
