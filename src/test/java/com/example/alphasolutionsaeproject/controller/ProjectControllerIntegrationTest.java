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
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
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
        userRepository.registerUser(testUser.getEmail(),testUser.getUsername(),"12345678",testUser.getRole());

        // Setup session with user email
        session = new MockHttpSession();
        session.setAttribute("email", testUser.getEmail());

        // Add a project for user
        Project project = new Project();
        project.setTitle("Integration Test Project");
        project.setDescription("Testing project listing");
        project.setDeadline(LocalDate.now().plusDays(7));
        project.setDuration(10);
        project.setCreatedBy(1);
        project.setChecked(false);

        projectService.addProject(project);
    }

    @Test
    void whenUserLoggedInProjectsPageLoads() throws Exception {
        mockMvc.perform(get("/projects").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("PM/projects"))
                .andExpect(model().attributeExists("allProjects"));
    }

    @Test
    void whenNoUserInSessionRedirectToLogin() throws Exception {
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
                    assert sessionAfter == null || sessionAfter.isInvalid();
                });
    }
}
