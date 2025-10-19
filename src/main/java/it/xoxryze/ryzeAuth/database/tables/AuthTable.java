package it.xoxryze.ryzeAuth.database.tables;

import it.xoxryze.ryzeAuth.database.DatabaseManager;
import it.xoxryze.ryzeAuth.database.DatabaseTable;
import it.xoxryze.ryzeAuth.utils.LogUtils;
import org.bukkit.OfflinePlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class AuthTable extends DatabaseTable {

    private static final String AUTH_TABLE = """
            CREATE TABLE IF NOT EXISTS auth
            (
                uuid            VARCHAR(36)         PRIMARY KEY,
                nickname        VARCHAR(20)         NOT NULL,
                password        TEXT,
                ip              VARCHAR(120)
            );
            """;

    private static final String INSERT_AUTH = """
            INSERT INTO auth(uuid, nickname)
            VALUES (?, ?);
            """;

    private static final String CHECK_PLAYER = """
            SELECT * FROM auth
            WHERE uuid = ?;
            """;

    private static final String SELECT_PASSWORD = """
            SELECT password
            FROM auth WHERE uuid = ?;
            """;

    private static final String SELECT_NICKS_BY_IP = """
        SELECT nickname FROM auth
        WHERE ip = ?;
        """;

    private static final String SELECT_IP = """
            SELECT ip FROM auth
            WHERE uuid = ?;
            """;

    private static final String UPDATE_ADDRESS = """
            UPDATE players
            SET ip = ?
            WHERE uuid = ?;
            """;

    private static final String UPDATE_PASSWORD = """
            UPDATE players
            SET password = ?
            WHERE uuid = ?;
            """;

    public AuthTable(DatabaseManager db) {
        super(db, AUTH_TABLE);
    }

    public CompletableFuture<Boolean> addPlayer(OfflinePlayer p) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = db.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(INSERT_AUTH)) {

                preparedStatement.setString(1, p.getUniqueId().toString());
                preparedStatement.setString(2, p.getName());

                return preparedStatement.executeUpdate() > 0;

            } catch (SQLException e) {
                LogUtils.logError(e, "Unable to add player " + p.getName());
            }

            return false;
        });
    }

    public CompletableFuture<Boolean> playerExists(OfflinePlayer player) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = db.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(CHECK_PLAYER)) {

                preparedStatement.setString(1, player.getUniqueId().toString());
                ResultSet resultSet = preparedStatement.executeQuery();
                return resultSet.next();

            } catch (SQLException e) {
                LogUtils.logError(e, "Unable to check if player " + player.getName() + " exists");
            }
            return false;
        });
    }

    public CompletableFuture<Optional<String>> getPlayerPassword(OfflinePlayer player) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = db.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PASSWORD)) {

                preparedStatement.setString(1, player.getUniqueId().toString());

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) return Optional.ofNullable(resultSet.getString("password"));

            } catch (SQLException e) {
                LogUtils.logError(e, "Unable to get the password of player " + player.getName());
            }

            return Optional.empty();
        });
    }

    public CompletableFuture<Optional<String>> getPlayerAddress(OfflinePlayer player) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = db.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(SELECT_IP)) {

                preparedStatement.setString(1, player.getUniqueId().toString());

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) return Optional.ofNullable(resultSet.getString("ip"));

            } catch (SQLException e) {
                LogUtils.logError(e, "Unable to get the address of player " + player.getName());
            }

            return Optional.empty();
        });
    }

    public CompletableFuture<List<String>> getAddress(String address) {
        return CompletableFuture.supplyAsync(() -> {
            List<String> nicknames = new ArrayList<>();

            try (Connection connection = db.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(SELECT_NICKS_BY_IP)) {

                preparedStatement.setString(1, address);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    nicknames.add(resultSet.getString("nickname"));
                }

            } catch (SQLException e) {
                LogUtils.logError(e, "Unable to get nicknames for IP address " + address);
            }

            return nicknames;
        });
    }

    public CompletableFuture<Boolean> updatePlayerAddress(OfflinePlayer player, String ip) {
        return CompletableFuture.supplyAsync(() -> {

            try (Connection connection = db.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ADDRESS)) {

                preparedStatement.setString(1, ip);
                preparedStatement.setString(2, player.getUniqueId().toString());

                return preparedStatement.executeUpdate() > 0;

            } catch (SQLException e) {
                LogUtils.logError(e, "Unable to update address for player " + player.getName());
            }

            return false;
        });
    }

    public CompletableFuture<Boolean> updatePlayerPassword(OfflinePlayer player, String password) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = db.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PASSWORD)) {

                preparedStatement.setString(1, password);
                preparedStatement.setString(2, player.getUniqueId().toString());

                return preparedStatement.executeUpdate() > 0;

            } catch (SQLException e) {
                LogUtils.logError(e, "Unable to update player's " + player.getName() + " password");
            }

            return false;
        });
    }
}
