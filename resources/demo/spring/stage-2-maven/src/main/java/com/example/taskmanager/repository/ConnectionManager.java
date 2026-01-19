package com.example.taskmanager.repository;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Manages database connections.
 *
 * IMPROVEMENT: Configuration loaded from properties file instead of hardcoded.
 * STILL PAINFUL: No connection pooling, manual connection management.
 */
public class ConnectionManager {
    private final String url;
    private final String username;
    private final String password;

    public ConnectionManager() {
        Properties props = loadProperties();
        String host = props.getProperty("db.host", "localhost");
        String port = props.getProperty("db.port", "3306");
        String dbName = props.getProperty("db.name", "taskmanager");

        this.url = String.format("jdbc:mysql://%s:%s/%s", host, port, dbName);
        this.username = props.getProperty("db.username", "root");
        this.password = props.getProperty("db.password", "password");

        loadDriver();
    }

    private Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (input != null) {
                props.load(input);
            } else {
                System.err.println("Warning: db.properties not found, using defaults");
            }
        } catch (IOException e) {
            System.err.println("Warning: Error loading db.properties: " + e.getMessage());
        }
        return props;
    }

    private void loadDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
