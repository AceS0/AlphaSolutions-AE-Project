package com.example.alphasolutionsaeproject.controller;

import com.example.alphasolutionsaeproject.model.Project;
import com.example.alphasolutionsaeproject.model.Role;
import com.example.alphasolutionsaeproject.model.User;
import com.example.alphasolutionsaeproject.repository.ProjectRepository;
import com.example.alphasolutionsaeproject.repository.UserRepository;
import com.example.alphasolutionsaeproject.service.ProjectService;
import com.example.alphasolutionsaeproject.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;

import org.springframework.mock.web.MockHttpSession;

import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProjectControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    private MockHttpSession session;

    private User testUser;

    @BeforeEach
    void setup() {
        // Setup user in DB
        testUser = new User();
        testUser.setUsername("testPM");
        testUser.setEmail("testpm@example.com");
        testUser.setRole(Role.PM);
        // You should save user here (assuming userRepository has save method)
        userRepository.registerUser(testUser.getEmail(),testUser.getUsername(),"12345678",testUser.getRole());

        // Setup session with user email
        session = new MockHttpSession();
        session.setAttribute("email", testUser.getEmail());

        // Clean up projects (optional)
        projectRepository.findAll().forEach(p -> projectRepository.deleteById(p.getId()));

        // Add a project for user
        Project project = new Project();
        project.setTitle("Integration Test Project");
        project.setDescription("Testing project listing");
        project.setDeadline(LocalDate.now().plusDays(7));
        project.setDuration(10);
        project.setCreatedBy(testUser.getId());
        project.setChecked(false);

        projectService.addProject(project);
    }

    @Test
    void whenUserLoggedInProjectsPageLoads() throws Exception {
        mockMvc.perform(get("/projects").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("Employee/projects"))
                .andExpect(model().attributeExists("projects"))
                .andExpect(content().string(containsString("Integration Test Project")));
    }

    @Test
    void whenNoUserInSession_thenRedirectToLogin() throws Exception {
        mockMvc.perform(get("/projects"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/login"));
    }

    @Test
    void homePageInvalidatesSession() throws Exception {
        mockMvc.perform(get("/").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(request -> {
                    MockHttpSession sessionAfter = (MockHttpSession) request.getRequest().getSession(false);
                    // session should be invalidated (null or new session)
                    assert sessionAfter == null || sessionAfter.isInvalid();
                });
    }
}
