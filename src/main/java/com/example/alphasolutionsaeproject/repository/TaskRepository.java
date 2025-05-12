package com.example.alphasolutionsaeproject.repository;
import com.example.alphasolutionsaeproject.model.*;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
    public void save(Task task, int spid) {
        String sql = "INSERT INTO task (subprojectId, title, description, deadline, duration, status, priority, checked)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,spid, task.getTitle(), task.getDescription(),
                            task.getDeadline(), task.getDuration(), task.getStatus(),
                            task.getPriority(), task.getChecked());

    }

    // Opdater eksisterende task
    public void update(Task task) {
        String sql = "UPDATE task SET title = ?, description = ?, status = ?, priority = ?, deadline = ?, duration = ? WHERE id = ?";
        jdbcTemplate.update(sql, task.getTitle(), task.getDescription(), task.getStatus(),
                    task.getPriority(), task.getDeadline(), task.getDuration(), task.getId());
    }

    // Slet task
    public void delete(int tid) {
        String sql = "DELETE FROM task WHERE id = ?";
        jdbcTemplate.update(sql, tid);
    }

    public void updateChecked(int id, boolean newValue) {
        String sql = "UPDATE task SET checked = ? WHERE id = ?";
        jdbcTemplate.update(sql, newValue, id);
    }

    public void assignUserToTask(int taskId, int userId){
            // 1. Assign user to task
            jdbcTemplate.update("INSERT INTO task_user (task_id, user_id) VALUES (?, ?)", taskId, userId);

            // 2. Assign user to project if not already assigned
            String sql = """
            INSERT IGNORE INTO project_user (project_id, user_id)
            SELECT p.id, ?
            FROM project p
            JOIN subproject sp ON sp.projectId = p.id
            JOIN task t ON t.subprojectId = sp.id
            WHERE t.id = ?
            """;
            jdbcTemplate.update(sql, userId, taskId);
        }

    public void unassignUserToTask(int taskId, int userId){
        String sql = "DELETE FROM task_user WHERE task_id = ? AND user_id = ?";
        jdbcTemplate.update(sql,taskId,userId);
    }

    public List<User> getUsersAssignedTo(int taskId){
        String sql = "SELECT u.id, u.email, u.username, u.password, u.role " +
                "FROM user u " +
                "JOIN task_user tu ON u.id = tu.user_id " +
                "WHERE tu.task_id = ?";
        return jdbcTemplate.query(sql,mapUsers(),taskId);
    }

    public List<User> getUsersUnassignedTo(int taskId){
        String sql = "SELECT u.id, u.email, u.username, u.password, u.role " +
                "FROM user u " +
                "WHERE u.id NOT IN (SELECT user_id FROM task_user WHERE task_id = ?)";
        return jdbcTemplate.query(sql,mapUsers(),taskId);
    }


    public List<Task> getAllTasksBySpid(int spid){
        String sql = "SELECT * FROM task WHERE subprojectId = ?";
        return jdbcTemplate.query(sql, mapTasks(), spid);
    }




    private RowMapper<Task> mapTasks(){
        return (rs, rowNum) -> new Task(
                rs.getInt("id"),
                rs.getInt("subprojectId"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getString("deadline"),
                rs.getInt("duration"),
                rs.getString("status"),
                rs.getString("priority"),
                rs.getBoolean("checked")
        );
    }

    private RowMapper<User> mapUsers(){
        return (rs, rowNum) -> new User(
                rs.getInt("id"),
                rs.getString("email"),
                rs.getString("username"),
                rs.getString("password"),
                Role.valueOf(rs.getString("role").toUpperCase())
        );
    }




}

