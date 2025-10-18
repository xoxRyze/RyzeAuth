package it.xoxryze.ryzeAuth.utils;

import org.bukkit.entity.Player;

public enum Permission {;

    public static String permission = "ryzeauth.command";

    public static boolean hasPermission(Player player, String str) {
        return player.hasPermission("ryzeauth.command." + str);
    }
}
