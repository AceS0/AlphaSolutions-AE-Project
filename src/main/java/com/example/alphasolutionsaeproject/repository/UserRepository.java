package com.example.alphasolutionsaeproject.repository;

import com.example.alphasolutionsaeproject.model.Role;
import com.example.alphasolutionsaeproject.model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


@Repository
public class UserRepository{
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    // Henter bruger baseret på email
    public User getUser(String email){
        try{
            String sql = "SELECT * FROM user WHERE email = ?";
            return jdbcTemplate.queryForObject(sql, mapUsers(), email); // Henter bruger ud fra en liste af bruger
        } catch (EmptyResultDataAccessException e){
            return null;  // Hvis ingen bruger findes, returneres null
        }
    }

    public User getUser(int id){
        try{
            String sql = "SELECT * FROM user WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, mapUsers(), id); // Henter bruger ud fra en liste af bruger
        } catch (EmptyResultDataAccessException e){
            return null;  // Hvis ingen bruger findes, returneres null
        }
    }


    // Registrerer ny bruger
    public void registerUser(String eid, String uid, String pw, Role role){
        String sql = "INSERT INTO USER (email, username, password, role) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, eid, uid, pw, role.name());  // Indsætter bruger i databasen
    }

    public User findUserById(int userId) {
        String sql = "SELECT * FROM user WHERE id = ?";

        // Execute the query and return the username
        return jdbcTemplate.queryForObject(sql, mapUsers(), userId);
    }

    // Mapper ResultSet til User objekt
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
