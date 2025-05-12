package com.example.alphasolutionsaeproject.repository;
import com.example.alphasolutionsaeproject.model.*;
import org.springframework.dao.EmptyResultDataAccessException;
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
        return jdbcTemplate.query(sql, mapTasks());
    }

    // Find task by id
    public Task findById(int id) {
        String sql = "SELECT * FROM task WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, mapTasks(), id);
    }

    // Tilføj ny task
    public void save(Task task) {
        String sql = "INSERT INTO task (title, description, status, priority) VALUES ( ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority());

    }

    // Opdater eksisterende task
    public void update(Task task) {
        String sql = "UPDATE task SET title = ?, description = ?, status = ?, priority = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getId());
    }

    // Slet task
    public void delete(int tid) {
        String sql = "DELETE FROM task WHERE id = ?";
        jdbcTemplate.update(sql, tid);
    }

    public List<Task> getAllTasksBySpid(int spid){
        String sql = "SELECT * FROM task WHERE subprojectId = ?";
        return jdbcTemplate.query(sql, mapTasks(), spid);
    }

    public void updateChecked(int id, boolean newValue) {
        String sql = "UPDATE task SET checked = ? WHERE id = ?";
        jdbcTemplate.update(sql, newValue, id);
    }

    private RowMapper<Task> mapTasks(){
        return (rs, rowNum) -> new Task(
                rs.getInt("id"),
                rs.getInt("subprojectId"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getString("deadline"),
                rs.getInt("duration"),
                Status.valueOf(rs.getString("status").toUpperCase()),
                Priority.valueOf(rs.getString("priority").toUpperCase()),
                rs.getBoolean("checked")
        );
    }



}

