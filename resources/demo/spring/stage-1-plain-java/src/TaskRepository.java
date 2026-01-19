import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Task entity using raw JDBC.
 *
 * PAIN POINTS IN THIS CLASS:
 * 1. Every method has the same try-catch-finally boilerplate
 * 2. Manual ResultSet to Object mapping (repeated in every find method)
 * 3. Manual resource management (close connection, statement, resultset)
 * 4. SQL strings scattered throughout the code
 * 5. SQLException handling is verbose
 *
 * Count the lines: This class is ~150 lines for basic CRUD.
 * In Stage 5 with Spring Data JPA, it will be ~10 lines.
 */
public class TaskRepository {
    private final ConnectionManager connectionManager;

    public TaskRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * Save a new task to the database.
     *
     * Notice the boilerplate:
     * - Get connection
     * - Create prepared statement
     * - Set parameters one by one
     * - Execute
     * - Get generated keys
     * - Handle exceptions
     * - Close resources in finally
     */
    public Task save(Task task) {
        String sql = "INSERT INTO tasks (title, description, status, created_at) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = connectionManager.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setString(3, task.getStatus().name());
            ps.setTimestamp(4, Timestamp.valueOf(task.getCreatedAt()));

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating task failed, no rows affected.");
            }

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                task.setId(rs.getLong(1));
            }

            return task;

        } catch (SQLException e) {
            throw new RuntimeException("Error saving task", e);
        } finally {
            closeQuietly(rs);
            closeQuietly(ps);
            closeQuietly(conn);
        }
    }

    /**
     * Find a task by ID.
     */
    public Optional<Task> findById(Long id) {
        String sql = "SELECT * FROM tasks WHERE id = ?";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = connectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setLong(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(mapRowToTask(rs));
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error finding task by id: " + id, e);
        } finally {
            closeQuietly(rs);
            closeQuietly(ps);
            closeQuietly(conn);
        }
    }

    /**
     * Find all tasks.
     */
    public List<Task> findAll() {
        String sql = "SELECT * FROM tasks ORDER BY created_at DESC";
        List<Task> tasks = new ArrayList<>();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = connectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                tasks.add(mapRowToTask(rs));
            }
            return tasks;

        } catch (SQLException e) {
            throw new RuntimeException("Error finding all tasks", e);
        } finally {
            closeQuietly(rs);
            closeQuietly(ps);
            closeQuietly(conn);
        }
    }

    /**
     * Update an existing task.
     */
    public Task update(Task task) {
        String sql = "UPDATE tasks SET title = ?, description = ?, status = ? WHERE id = ?";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = connectionManager.getConnection();
            ps = conn.prepareStatement(sql);

            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setString(3, task.getStatus().name());
            ps.setLong(4, task.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating task failed, no rows affected.");
            }

            return task;

        } catch (SQLException e) {
            throw new RuntimeException("Error updating task: " + task.getId(), e);
        } finally {
            closeQuietly(ps);
            closeQuietly(conn);
        }
    }

    /**
     * Delete a task by ID.
     */
    public void deleteById(Long id) {
        String sql = "DELETE FROM tasks WHERE id = ?";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = connectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting task: " + id, e);
        } finally {
            closeQuietly(ps);
            closeQuietly(conn);
        }
    }

    /**
     * Find tasks by status.
     */
    public List<Task> findByStatus(TaskStatus status) {
        String sql = "SELECT * FROM tasks WHERE status = ? ORDER BY created_at DESC";
        List<Task> tasks = new ArrayList<>();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = connectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status.name());
            rs = ps.executeQuery();

            while (rs.next()) {
                tasks.add(mapRowToTask(rs));
            }
            return tasks;

        } catch (SQLException e) {
            throw new RuntimeException("Error finding tasks by status: " + status, e);
        } finally {
            closeQuietly(rs);
            closeQuietly(ps);
            closeQuietly(conn);
        }
    }

    /**
     * Maps a database row to a Task object.
     *
     * PAIN POINT: This mapping code is manual and error-prone.
     * Column names are strings - typos cause runtime errors.
     * JPA/Hibernate handles this automatically.
     */
    private Task mapRowToTask(ResultSet rs) throws SQLException {
        return new Task(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("description"),
                TaskStatus.valueOf(rs.getString("status")),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }

    /**
     * Utility method to close resources without throwing exceptions.
     *
     * PAIN POINT: We need this helper to avoid nested try-catch in finally blocks.
     */
    private void closeQuietly(AutoCloseable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception e) {
                // Swallow exception - we're already in cleanup
                System.err.println("Warning: Error closing resource: " + e.getMessage());
            }
        }
    }
}
