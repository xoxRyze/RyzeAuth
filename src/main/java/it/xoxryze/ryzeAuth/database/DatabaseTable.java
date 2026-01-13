package it.xoxryze.ryzeAuth.database;

import it.xoxryze.ryzeAuth.utils.LogUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class DatabaseTable {

    protected final DatabaseManager db;
    protected final String tableQuery;

    protected DatabaseTable(DatabaseManager db, String tableQuery) {
        this.db = db;
        this.tableQuery = tableQuery;
        createTable();
    }

    private void createTable() {
        try (Connection connection = db.getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(tableQuery);
        } catch (SQLException e) {
            LogUtils.logError(e, "Unable to create table");
        }
    }
}