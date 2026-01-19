package com.example.taskmanager.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages database connections.
 *
 * NEW IN STAGE 3:
 * - @Component marks this as a Spring-managed bean
 * - @Value injects properties from db.properties
 *
 * Spring creates ONE instance and injects it wherever needed.
 */
@Component
public class ConnectionManager {

    private final String url;
    private final String username;
    private final String password;

    /**
     * Constructor with @Value injection.
     *
     * Spring reads db.properties and injects values automatically!
     * No more manual properties loading.
     */
    public ConnectionManager(
            @Value("${db.host}") String host,
            @Value("${db.port}") String port,
            @Value("${db.name}") String dbName,
            @Value("${db.username}") String username,
            @Value("${db.password}") String password) {

        this.url = String.format("jdbc:mysql://%s:%s/%s", host, port, dbName);
        this.username = username;
        this.password = password;

        loadDriver();
        System.out.println("ConnectionManager initialized by Spring");
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
