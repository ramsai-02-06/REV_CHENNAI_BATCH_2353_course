/**
 * Holds database configuration.
 *
 * PAIN POINT: Configuration is hardcoded in Java code.
 * To change the database, you need to:
 * 1. Modify this class (or where it's instantiated)
 * 2. Recompile the application
 * 3. Redeploy
 *
 * In later stages, we'll externalize this to properties files.
 */
public class DatabaseConfig {
    private final String host;
    private final String database;
    private final String username;
    private final String password;
    private final int port;

    public DatabaseConfig(String host, String database, String username, String password) {
        this(host, 3306, database, username, password);
    }

    public DatabaseConfig(String host, int port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public String getUrl() {
        return String.format("jdbc:mysql://%s:%d/%s", host, port, database);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    public String getDatabase() {
        return database;
    }

    public int getPort() {
        return port;
    }
}
