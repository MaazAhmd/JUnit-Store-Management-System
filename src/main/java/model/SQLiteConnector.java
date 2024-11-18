package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnector {
    static final String databaseURL = "jdbc:sqlite:./swing_store_database.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(databaseURL);
    }
}
