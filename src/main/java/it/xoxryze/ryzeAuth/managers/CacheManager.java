package it.xoxryze.ryzeAuth.managers;

import it.xoxryze.ryzeAuth.data.AuthPlayer;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class CacheManager {

    private final List<AuthPlayer> authenticated;

    public CacheManager() {
        this.authenticated = new CopyOnWriteArrayList<>();
    }

    public boolean isAuthenticated(Player player) {
        for (AuthPlayer authPlayer : authenticated) {
            if (authPlayer.getUuid().toString().equals(player.getUniqueId().toString())) {
                return true;
            }
        }
        return false;
    }

    public void removeAuthPlayer(Player player) {
        getAuthPlayer(player).ifPresent(authenticated::remove);
    }

    public Optional<AuthPlayer> getAuthPlayer(Player player) {
        return authenticated.stream()
                .filter(authPlayer -> authPlayer.getUuid().toString().equals(player.getUniqueId().toString()))
                .findFirst();
    }

    public List<AuthPlayer> getAuthenticated() {
        return authenticated;
    }
}
