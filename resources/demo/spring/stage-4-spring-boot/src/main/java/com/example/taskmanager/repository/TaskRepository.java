package com.example.taskmanager.repository;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.TaskStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object using Spring's JdbcTemplate.
 *
 * IMPROVEMENTS OVER STAGE 3:
 * - No ConnectionManager needed (Spring auto-configures DataSource)
 * - JdbcTemplate handles connection management
 * - Cleaner query methods
 * - Automatic exception translation
 *
 * STILL MANUAL:
 * - Writing SQL
 * - RowMapper implementation
 * - Each method implementation
 *
 * Stage 5 (Spring Data JPA) eliminates all of this!
 */
@Repository
public class TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * JdbcTemplate is auto-configured by Spring Boot!
     * Just inject it - no setup required.
     */
    public TaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Task save(Task task) {
        String sql = "INSERT INTO tasks (title, description, status, created_at) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setString(3, task.getStatus().name());
            ps.setTimestamp(4, Timestamp.valueOf(task.getCreatedAt()));
            return ps;
        }, keyHolder);

        task.setId(keyHolder.getKey().longValue());
        return task;
    }

    public Optional<Task> findById(Long id) {
        String sql = "SELECT * FROM tasks WHERE id = ?";

        List<Task> results = jdbcTemplate.query(sql, this::mapRowToTask, id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public List<Task> findAll() {
        String sql = "SELECT * FROM tasks ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, this::mapRowToTask);
    }

    public Task update(Task task) {
        String sql = "UPDATE tasks SET title = ?, description = ?, status = ? WHERE id = ?";

        jdbcTemplate.update(sql,
                task.getTitle(),
                task.getDescription(),
                task.getStatus().name(),
                task.getId());

        return task;
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<Task> findByStatus(TaskStatus status) {
        String sql = "SELECT * FROM tasks WHERE status = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, this::mapRowToTask, status.name());
    }

    /**
     * Row mapper - converts ResultSet row to Task object.
     * Still manual, but cleaner than Stage 3.
     */
    private Task mapRowToTask(ResultSet rs, int rowNum) throws SQLException {
        return new Task(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("description"),
                TaskStatus.valueOf(rs.getString("status")),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
