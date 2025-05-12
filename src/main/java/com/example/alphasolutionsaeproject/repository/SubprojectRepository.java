package com.example.alphasolutionsaeproject.repository;


import com.example.alphasolutionsaeproject.model.Project;
import com.example.alphasolutionsaeproject.model.Subproject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class SubprojectRepository {

    private final JdbcTemplate jdbcTemplate;

    public SubprojectRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Find alle subprojects
    public List<Subproject> findAll() {
        String sql = "SELECT * FROM subproject";
        return jdbcTemplate.query(sql, mapSubprojects());
    }

    // Find subproject by id
    public Subproject findById(int id) {
        String sql = "SELECT * FROM subproject WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, mapSubprojects(), id);
    }

    // Tilføj nyt subproject
    public void save(Subproject subproject) {
        String sql = "INSERT INTO subproject (projectId, title, priority, deadline, duration, checked) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, subproject.getProjectId(), subproject.getTitle(), subproject.getPriority(),
                subproject.getDeadline(), subproject.getDuration(), subproject.getChecked());
    }

    // Opdater eksisterende subproject
    public void update(Subproject subproject) {
        String sql = "UPDATE subproject SET projectId = ?, title = ?, priority = ?, deadline = ?, duration = ?, checked = ? WHERE id = ?";
        jdbcTemplate.update(sql, subproject.getProjectId(), subproject.getTitle(), subproject.getPriority(),
                subproject.getDeadline(), subproject.getDuration(), subproject.getChecked(), subproject.getId());
    }

    // Slet subproject
    public void delete(int spid) {
        String sql = "DELETE FROM subproject WHERE id = ?";
        jdbcTemplate.update(sql,  spid);
    }

    public List<Subproject> getAllProjectsByProjectId(int id){
        String sql = "SELECT * FROM subproject WHERE projectId = ?";
        return jdbcTemplate.query(sql, mapSubprojects(), id);
    }

    public void updateChecked(int id, boolean newValue) {
        String sql = "UPDATE subproject SET checked = ? WHERE id = ?";
        jdbcTemplate.update(sql, newValue, id);
    }

    private RowMapper<Subproject> mapSubprojects(){
        return (rs, rowNum) -> new Subproject(
                rs.getInt("id"),
                rs.getInt("projectId"),
                rs.getString("title"),
                rs.getInt("priority"),
                rs.getString("deadline"),
                rs.getInt("duration"),
                rs.getBoolean("checked")
        );
    }
}
