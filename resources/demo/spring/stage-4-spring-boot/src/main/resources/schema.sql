-- Schema for Task Manager
-- This file is automatically executed by Spring Boot on startup

CREATE TABLE IF NOT EXISTS tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert sample data (optional)
INSERT INTO tasks (title, description, status) VALUES
    ('Learn Spring Boot', 'Complete the Spring Boot tutorial', 'IN_PROGRESS'),
    ('Build REST API', 'Create RESTful endpoints', 'PENDING'),
    ('Write Tests', 'Add unit and integration tests', 'PENDING');
