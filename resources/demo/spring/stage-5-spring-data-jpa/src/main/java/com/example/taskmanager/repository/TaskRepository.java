package com.example.taskmanager.repository;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Task Repository - Spring Data JPA Magic!
 *
 * =====================================================
 * COMPARE THIS TO STAGE 4!
 * =====================================================
 *
 * Stage 4: ~100 lines of JdbcTemplate code
 * Stage 5: 3 lines!
 *
 * JpaRepository<Task, Long> provides:
 * - save(Task) - Create or update
 * - findById(Long) - Find by primary key
 * - findAll() - Get all tasks
 * - deleteById(Long) - Delete by primary key
 * - count() - Count all tasks
 * - existsById(Long) - Check if exists
 * - And more!
 *
 * Spring Data JPA generates the implementation at runtime.
 * You write interface, Spring writes SQL!
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Find tasks by status.
     *
     * Spring Data JPA parses this method name and generates:
     * SELECT * FROM tasks WHERE status = ?
     *
     * No SQL needed!
     */
    List<Task> findByStatus(TaskStatus status);

    /**
     * Find tasks ordered by creation date (newest first).
     *
     * Generates: SELECT * FROM tasks ORDER BY created_at DESC
     */
    List<Task> findAllByOrderByCreatedAtDesc();

    /**
     * Find tasks by status, ordered by creation date.
     *
     * Generates: SELECT * FROM tasks WHERE status = ? ORDER BY created_at DESC
     */
    List<Task> findByStatusOrderByCreatedAtDesc(TaskStatus status);

    /*
     * More examples of derived query methods:
     *
     * List<Task> findByTitleContaining(String keyword);
     *   -> WHERE title LIKE '%keyword%'
     *
     * List<Task> findByTitleStartingWith(String prefix);
     *   -> WHERE title LIKE 'prefix%'
     *
     * long countByStatus(TaskStatus status);
     *   -> SELECT COUNT(*) WHERE status = ?
     *
     * boolean existsByTitle(String title);
     *   -> SELECT EXISTS(...)
     *
     * void deleteByStatus(TaskStatus status);
     *   -> DELETE WHERE status = ?
     */
}
