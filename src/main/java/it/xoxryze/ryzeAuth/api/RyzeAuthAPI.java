package it.xoxryze.ryzeAuth.api;

import it.xoxryze.ryzeAuth.RyzeAuth;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class RyzeAuthAPI {

    private final RyzeAuth main;

    public RyzeAuthAPI(RyzeAuth main) {
        this.main = main;
    }

    public boolean isAuthenticated(Player player) {
        return main.getAuthenticated().contains(player.getUniqueId());
    }

    public boolean addAuthenticate(Player player) {
        return main.getAuthenticated().add(player.getUniqueId());
    }

    public boolean removeAuthenticate(Player player) {
        return main.getAuthenticated().remove(player.getUniqueId());
    }

    public CompletableFuture<Optional<String>> getLastAddress(OfflinePlayer player) {
        return main.getAuthTable().getPlayerAddress(player);
    }
}
