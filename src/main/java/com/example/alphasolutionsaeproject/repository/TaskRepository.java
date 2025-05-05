package com.example.alphasolutionsaeproject.repository;
import com.example.alphasolutionsaeproject.model.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    public TaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Find alle tasks
    public List<Task> findAll() {
        String sql = "SELECT * FROM task";
        return jdbcTemplate.query(sql, taskRowMapper);
    }

    // Find task by id
    public Task findById(int id) {
        String sql = "SELECT * FROM task WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, taskRowMapper, id);
    }

    // Tilføj ny task
    public void save(Task task) {
        String sql = "INSERT INTO task (columnId, title, description, assignedTo, status, priority, attachments) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                task.getColumnId(),
                task.getTitle(),
                task.getDescription(),
                task.getAssignedTo(),
                task.getStatus(),
                task.getPriority(),
                task.getAttachments());
    }

    // Opdater eksisterende task
    public void update(Task task) {
        String sql = "UPDATE task SET columnId = ?, title = ?, description = ?, assignedTo = ?, status = ?, priority = ?, attachments = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                task.getColumnId(),
                task.getTitle(),
                task.getDescription(),
                task.getAssignedTo(),
                task.getStatus(),
                task.getPriority(),
                task.getAttachments(),
                task.getId());
    }

    // Slet task
    public void delete(int tid) {
        String sql = "DELETE FROM task WHERE id = ?";
        jdbcTemplate.update(sql, tid);
    }

    private RowMapper<Task> taskRowMapper = (rs, rowNum) -> {
        Task task = new Task();
        task.setId(rs.getInt("id"));
        task.setColumnId(rs.getInt("columnId"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setAssignedTo((Integer) rs.getObject("assignedTo"));
        task.setStatus(rs.getString("status"));
        task.setPriority(rs.getString("priority"));
        task.setAttachments(rs.getString("attachments"));
        return task;
    };
}
