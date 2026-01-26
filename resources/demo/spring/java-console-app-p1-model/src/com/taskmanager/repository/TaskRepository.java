package com.taskmanager.repository;

import com.taskmanager.config.ConnectionManager;
import com.taskmanager.exception.DataAccessException;
import com.taskmanager.model.Task;
import com.taskmanager.model.TaskStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Task entity using raw JDBC.
 *
 * This version includes comprehensive logging that demonstrates
 * the logging patterns that would be automated by AOP:
 *
 * LOGGING POINTS (for future AOP):
 * - Method entry with parameters
 * - SQL execution timing
 * - Method exit with return value
 * - Exception logging with context
 *
 * AOP ASPECT EQUIVALENT:
 * @Around("execution(* com.taskmanager.repository.*.*(..))")
 * Would automatically log:
 * - Entering {method} with args {args}
 * - Exiting {method} with result {result} in {duration}ms
 * - Exception in {method}: {exception}
 */
public class TaskRepository {
    private static final Logger logger = LogManager.getLogger(TaskRepository.class);

    private final ConnectionManager connectionManager;

    public TaskRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        logger.info("TaskRepository initialized");
    }

    /**
     * Save a new task to the database.
     *
     * Detects SQL constraint violations (like duplicate title) and wraps them
     * in DataAccessException with context for the service layer to translate.
     */
    public Task save(Task task) {
        logger.debug("Entering save() with task: title='{}', status={}",
                task.getTitle(), task.getStatus());

        String sql = "INSERT INTO tasks (title, description, status, created_at) VALUES (?, ?, ?, ?)";
        long startTime = System.currentTimeMillis();

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

            logger.debug("Executing SQL: {} with params: [{}, {}, {}, {}]",
                    sql, task.getTitle(), task.getDescription(),
                    task.getStatus(), task.getCreatedAt());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new DataAccessException("Creating task failed, no rows affected");
            }

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                task.setId(rs.getLong(1));
            }

            long duration = System.currentTimeMillis() - startTime;
            logger.info("Exiting save() - created task id={} in {}ms", task.getId(), duration);

            return task;

        } catch (SQLException e) {
            long duration = System.currentTimeMillis() - startTime;
            logger.error("Exception in save() after {}ms: SQLState={}, ErrorCode={}, Message={}",
                    duration, e.getSQLState(), e.getErrorCode(), e.getMessage());

            // Wrap SQLException with context for service layer
            throw new DataAccessException(
                    "Error saving task: " + e.getMessage(),
                    e.getSQLState(),
                    e.getErrorCode(),
                    e
            );
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
        logger.debug("Entering findById() with id={}", id);

        String sql = "SELECT * FROM tasks WHERE id = ?";
        long startTime = System.currentTimeMillis();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = connectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setLong(1, id);

            logger.debug("Executing SQL: {} with params: [{}]", sql, id);

            rs = ps.executeQuery();

            Optional<Task> result;
            if (rs.next()) {
                result = Optional.of(mapRowToTask(rs));
            } else {
                result = Optional.empty();
            }

            long duration = System.currentTimeMillis() - startTime;
            logger.debug("Exiting findById() - found={} in {}ms", result.isPresent(), duration);

            return result;

        } catch (SQLException e) {
            long duration = System.currentTimeMillis() - startTime;
            logger.error("Exception in findById() after {}ms: {}", duration, e.getMessage());
            throw new DataAccessException("Error finding task by id: " + id, e);
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
        logger.debug("Entering findAll()");

        String sql = "SELECT * FROM tasks ORDER BY created_at DESC";
        long startTime = System.currentTimeMillis();

        List<Task> tasks = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = connectionManager.getConnection();
            ps = conn.prepareStatement(sql);

            logger.debug("Executing SQL: {}", sql);

            rs = ps.executeQuery();

            while (rs.next()) {
                tasks.add(mapRowToTask(rs));
            }

            long duration = System.currentTimeMillis() - startTime;
            logger.info("Exiting findAll() - found {} tasks in {}ms", tasks.size(), duration);

            return tasks;

        } catch (SQLException e) {
            long duration = System.currentTimeMillis() - startTime;
            logger.error("Exception in findAll() after {}ms: {}", duration, e.getMessage());
            throw new DataAccessException("Error finding all tasks", e);
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
        logger.debug("Entering update() with task: id={}, title='{}', status={}",
                task.getId(), task.getTitle(), task.getStatus());

        String sql = "UPDATE tasks SET title = ?, description = ?, status = ? WHERE id = ?";
        long startTime = System.currentTimeMillis();

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = connectionManager.getConnection();
            ps = conn.prepareStatement(sql);

            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setString(3, task.getStatus().name());
            ps.setLong(4, task.getId());

            logger.debug("Executing SQL: {} with params: [{}, {}, {}, {}]",
                    sql, task.getTitle(), task.getDescription(), task.getStatus(), task.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("Updating task failed, no rows affected. Task may not exist.");
            }

            long duration = System.currentTimeMillis() - startTime;
            logger.info("Exiting update() - updated task id={} in {}ms", task.getId(), duration);

            return task;

        } catch (SQLException e) {
            long duration = System.currentTimeMillis() - startTime;
            logger.error("Exception in update() after {}ms: SQLState={}, ErrorCode={}, Message={}",
                    duration, e.getSQLState(), e.getErrorCode(), e.getMessage());

            throw new DataAccessException(
                    "Error updating task: " + task.getId(),
                    e.getSQLState(),
                    e.getErrorCode(),
                    e
            );
        } finally {
            closeQuietly(ps);
            closeQuietly(conn);
        }
    }

    /**
     * Delete a task by ID.
     */
    public boolean deleteById(Long id) {
        logger.debug("Entering deleteById() with id={}", id);

        String sql = "DELETE FROM tasks WHERE id = ?";
        long startTime = System.currentTimeMillis();

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = connectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setLong(1, id);

            logger.debug("Executing SQL: {} with params: [{}]", sql, id);

            int affectedRows = ps.executeUpdate();

            long duration = System.currentTimeMillis() - startTime;
            boolean deleted = affectedRows > 0;
            logger.info("Exiting deleteById() - deleted={} in {}ms", deleted, duration);

            return deleted;

        } catch (SQLException e) {
            long duration = System.currentTimeMillis() - startTime;
            logger.error("Exception in deleteById() after {}ms: {}", duration, e.getMessage());
            throw new DataAccessException("Error deleting task: " + id, e);
        } finally {
            closeQuietly(ps);
            closeQuietly(conn);
        }
    }

    /**
     * Find tasks by status.
     */
    public List<Task> findByStatus(TaskStatus status) {
        logger.debug("Entering findByStatus() with status={}", status);

        String sql = "SELECT * FROM tasks WHERE status = ? ORDER BY created_at DESC";
        long startTime = System.currentTimeMillis();

        List<Task> tasks = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = connectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status.name());

            logger.debug("Executing SQL: {} with params: [{}]", sql, status);

            rs = ps.executeQuery();

            while (rs.next()) {
                tasks.add(mapRowToTask(rs));
            }

            long duration = System.currentTimeMillis() - startTime;
            logger.info("Exiting findByStatus() - found {} tasks in {}ms", tasks.size(), duration);

            return tasks;

        } catch (SQLException e) {
            long duration = System.currentTimeMillis() - startTime;
            logger.error("Exception in findByStatus() after {}ms: {}", duration, e.getMessage());
            throw new DataAccessException("Error finding tasks by status: " + status, e);
        } finally {
            closeQuietly(rs);
            closeQuietly(ps);
            closeQuietly(conn);
        }
    }

    /**
     * Check if a task with the given title already exists.
     */
    public boolean existsByTitle(String title) {
        logger.debug("Entering existsByTitle() with title='{}'", title);

        String sql = "SELECT COUNT(*) FROM tasks WHERE title = ?";
        long startTime = System.currentTimeMillis();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = connectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, title);

            rs = ps.executeQuery();
            boolean exists = rs.next() && rs.getInt(1) > 0;

            long duration = System.currentTimeMillis() - startTime;
            logger.debug("Exiting existsByTitle() - exists={} in {}ms", exists, duration);

            return exists;

        } catch (SQLException e) {
            long duration = System.currentTimeMillis() - startTime;
            logger.error("Exception in existsByTitle() after {}ms: {}", duration, e.getMessage());
            throw new DataAccessException("Error checking task existence by title", e);
        } finally {
            closeQuietly(rs);
            closeQuietly(ps);
            closeQuietly(conn);
        }
    }

    /**
     * Maps a database row to a Task object.
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
     */
    private void closeQuietly(AutoCloseable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception e) {
                logger.warn("Error closing resource: {}", e.getMessage());
            }
        }
    }
}
