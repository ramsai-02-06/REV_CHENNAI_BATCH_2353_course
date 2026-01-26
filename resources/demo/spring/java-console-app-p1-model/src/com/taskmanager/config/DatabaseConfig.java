package com.taskmanager.config;

/**
 * Holds database configuration loaded from external properties.
 *
 * This class is now populated by ConfigLoader from the properties file,
 * eliminating hardcoded credentials in source code.
 *
 * In Spring Boot, this would be replaced by @ConfigurationProperties
 * with automatic binding to application.properties/yaml.
 */
public class DatabaseConfig {
    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    private final String driverClassName;

    public DatabaseConfig(String host, int port, String database,
                         String username, String password, String driverClassName) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return String.format("jdbc:mysql://%s:%d/%s?useSSL=false&serverTimezone=UTC",
                host, port, database);
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    @Override
    public String toString() {
        // Don't log password
        return String.format("DatabaseConfig{host='%s', port=%d, database='%s', user='%s'}",
                host, port, database, username);
    }
}
