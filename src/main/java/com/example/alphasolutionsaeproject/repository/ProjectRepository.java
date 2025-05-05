package com.example.alphasolutionsaeproject.repository;
import com.example.alphasolutionsaeproject.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class ProjectRepository {

    private JdbcTemplate jdbcTemplate;

    public ProjectRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    // Hent alle projekter
    public List<Project> findAll() {
        String sql = "SELECT * FROM project";
        return jdbcTemplate.query(sql,mapProjects());
    }

    // Hent projekt baseret på ID
    public Project findById(int id) {
        String sql = "SELECT * FROM project WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, mapProjects(), id);
    }


    // Gem et nyt projekt
    public void save(Project project) {
        String sql = "INSERT INTO project (title, description, deadline, duration, createdBy, checked) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, project.getTitle(), project.getDescription(), project.getDeadline(),
                project.getDuration(), project.getCreatedBy(), project.getChecked());
    }

    // Opdater et eksisterende projekt
    public void update(Project project, int pid) {
        String sql = "UPDATE project SET title = ?, description = ?, deadline = ?, duration = ?, createdBy = ?, checked = ? WHERE id = ?";
        jdbcTemplate.update(sql, project.getTitle(), project.getDescription(), project.getDeadline(),
                project.getDuration(), project.getCreatedBy(), project.getChecked(),pid);
    }

    // Slet et projekt
    public void deleteById(int id) {
        String sql = "DELETE FROM project WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<Project> getAllProjectsByUserId(int id){
        String sql = "SELECT * FROM project WHERE createdBy = ?";
        return jdbcTemplate.query(sql, mapProjects(), id);
    }

    public List<Project> getSharedProjectsByUserId(int userId) {
        String sql = "SELECT p.* FROM project p JOIN project_user pu ON p.id = pu.project_id WHERE pu.user_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Project.class), userId);
    }


    public void updateChecked(int id, boolean newValue) {
        String sql = "UPDATE project SET checked = ? WHERE id = ?";
        jdbcTemplate.update(sql, newValue, id);
    }


    private RowMapper<Project> mapProjects(){
        return (rs, rowNum) -> new Project(
                rs.getInt("id"),
                rs.getString("title"),
            rs.getString("description"),
            rs.getString("deadline"),
            rs.getInt("duration"),
            rs.getInt("createdBy"),
                rs.getBoolean("checked")
        );
    }
}
