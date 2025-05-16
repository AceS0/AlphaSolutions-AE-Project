package com.example.alphasolutionsaeproject.repository;

import com.example.alphasolutionsaeproject.model.*;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@SuppressWarnings("ALL")
@Repository
public class TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    public TaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Task> findAll() {
        String sql = "SELECT * FROM task";
        return jdbcTemplate.query(sql, mapTasks());
    }

    public Task findById(int id) {
        String sql = "SELECT * FROM task WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, mapTasks(), id);
    }

    public void save(Task task, int spid) {
        String sql = "INSERT INTO task (subprojectId, title, description, deadline, duration, workHours, status, priority, checked)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, spid, task.getTitle(), task.getDescription(),
                task.getDeadline(), task.getDuration(), task.getWorkHours(), task.getStatus(),
                task.getPriority(), task.getChecked());
    }

    public void update(Task task) {
        String sql = "UPDATE task SET title = ?, description = ?, status = ?, priority = ?, deadline = ?, estDeadline = ?, duration = ?, workHours = ? WHERE id = ?";
        jdbcTemplate.update(sql, task.getTitle(), task.getDescription(), task.getStatus(),
                task.getPriority(), task.getDeadline(), task.getEstDeadline(), task.getDuration(), task.getWorkHours(), task.getId());
    }

    public void delete(int tid) {
        String sql = "DELETE FROM task WHERE id = ?";
        jdbcTemplate.update(sql, tid);
    }

    public void assignUserToTask(int taskId, int userId) {
        String sql = "INSERT INTO task_user (task_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, taskId, userId);
    }

    public void assignToProjectIfNotExists(int userId, int projectId) {
        String sql = "INSERT IGNORE INTO project_user (project_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, projectId, userId);
    }

    public void removeUserFromTask(int taskId, int userId) {
        String sql = "DELETE FROM task_user WHERE task_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, taskId, userId);
    }

    public int getProjectIdByTask(int taskId) {
        String sql = """
                    SELECT p.id
                    FROM project p
                    JOIN subproject sp ON sp.projectId = p.id
                    JOIN task t ON t.subprojectId = sp.id
                    WHERE t.id = ?
                """;
        return jdbcTemplate.queryForObject(sql, Integer.class, taskId);
    }

    public boolean isUserStillAssignedToProject(int userId, int projectId) {
        String sql = """
                    SELECT COUNT(*)
                    FROM task_user tu
                    JOIN task t ON tu.task_id = t.id
                    JOIN subproject sp ON t.subprojectId = sp.id
                    WHERE tu.user_id = ? AND sp.projectId = ?
                """;
        int count = jdbcTemplate.queryForObject(sql, Integer.class, userId, projectId);
        return count > 0;
    }

    public void removeUserFromProject(int userId, int projectId) {
        String sql = "DELETE FROM project_user WHERE project_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, projectId, userId);
    }


    public List<User> getUsersAssignedTo(int taskId) {
        String sql = "SELECT u.id, u.email, u.username, u.password, u.role " +
                "FROM user u " +
                "JOIN task_user tu ON u.id = tu.user_id " +
                "WHERE tu.task_id = ?";
        return jdbcTemplate.query(sql, mapUsers(), taskId);
    }

    public List<User> getUsersUnassignedTo(int taskId) {
        String sql = "SELECT u.id, u.email, u.username, u.password, u.role " +
                "FROM user u " +
                "WHERE u.id NOT IN (SELECT user_id FROM task_user WHERE task_id = ?)";
        return jdbcTemplate.query(sql, mapUsers(), taskId);
    }


    public List<Task> getAllTasksBySpid(int spid) {
        String sql = "SELECT * FROM task WHERE subprojectId = ?";
        return jdbcTemplate.query(sql, mapTasks(), spid);
    }

    public void updateChecked(int id, boolean newValue) {
        String sql = "UPDATE task SET checked = ? WHERE id = ?";
        jdbcTemplate.update(sql, newValue, id);
    }

    public void updateWorkHours(int taskId, int workHours) {
        String sql = "UPDATE task SET workHours = ? WHERE id = ?";
        jdbcTemplate.update(sql, workHours, taskId);
    }

    public void updateStatus(int taskId, String status) {
        String sql = "UPDATE task SET status = ? WHERE id = ?";
        jdbcTemplate.update(sql, status, taskId);
    }

    public int sumWorkHoursBySubproject(int subprojectId) {
        try {
            String sql = "SELECT COALESCE(SUM(workHours), 0) FROM task WHERE subprojectId = ?";
            return jdbcTemplate.queryForObject(sql, Integer.class, subprojectId);
        } catch (DataAccessException DAE) {
            throw new RuntimeException("Database not connected", DAE);
        }
    }

    public void updateEstimatedDeadline(int taskId, LocalDate estDeadline) {
        String sql = "UPDATE task SET estDeadline = ? WHERE id = ?";
        jdbcTemplate.update(sql, estDeadline, taskId);
    }


    private RowMapper<Task> mapTasks() {
        return (rs, rowNum) -> {
            LocalDate estDeadline = rs.getDate("estDeadline") != null
                    ? rs.getDate("estDeadline").toLocalDate()
                    : rs.getDate("deadline").toLocalDate();


            return new Task(
                    rs.getInt("id"),
                    rs.getInt("subprojectId"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getDate("deadline").toLocalDate(),
                    estDeadline,
                    rs.getInt("duration"),
                    rs.getInt("workHours"),
                    rs.getString("status"),
                    rs.getString("priority"),
                    rs.getBoolean("checked")
            );
        };
    }

    private RowMapper<User> mapUsers() {
        return (rs, rowNum) -> new User(
                rs.getInt("id"),
                rs.getString("email"),
                rs.getString("username"),
                rs.getString("password"),
                Role.valueOf(rs.getString("role").toUpperCase())
        );
    }
}

