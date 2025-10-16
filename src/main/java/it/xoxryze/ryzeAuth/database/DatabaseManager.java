package it.xoxryze.ryzeAuth.database;

import org.bukkit.OfflinePlayer;

import java.sql.*;

public class DatabaseManager {

    private final Connection connection;

    public DatabaseManager(String path) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                   CREATE TABLE IF NOT EXISTS players (
                   uuid TEXT PRIMARY KEY,
                   nickname TEXT NOT NULL,
                   password TEXT,
                   ip TEXT)
                   """);
        }
    }

    public void closeConnection() throws SQLException {
        if (connection != null & !connection.isClosed()) {
            connection.close();
        }
    }

    public void addPlayer(OfflinePlayer p) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO players (uuid, nickname) VALUES (?, ?)")) {
            preparedStatement.setString(1, p.getUniqueId().toString());
            preparedStatement.setString(2, p.getName());
            preparedStatement.executeUpdate();
        }
    }

    public boolean playerExists(OfflinePlayer player) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM players WHERE uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }

    public String getPlayerPassword (OfflinePlayer player) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT password FROM players WHERE uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("password");
            } else {
                return null;
            }
        }
    }

    public String getPlayerAdress (OfflinePlayer player) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT ip FROM players WHERE uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("ip");
            } else {
                return null;
            }
        }
    }

    public void updatePlayerAdress (OfflinePlayer player, String ip) throws SQLException{

        if (!playerExists(player)) {
            addPlayer(player);
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE players SET ip = ? WHERE uuid = ?")) {
            preparedStatement.setString(1, ip);
            preparedStatement.setString(2, player.getUniqueId().toString());
            preparedStatement.executeUpdate();
        }
    }

    public void updatePlayerPassword(OfflinePlayer player, String password) throws SQLException{

        if (!playerExists(player)) {
            addPlayer(player);
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE players SET password = ? WHERE uuid = ?")) {
            preparedStatement.setString(1, password);
            preparedStatement.setString(2, player.getUniqueId().toString());
            preparedStatement.executeUpdate();
        }
    }

}
