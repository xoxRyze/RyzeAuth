package it.xoxryze.ryzeAuth.commands;

import it.xoxryze.ryzeAuth.utils.Permission;
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

        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            String partial = args[0].toLowerCase();
            if ("changepassword".startsWith(partial)) completions.add("changepassword");
            if ("unregister".startsWith(partial)) completions.add("unregister");
            if ("register".startsWith(partial)) completions.add("register");
            if ("checkip".startsWith(partial)) completions.add("checkip");
            if ("kick".startsWith(partial)) completions.add("kick");
            if ("dupeip".startsWith(partial)) completions.add("dupeip");
        }
        if (args.length == 2) {
            String partial = args[1].toLowerCase();
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().startsWith(partial)) completions.add(p.getName());
            }
        }

        return completions;
    }
}