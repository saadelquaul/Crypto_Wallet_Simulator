package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.EnvLoader;

public class DatabaseConnectionManager {
    private static final Logger LOGGER =
            Logger.getLogger(DatabaseConnectionManager.class.getName());
    private static  final String DB_URL = EnvLoader.get("DB_URL");
    private static final String DB_USER = EnvLoader.get("DB_USER");
    private static final String DB_PASSWORD = EnvLoader.get("DB_PASSWORD")    ;
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
        if (DB_URL == null) {
            throw new IllegalStateException("DB_URL not found in environment variables or .env file");
        }
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
