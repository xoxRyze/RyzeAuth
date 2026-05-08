package it.xoxryze.ryzeAuth.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MojangSessionAPI {

    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    public static CompletableFuture<Boolean> isPremium(String username) {
        return getOnlineUUID(username)
                .thenApply(uuid -> uuid != null);
    }

    public static CompletableFuture<UUID> getOnlineUUID(String username) {

        String url = "https://api.mojang.com/users/profiles/minecraft/" + username;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        return CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {

                    if (response.statusCode() != 200) {
                        return null;
                    }

                    try {

                        JsonObject json = JsonParser
                                .parseString(response.body())
                                .getAsJsonObject();

                        String rawId = json.get("id").getAsString();

                        return parseMojangUUID(rawId);

                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
    }

    public static CompletableFuture<Boolean> isAuthenticated(String username) {

        return getOnlineUUID(username)
                .thenCompose(uuid -> {

                    if (uuid == null) {
                        return CompletableFuture.completedFuture(false);
                    }

                    return verifySession(uuid);
                });
    }

    public static CompletableFuture<Boolean> verifySession(UUID uuid) {

        String cleanUUID = uuid.toString().replace("-", "");

        String url = "https://sessionserver.mojang.com/session/minecraft/profile/" + cleanUUID;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        return CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {

                    if (response.statusCode() != 200) {
                        return false;
                    }

                    try {

                        JsonObject json = JsonParser
                                .parseString(response.body())
                                .getAsJsonObject();

                        return json.has("id") && json.has("name");

                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return false;
                });
    }

    public static CompletableFuture<Boolean> matchesOnlineUUID(String username, UUID uuid) {

        return getOnlineUUID(username)
                .thenApply(mojangUUID -> {

                    if (mojangUUID == null) {
                        return false;
                    }

                    return mojangUUID.equals(uuid);
                });
    }

    private static UUID parseMojangUUID(String rawUUID) {

        String formatted = rawUUID.replaceFirst(
                "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
                "$1-$2-$3-$4-$5"
        );

        return UUID.fromString(formatted);
    }
}