package com.taskmanager.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads configuration from external properties files.
 *
 * This class demonstrates externalized configuration - a pattern
 * that Spring Boot automates with @ConfigurationProperties and
 * automatic property binding.
 *
 * Configuration sources (in order of precedence):
 * 1. System properties (-Ddb.host=...)
 * 2. External file (./application.properties)
 * 3. Classpath resource (/application.properties)
 * 4. Default values
 */
public class ConfigLoader {
    private static final String DEFAULT_CONFIG_FILE = "application.properties";
    private static final String RESOURCES_CONFIG_FILE = "resources/application.properties";

    private final Properties properties;

    public ConfigLoader() {
        this.properties = new Properties();
        loadProperties();
    }

    public ConfigLoader(String configFilePath) {
        this.properties = new Properties();
        loadPropertiesFromFile(configFilePath);
    }

    private void loadProperties() {
        // Try loading from multiple locations
        boolean loaded = false;

        // 1. Try current directory
        loaded = loadPropertiesFromFile(DEFAULT_CONFIG_FILE);

        // 2. Try resources directory
        if (!loaded) {
            loaded = loadPropertiesFromFile(RESOURCES_CONFIG_FILE);
        }

        // 3. Try classpath
        if (!loaded) {
            loaded = loadPropertiesFromClasspath();
        }

        if (!loaded) {
            System.out.println("Warning: No configuration file found. Using default values.");
        }
    }

    private boolean loadPropertiesFromFile(String filePath) {
        try (InputStream input = new FileInputStream(filePath)) {
            properties.load(input);
            System.out.println("Loaded configuration from: " + filePath);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private boolean loadPropertiesFromClasspath() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(DEFAULT_CONFIG_FILE)) {
            if (input != null) {
                properties.load(input);
                System.out.println("Loaded configuration from classpath");
                return true;
            }
        } catch (IOException e) {
            // Ignore
        }
        return false;
    }

    /**
     * Get a property value with system property override support.
     */
    public String getProperty(String key) {
        // System properties take precedence
        String systemValue = System.getProperty(key);
        if (systemValue != null) {
            return systemValue;
        }
        return properties.getProperty(key);
    }

    /**
     * Get a property value with a default fallback.
     */
    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
    }

    /**
     * Get an integer property.
     */
    public int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Get a boolean property.
     */
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value.trim());
    }

    /**
     * Create DatabaseConfig from loaded properties.
     */
    public DatabaseConfig createDatabaseConfig() {
        return new DatabaseConfig(
            getProperty("db.host", "localhost"),
            getIntProperty("db.port", 3306),
            getProperty("db.name", "taskmanager"),
            getProperty("db.username", "root"),
            getProperty("db.password", "password"),
            getProperty("db.driver", "com.mysql.cj.jdbc.Driver")
        );
    }
}
