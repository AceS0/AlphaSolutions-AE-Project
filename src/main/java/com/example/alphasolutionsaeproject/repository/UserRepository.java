package com.example.alphasolutionsaeproject.repository;

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

    public User getUser(String eid){
        try{
            String sql = "SELECT * FROM user WHERE email = ?";
            return jdbcTemplate.queryForObject(sql,mapUsers(),eid);
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public void registerUser(String eid, String uid, String pw){
        String sql = "INSERT INTO USER (email, username, password) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql,eid,uid,pw);
    }



    private RowMapper<User> mapUsers(){
        return (rs, rowNum) -> new User(
                rs.getString("email"),
                rs.getString("username"),
                rs.getString("password")
        );
    }
}