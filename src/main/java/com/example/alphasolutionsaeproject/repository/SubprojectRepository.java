package com.example.alphasolutionsaeproject.repository;


import com.example.alphasolutionsaeproject.model.Subproject;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class SubprojectRepository {

    private final JdbcTemplate jdbcTemplate;

    public SubprojectRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Subproject> findAll() {
        String sql = "SELECT * FROM subproject";
        return jdbcTemplate.query(sql, mapSubprojects());
    }

    public Subproject findById(int id) {
        String sql = "SELECT * FROM subproject WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, mapSubprojects(), id);
    }

    public void save(Subproject subproject) {
        String sql = "INSERT INTO subproject (projectId, title, priority, deadline, duration, workHours, checked) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, subproject.getProjectId(), subproject.getTitle(), subproject.getPriority(),
                subproject.getDeadline(), subproject.getDuration(), subproject.getWorkHours(), subproject.getChecked());
    }

    public void update(Subproject subproject, int spid) {
        String sql = "UPDATE subproject SET title = ?, priority = ?, deadline = ?, duration = ?, checked = ?, estDeadline = ? WHERE id = ?";
        jdbcTemplate.update(sql, subproject.getTitle(), subproject.getPriority(),
                subproject.getDeadline(), subproject.getDuration(),
                subproject.getChecked(), subproject.getEstDeadline(), spid);
    }


    public void delete(int spid) {
        String sql = "DELETE FROM subproject WHERE id = ?";
        jdbcTemplate.update(sql, spid);
    }

    public List<Subproject> getAllProjectsByProjectId(int id) {
        String sql = "SELECT * FROM subproject WHERE projectId = ?";
        return jdbcTemplate.query(sql, mapSubprojects(), id);
    }

    public void updateChecked(int id, boolean newValue) {
        String sql = "UPDATE subproject SET checked = ? WHERE id = ?";
        jdbcTemplate.update(sql, newValue, id);
    }

    public int getWorkHours(int subprojectId) {
        String sql = "SELECT workHours FROM subproject WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, subprojectId);
    }

    public void updateEstimatedDeadline(Subproject subproject) {
        String sql = "UPDATE subproject SET estDeadline = ? WHERE id = ?";
        jdbcTemplate.update(sql, subproject.getEstDeadline(), subproject.getId());
    }

    public void updateWorkHours(int subprojectId, int newWorkHours) {
        String sql = "UPDATE subproject SET workHours = ? WHERE id = ?";
        jdbcTemplate.update(sql, newWorkHours, subprojectId);
    }

    public Integer findProjectIdBySubprojectId(int subprojectId) {
        try {
            String sql = "SELECT projectId FROM subproject WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, Integer.class, subprojectId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public int sumWorkHoursByProject(int projectId) {
        String sql = "SELECT COALESCE(SUM(workHours), 0) FROM subproject WHERE projectId = ?";
        Integer sum = jdbcTemplate.queryForObject(sql, Integer.class, projectId);
        return sum != null ? sum : 0;
    }


    private RowMapper<Subproject> mapSubprojects() {
        return (rs, rowNum) -> {
            LocalDate estDeadline = rs.getDate("estDeadline") != null
                    ? rs.getDate("estDeadline").toLocalDate()
                    : rs.getDate("deadline").toLocalDate();  // fallback


            return new Subproject(
                    rs.getInt("id"),
                    rs.getInt("projectId"),
                    rs.getString("title"),
                    rs.getInt("priority"),
                    rs.getDate("deadline").toLocalDate(),
                    estDeadline,
                    rs.getInt("duration"),
                    rs.getInt("workHours"),
                    rs.getBoolean("checked")
            );
        };
    }
}
