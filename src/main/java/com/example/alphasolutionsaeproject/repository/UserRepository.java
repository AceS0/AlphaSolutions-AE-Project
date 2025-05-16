package com.example.alphasolutionsaeproject.repository;

import com.example.alphasolutionsaeproject.model.Role;
import com.example.alphasolutionsaeproject.model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User getUser(String email) {
        try {
            String sql = "SELECT * FROM user WHERE email = ?";
            return jdbcTemplate.queryForObject(sql, mapUsers(), email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public User getUser(int id) {
        try {
            String sql = "SELECT * FROM user WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, mapUsers(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<User> getAllUsers() {
        try {
            String sql = "SELECT * FROM user";
            return jdbcTemplate.query(sql, mapUsers());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<User> getAllUsersByRole(String role) {
        String sql = "SELECT * FROM user WHERE role = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), role);
    }

    public void registerUser(String eid, String uid, String pw, Role role) {
        String sql = "INSERT INTO USER (email, username, password, role) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, eid, uid, pw, role.name());
    }

    public void updateUser(User user, int uid) {
        String sql = "UPDATE user SET email = ?, username = ?, password = ?, role = ? WHERE id = ?";
        jdbcTemplate.update(sql, user.getEmail(),
                user.getUsername(), user.getPassword(), user.getRole().name(), uid);
    }

    public int getProjectManagerId(String getCreatedBy) {
        try {
            String sql = "SELECT id FROM user WHERE username = ?";
            Integer result = jdbcTemplate.queryForObject(sql, Integer.class, getCreatedBy);
            return result != null ? result : -1;
        } catch (EmptyResultDataAccessException e) {
            return -1;
        }
    }

    public User findUserById(int userId) {
        String sql = "SELECT * FROM user WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, mapUsers(), userId);
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

    public void deleteById(int id) {
        String sql = "DELETE FROM user WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
