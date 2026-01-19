import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages database connections.
 *
 * PAIN POINT: No connection pooling - creates new connection every time.
 * In production, this is inefficient. Connection pools (HikariCP, etc.)
 * solve this, but require additional setup.
 *
 * Spring Boot auto-configures connection pooling for you.
 */
public class ConnectionManager {
    private final DatabaseConfig config;

    public ConnectionManager(DatabaseConfig config) {
        this.config = config;
        loadDriver();
    }

    /**
     * Loads the MySQL JDBC driver.
     *
     * PAIN POINT: You need to know the exact driver class name
     * and handle the case where it's not found.
     */
    private void loadDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found. " +
                    "Make sure mysql-connector-j is in your classpath.", e);
        }
    }

    /**
     * Creates a new database connection.
     *
     * PAIN POINT: Every call creates a new connection.
     * Caller is responsible for closing it.
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                config.getUrl(),
                config.getUsername(),
                config.getPassword()
        );
    }
}
