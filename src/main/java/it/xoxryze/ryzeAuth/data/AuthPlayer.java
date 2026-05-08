package it.xoxryze.ryzeAuth.data;

import java.util.UUID;

public class AuthPlayer {

    private final UUID uuid;
    private final boolean isPremium;

    public AuthPlayer(UUID uuid, boolean isPremium) {
        this.uuid = uuid;
        this.isPremium = isPremium;
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean isPremium() {
        return isPremium;
    }
}
