package com.example.alphasolutionsaeproject.repository;


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
        return jdbcTemplate.query(sql, subprojectRowMapper);
    }

    // Find subproject by id
    public Subproject findById(int id) {
        String sql = "SELECT * FROM subproject WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, subprojectRowMapper, id);
    }

    // Tilføj nyt subproject
    public void save(Subproject subproject) {
        String sql = "INSERT INTO subproject (projectId, title) VALUES (?, ?)";
        jdbcTemplate.update(sql, subproject.getProjectId(), subproject.getTitle());
    }

    // Opdater eksisterende subproject
    public void update(Subproject subproject) {
        String sql = "UPDATE subproject SET projectId = ?, title = ? WHERE id = ?";
        jdbcTemplate.update(sql, subproject.getProjectId(), subproject.getTitle(), subproject.getId());
    }

    // Slet subproject
    public void delete(int id) {
        String sql = "DELETE FROM subproject WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    private RowMapper<Subproject> subprojectRowMapper = (rs, rowNum) -> {
        Subproject subproject = new Subproject();
        subproject.setId(rs.getInt("id"));
        subproject.setProjectId(rs.getInt("projectId"));
        subproject.setTitle(rs.getString("title"));
        return subproject;
    };
}
