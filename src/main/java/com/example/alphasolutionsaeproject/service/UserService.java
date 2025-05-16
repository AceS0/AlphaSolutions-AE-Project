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

    public boolean login(String email, String paswword) {
        User user = userRepository.getUser(email);
        if (user != null) {
            return checkPassword(paswword, user.getPassword());
        }
        return false;
    }

    public boolean checkPassword(String enteredPassword, String storedPassword) {
        return Objects.equals(enteredPassword, storedPassword);
    }

    private boolean isUsernameValid(String username) {
        String regex = "^[a-zA-Z0-9_ ]{3,50}$"; // minimum format abc
        return username.matches(regex);
    }

    private boolean isEmailValid(String email) {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"; // format abc@mail.dk
        return email.matches(regex);
    }

    public boolean register(String email, String username, String password, String role) {
        if (userRepository.getUser(email) != null ||
                !isUsernameValid(username) ||
                !isEmailValid(email)) {
            return false;
        }
        userRepository.registerUser(email, username, password, Role.valueOf(role));
        return true;
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

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }
}