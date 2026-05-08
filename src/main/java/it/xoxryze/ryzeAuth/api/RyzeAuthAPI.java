package it.xoxryze.ryzeAuth.api;

import it.xoxryze.ryzeAuth.RyzeAuth;
import it.xoxryze.ryzeAuth.data.AuthPlayer;
import it.xoxryze.ryzeAuth.utils.MojangSessionAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class RyzeAuthAPI {

    private final RyzeAuth main;

    public RyzeAuthAPI(RyzeAuth main) {
        this.main = main;
    }

    public CompletableFuture<Boolean> isPremium(Player player) {
        return MojangSessionAPI.isAuthenticated(player.getName());
    }

    public boolean isAuthenticated(Player player) {
        return main.getCacheManager().isAuthenticated(player);
    }

    public boolean addAuthenticate(Player player) {
        return main.getAuthenticated().add(new AuthPlayer(player.getUniqueId(), false));
    }

    public boolean removeAuthenticate(Player player) {
        main.getCacheManager().removeAuthPlayer(player);
        return true;
    }

    public CompletableFuture<Optional<String>> getLastAddress(OfflinePlayer player) {
        return main.getAuthTable().getPlayerAddress(player);
    }

    public CompletableFuture<Optional<String>> isRegistered(OfflinePlayer player) {
        return main.getAuthTable().isRegistered(player);
    }

}