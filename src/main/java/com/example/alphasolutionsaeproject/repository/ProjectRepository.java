package com.example.alphasolutionsaeproject.repository;
import com.example.alphasolutionsaeproject.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProjectRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Hent alle projekter
    public List<Project> findAll() {
        String sql = "SELECT * FROM project";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Project project = new Project();
            project.setId(rs.getInt("id"));
            project.setTitle(rs.getString("title"));
            project.setDescription(rs.getString("description"));
            project.setCreatedBy(rs.getInt("createdBy"));
            return project;
        });
    }

    // Hent projekt baseret på ID
    public Project findById(int id) {
        String sql = "SELECT * FROM project WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, mapProjects(), id);
    }

    // Gem et nyt projekt
    public void save(Project project) {
        String sql = "INSERT INTO project (title, description, deadline, duration, createdBy, checked) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, project.getTitle(), project.getDescription(), project.getDeadline(),
                project.getDuration(), project.getCreatedBy(), project.getChecked());
    }

    // Opdater et eksisterende projekt
    public void update(Project project) {
        String sql = "UPDATE project SET title = ?, description = ?, deadline = ?, duration = ?, createdBy = ?, checked = ? WHERE id = ?";
        jdbcTemplate.update(sql, project.getTitle(), project.getDescription(), project.getDeadline(),
                project.getDuration(), project.getCreatedBy(), project.getChecked());
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
