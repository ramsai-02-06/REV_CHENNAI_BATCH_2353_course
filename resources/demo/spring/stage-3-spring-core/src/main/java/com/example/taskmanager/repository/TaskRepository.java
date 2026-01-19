package com.example.taskmanager.repository;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Task entity.
 *
 * NEW IN STAGE 3:
 * - @Repository marks this as a Spring-managed data access bean
 * - @Autowired on constructor tells Spring to inject ConnectionManager
 *
 * The JDBC boilerplate is still here - Stage 5 (Spring Data JPA) fixes that.
 */
@Repository
public class TaskRepository {

    private final ConnectionManager connectionManager;

    /**
     * Constructor Injection - the recommended approach.
     *
     * Spring sees this constructor, finds a ConnectionManager bean,
     * and passes it in automatically.
     *
     * Note: @Autowired is optional on single-constructor classes in Spring 4.3+
     * but we include it for clarity.
     */
    @Autowired
    public TaskRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        System.out.println("TaskRepository initialized by Spring");
    }

    public Task save(Task task) {
        String sql = "INSERT INTO tasks (title, description, status, created_at) VALUES (?, ?, ?, ?)";

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setString(3, task.getStatus().name());
            ps.setTimestamp(4, Timestamp.valueOf(task.getCreatedAt()));

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    task.setId(rs.getLong(1));
                }
            }
            return task;

        } catch (SQLException e) {
            throw new RuntimeException("Error saving task", e);
        }
    }

    public Optional<Task> findById(Long id) {
        String sql = "SELECT * FROM tasks WHERE id = ?";

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToTask(rs));
                }
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error finding task", e);
        }
    }

    public List<Task> findAll() {
        String sql = "SELECT * FROM tasks ORDER BY created_at DESC";
        List<Task> tasks = new ArrayList<>();

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                tasks.add(mapRowToTask(rs));
            }
            return tasks;

        } catch (SQLException e) {
            throw new RuntimeException("Error finding all tasks", e);
        }
    }

    public Task update(Task task) {
        String sql = "UPDATE tasks SET title = ?, description = ?, status = ? WHERE id = ?";

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setString(3, task.getStatus().name());
            ps.setLong(4, task.getId());

            ps.executeUpdate();
            return task;

        } catch (SQLException e) {
            throw new RuntimeException("Error updating task", e);
        }
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM tasks WHERE id = ?";

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting task", e);
        }
    }

    public List<Task> findByStatus(TaskStatus status) {
        String sql = "SELECT * FROM tasks WHERE status = ? ORDER BY created_at DESC";
        List<Task> tasks = new ArrayList<>();

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status.name());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapRowToTask(rs));
                }
            }
            return tasks;

        } catch (SQLException e) {
            throw new RuntimeException("Error finding tasks by status", e);
        }
    }

    private Task mapRowToTask(ResultSet rs) throws SQLException {
        return new Task(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("description"),
                TaskStatus.valueOf(rs.getString("status")),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
