package com.example.alphasolutionsaeproject.controller;

import com.example.alphasolutionsaeproject.model.Role;
import com.example.alphasolutionsaeproject.model.User;
import com.example.alphasolutionsaeproject.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private MockHttpSession session;

    private User testUser;

    @BeforeEach
    void setup() {
        // Setup user in DB
        testUser = new User();
        testUser.setUsername("testAdmin");
        testUser.setEmail("testAdmin@example.com");
        testUser.setRole(Role.ADMIN);
        testUser.setPassword("12345678");
        userRepository.registerUser(testUser.getEmail(),testUser.getUsername(),testUser.getPassword(),testUser.getRole());

        // Setup session with user email
        session = new MockHttpSession();
        session.setAttribute("email", testUser.getEmail());
    }

    @Test
    void testRegisterUserSuccessful() throws Exception {
        mockMvc.perform(post("/admin/users/create")
                        .param("email", "test@example.com")
                        .param("username", "testuser")
                        .param("password", "password123")
                        .param("role", "ADMIN"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"));
    }

    @Test
    void testRegisterUserExistingEmail() throws Exception {
        // Create user manually
        userRepository.registerUser("test@example.com", "existing", "password123", Role.ADMIN);

        mockMvc.perform(post("/admin/users/create")
                        .param("email", "test@example.com")
                        .param("username", "testuser2")
                        .param("password", "password123")
                        .param("role", "ADMIN"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users/create"));
    }


    @Test
    void testRegisterUserShortPassword() throws Exception {
        mockMvc.perform(post("/admin/users/create")
                        .param("email", "test3@example.com")
                        .param("username", "testuser3")
                        .param("password", "1234567") // too short password, minimum 8 character.
                        .param("role", "ADMIN")
                        .sessionAttr("email", "admin@example.com")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users/create"));
    }



    @Test
    void testLoginSuccessful() throws Exception {
        userRepository.registerUser("login@example.com", "loginUser", "mypassword", Role.EMPLOYEE);

        mockMvc.perform(post("/users/login")
                        .param("email", "login@example.com")
                        .param("password", "mypassword")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects"));
    }

    @Test
    void testLoginFailure() throws Exception {
        mockMvc.perform(post("/users/login")
                        .param("email", "nonexisting@example.com")
                        .param("password", "wrongpassword")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Wrong email or password")));
    }

}
