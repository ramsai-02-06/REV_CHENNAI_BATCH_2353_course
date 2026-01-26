package com.taskmanager.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages database connections.
 *
 * This version includes logging for debugging connection issues.
 * In Spring Boot, connection management is handled automatically
 * by the DataSource auto-configuration and connection pooling.
 *
 * FUTURE AOP ENHANCEMENT:
 * Connection management could be wrapped with aspects for:
 * - Performance monitoring (track connection acquisition time)
 * - Connection leak detection
 * - Automatic retry on transient failures
 */
public class ConnectionManager {
    private static final Logger logger = LogManager.getLogger(ConnectionManager.class);

    private final DatabaseConfig config;

    public ConnectionManager(DatabaseConfig config) {
        this.config = config;
        loadDriver();
        logger.info("ConnectionManager initialized with config: {}", config);
    }

    /**
     * Loads the JDBC driver.
     */
    private void loadDriver() {
        try {
            logger.debug("Loading JDBC driver: {}", config.getDriverClassName());
            Class.forName(config.getDriverClassName());
            logger.info("JDBC driver loaded successfully");
        } catch (ClassNotFoundException e) {
            logger.error("Failed to load JDBC driver: {}", config.getDriverClassName(), e);
            throw new RuntimeException("JDBC Driver not found: " + config.getDriverClassName() +
                    ". Make sure the driver JAR is in your classpath.", e);
        }
    }

    /**
     * Creates a new database connection.
     *
     * LOG POINTS (for future AOP):
     * - BEFORE: Log connection request
     * - AFTER_RETURNING: Log successful connection
     * - AFTER_THROWING: Log connection failure
     */
    public Connection getConnection() throws SQLException {
        logger.debug("Requesting database connection to: {}", config.getUrl());

        long startTime = System.currentTimeMillis();
        try {
            Connection connection = DriverManager.getConnection(
                    config.getUrl(),
                    config.getUsername(),
                    config.getPassword()
            );

            long duration = System.currentTimeMillis() - startTime;
            logger.debug("Connection established in {}ms", duration);

            return connection;

        } catch (SQLException e) {
            long duration = System.currentTimeMillis() - startTime;
            logger.error("Failed to establish connection after {}ms: {}", duration, e.getMessage());
            throw e;
        }
    }

    /**
     * Test the database connection.
     */
    public boolean testConnection() {
        logger.info("Testing database connection...");
        try (Connection conn = getConnection()) {
            boolean valid = conn.isValid(5);
            if (valid) {
                logger.info("Database connection test successful");
            } else {
                logger.warn("Database connection test failed - connection invalid");
            }
            return valid;
        } catch (SQLException e) {
            logger.error("Database connection test failed: {}", e.getMessage());
            return false;
        }
    }
}
