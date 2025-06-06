package com.example.alphasolutionsaeproject.service;

import com.example.alphasolutionsaeproject.model.Role;
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
    public boolean login(String email, String paswword){
        User user = userRepository.getUser(email);
        if (user != null) {
            return checkPassword(paswword, user.getPassword());
        }
        return false;
    }

    // Sammenligner to adgangskoder
    public boolean checkPassword(String enteredPassword, String storedPassword){
        return Objects.equals(enteredPassword, storedPassword);
    }

    // Tjekker om brugernavnet er gyldigt
    private boolean isUsernameValid(String username){
        String regex = "^[a-zA-Z0-9_ ]{3,50}$"; // minimum format abc
        return username.matches(regex);
    }

    // Tjekker om emailen er gyldig
    private boolean isEmailValid(String email){
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"; // format abc@mail.dk
        return email.matches(regex);
    }

    // Registrerer en ny bruger
    public boolean register(String email, String username, String password, String role) {
        if (userRepository.getUser(email) != null ||
                !isUsernameValid(username) ||
                !isEmailValid(email)) {
            return false; // Email exists or input invalid
        }
        userRepository.registerUser(email, username, password, Role.valueOf(role));
        return true; // Successful registration
    }

    public void updateUser(User user, int uid) {
        userRepository.updateUser(user, uid);
    }

    public User getUserById(int id) {
        return userRepository.getUser(id);
    }

    public User getUserByMail(String email) {
        return userRepository.getUser(email);
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public List<User> getAllPms(String role) {
        return userRepository.getAllUsersByRole(role);
    }

    public List<User> getAllAssigned(int pid){
        return userRepository.getAllAssigned(pid);
    }

    public List<User> getAllUnassigned(int pid){
        return userRepository.getAllUnassigned(pid);
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }
}