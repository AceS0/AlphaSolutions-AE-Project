package com.example.alphasolutionsaeproject.service;

import com.example.alphasolutionsaeproject.model.User;
import com.example.alphasolutionsaeproject.model.Role;
import com.example.alphasolutionsaeproject.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loginTrueWhenPasswordMatches() {
        // Arrange
        String email = "test@alpha.dk";
        String password = "alpha123";
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        when(userRepository.getUser(email)).thenReturn(user);

        // Act
        boolean result = userService.login(email, password);

        // Assert
        assertTrue(result);
        verify(userRepository).getUser(email);
    }

    @Test
    void loginFalseIfUserNotFound() {
        // Arrange
        when(userRepository.getUser(anyString())).thenReturn(null);

        // Act
        boolean result = userService.login("test@alpha.dk", "alpha123");

        // Assert
        assertFalse(result);
    }

    @Test
    void registerReturnFalseIfEmailExists() {
        // Arrange
        String email = "exists@alpha.dk";
        when(userRepository.getUser(email)).thenReturn(new User());

        // Act
        boolean result = userService.register(email, "username", "password", "EMPLOYEE");

        // Assert
        assertFalse(result);
        verify(userRepository).getUser(email);
        verify(userRepository, never()).registerUser(anyString(), anyString(), anyString(), any());
    }

    @Test
    void registerReturnFalseIfEmailInvalid() {
        // Arrange
        String invalidEmail = "invalid-email";

        // Act
        boolean result = userService.register(invalidEmail, "username", "password", "EMPLOYEE");

        // Assert
        assertFalse(result);
    }

    @Test
    void registerReturnTrueIfValidInput() {
        // Arrange
        String email = "valid@alpha.dk";
        when(userRepository.getUser(email)).thenReturn(null);

        // Act
        boolean result = userService.register(email, "validUser", "password", "EMPLOYEE");

        // Assert
        assertTrue(result);
        verify(userRepository).registerUser(eq(email), eq("validUser"), eq("password"), eq(Role.EMPLOYEE));
    }

    @Test
    void getUserByIdReturnUser() {
        // Arrange
        User user = new User();
        when(userRepository.getUser(1)).thenReturn(user);

        // Act
        User result = userService.getUserById(1);

        // Assert
        assertEquals(user,result);
        verify(userRepository).getUser(1);
    }

    @Test
    void deleteUserCallRepositoryDelete() {
        // Arrange
        int userId = 1;

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(userRepository).deleteById(userId);
    }

}
