package repository;
import model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            Project project = new Project();
            project.setId(rs.getInt("id"));
            project.setTitle(rs.getString("title"));
            project.setDescription(rs.getString("description"));
            project.setCreatedBy(rs.getInt("createdBy"));
            return project;
        });
    }

    // Gem et nyt projekt
    public void save(Project project) {
        String sql = "INSERT INTO project (title, description, createdBy) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, project.getTitle(), project.getDescription(), project.getCreatedBy());
    }

    // Opdater et eksisterende projekt
    public void update(Project project) {
        String sql = "UPDATE project SET title = ?, description = ?, createdBy = ? WHERE id = ?";
        jdbcTemplate.update(sql, project.getTitle(), project.getDescription(), project.getCreatedBy(), project.getId());
    }

    // Slet et projekt
    public void deleteById(int id) {
        String sql = "DELETE FROM project WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
