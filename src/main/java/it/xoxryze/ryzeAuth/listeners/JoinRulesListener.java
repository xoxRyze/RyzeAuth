package it.xoxryze.ryzeAuth.listeners;

import com.destroystokyo.paper.event.player.PlayerConnectionCloseEvent;
import io.papermc.paper.connection.PlayerConfigurationConnection;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.event.connection.configuration.AsyncPlayerConnectionConfigureEvent;
import io.papermc.paper.event.player.PlayerCustomClickEvent;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import it.xoxryze.ryzeAuth.RyzeAuth;
import it.xoxryze.ryzeAuth.utils.Palette;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class JoinRulesListener implements Listener {

    private final Map<UUID, CompletableFuture<Boolean>> awaitingResponse = new ConcurrentHashMap<>();
    private final RyzeAuth module;

    public JoinRulesListener(RyzeAuth module) {
        this.module = module;
    }

    @EventHandler
    void onPlayerConfigure(AsyncPlayerConnectionConfigureEvent event) {
        Dialog dialog = RegistryAccess.registryAccess().getRegistry(RegistryKey.DIALOG).get(Key.key("papermc:praise_paperchan"));
        if (dialog == null) {
            module.getLogger().severe("Dialog failed to launch.");
            return;
        }

        PlayerConfigurationConnection connection = event.getConnection();
        UUID uniqueId = connection.getProfile().getId();
        if (uniqueId == null) {
            return;
        }

        CompletableFuture<Boolean> response = new CompletableFuture<>();
        response.completeOnTimeout(false, 1, TimeUnit.MINUTES);

        awaitingResponse.put(uniqueId, response);

        Audience audience = connection.getAudience();
        audience.showDialog(dialog);

        if (!response.join()) {
            audience.sendMessage(Component.text(
                    "Hai §laccettato§r con successo il regolamento del server.", Palette.GREEN));
            audience.closeDialog();
            connection.disconnect(Component.text(
                    "Hai rifiutato le regole del server.", NamedTextColor.RED));
        }

        awaitingResponse.remove(uniqueId);
    }

    @EventHandler
    void onHandleDialog(PlayerCustomClickEvent event) {
        if (!(event.getCommonConnection() instanceof PlayerConfigurationConnection configurationConnection)) {
            return;
        }

        UUID uniqueId = configurationConnection.getProfile().getId();
        if (uniqueId == null) {
            return;
        }

        Key key = event.getIdentifier();
        if (key.equals(Key.key("papermc:paperchan/disagree"))) {
            setConnectionJoinResult(uniqueId, false);
        } else if (key.equals(Key.key("papermc:paperchan/agree"))) {
            setConnectionJoinResult(uniqueId, true);
        }
    }

    @EventHandler
    void onConnectionClose(PlayerConnectionCloseEvent event) {
        awaitingResponse.remove(event.getPlayerUniqueId());
    }

    private void setConnectionJoinResult(UUID uniqueId, boolean value) {
        CompletableFuture<Boolean> future = awaitingResponse.get(uniqueId);
        if (future != null) {
            future.complete(value);
        }
    }

}