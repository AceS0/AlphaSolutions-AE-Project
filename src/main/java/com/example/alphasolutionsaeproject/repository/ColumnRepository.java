package com.example.alphasolutionsaeproject.repository;
import com.example.alphasolutionsaeproject.model.Column;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ColumnRepository {

    private final JdbcTemplate jdbcTemplate;

    public ColumnRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Column> findAll() {
        String sql = "SELECT * FROM column_table";
        return jdbcTemplate.query(sql, columnRowMapper);
    }

    public Column findById(int id) {
        String sql = "SELECT * FROM column_table WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, columnRowMapper, id);
    }

    public void save(Column column) {
        String sql = "INSERT INTO column_table (subprojectId, title, position) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql,
                column.getSubprojectId(),
                column.getTitle(),
                column.getPosition());
    }

    public void update(Column column) {
        String sql = "UPDATE column_table SET subprojectId = ?, title = ?, position = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                column.getSubprojectId(),
                column.getTitle(),
                column.getPosition(),
                column.getId());
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM column_table WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    private RowMapper<Column> columnRowMapper = (rs, rowNum) -> {
        Column column = new Column();
        column.setId(rs.getInt("id"));
        column.setSubprojectId(rs.getInt("subprojectId"));
        column.setTitle(rs.getString("title"));
        column.setPosition(rs.getInt("position"));
        return column;
    };
}



