package repository;

import config.DbConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnectionManager {
    private static final Logger LOGGER =
            Logger.getLogger(DatabaseConnectionManager.class.getName());
    private static final String DB_URL = DbConfig.URL;
    private static final String DB_USER = DbConfig.USER;
    private static final String DB_PASSWORD = DbConfig.PASSWORD;
    private static DatabaseConnectionManager instance;

    private DatabaseConnectionManager() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "PostgreSQL JDBC Driver not found.", e);
            throw new RuntimeException("Failed to load JDBC driver", e);
        }
    }

    public static synchronized DatabaseConnectionManager getInstance() {
        if (instance == null) {
            instance = new DatabaseConnectionManager();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        LOGGER.info("Attempting to get database connection.");
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                LOGGER.info("Database connection closed.");
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error closing database connection.",
                        e);
            }
        }
    }


}
