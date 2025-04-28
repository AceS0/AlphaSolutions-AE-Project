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

    // Tjekker om login er korrekt
    public boolean login(String eid, String pw){
        User user = userRepository.getUser(eid);
        if (user != null){
            return checkPassword(pw, user.getPw());  // Sammenligner adgangskoder
        }
        return false;  // Hvis bruger ikke findes, returneres false
    }

    // Sammenligner to adgangskoder
    public boolean checkPassword(String enteredPassword, String storedPassword){
        return Objects.equals(enteredPassword, storedPassword);
    }

    // Tjekker om brugernavnet er gyldigt
    private boolean isUsernameValid(String uid){
        String regex = "^[a-zA-Z0-9_]{3,15}$"; // minimum format abc
        return uid.matches(regex);
    }

    // Tjekker om emailen er gyldig
    private boolean isEmailValid(String eid){
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"; // format abc@mail.dk
        return eid.matches(regex);
    }

    // Registrerer en ny bruger
    public boolean register(String eid, String uid, String pw){
        User user = userRepository.getUser(eid);
        if (user != null || !isUsernameValid(uid) || !isEmailValid(eid)) {
            return false;  // Hvis email allerede findes eller input er ugyldigt, returneres false
        }
        userRepository.registerUser(eid, uid, pw);  // Opretter bruger i databasen
        return true;  // Hvis alt er okay, returneres true
    }
}