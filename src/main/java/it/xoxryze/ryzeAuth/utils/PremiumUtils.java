package it.xoxryze.ryzeAuth.utils;

import org.bukkit.entity.Player;

import java.util.UUID;

public class PremiumUtils {

    public static boolean isPremium(Player player) {
        return player.getPlayerProfile().getTextures().getSkin() != null;
    }

}
