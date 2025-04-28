package com.example.alphasolutionsaeproject.service;

import com.example.alphasolutionsaeproject.model.User;
import com.example.alphasolutionsaeproject.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean login(String eid, String pw){
        User user = userRepository.getUser(eid);
        if (user != null){
        return checkPassword(pw,user.getPw());
        }
        return false;
    }

    public boolean checkPassword(String enteredPassword, String storedPassword){
        return Objects.equals(enteredPassword, storedPassword);
    }

    private boolean isUsernameValid(String uid){
        String regex = "^[a-zA-Z0-9_]{3,15}$";
        return uid.matches(regex);
    }

    private boolean isEmailValid(String eid){
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return eid.matches(regex);
    }

    public boolean register(String eid, String uid, String pw){
        User user = userRepository.getUser(eid);
        if (user != null || !isUsernameValid(uid) || !isEmailValid(eid)) {
            return false;
        }
        userRepository.registerUser(eid,uid,pw);
        return true;
    }
}