package com.example.alphasolutionsaeproject.controller;
import com.example.alphasolutionsaeproject.model.Project;
import com.example.alphasolutionsaeproject.service.ProjectService;
import com.example.alphasolutionsaeproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.support.SessionStatus;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ProjectControllerTest {

    @InjectMocks
    private ProjectController projectController;

    @Mock
    private ProjectService projectService;

    @Mock
    private UserService userService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @Mock
    private SessionStatus sessionStatus;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testShowHomePageInvalidateSessionAndReturnIndex() {
        // Arrange
        doNothing().when(session).invalidate();

        // Act
        String viewName = projectController.showHomePage(session);

        // Assert
        verify(session).invalidate();
        assertEquals("index", viewName);
    }

    @Test
    public void testShowAdminPageNotLoggedInRedirectToLogin() {
        // Arrange
        when(session.getAttribute("email")).thenReturn(null);

        // Act
        String viewName = projectController.showAdminPage(session);

        // Assert
        assertEquals("redirect:/users/login", viewName);
    }

    @Test
    public void testShowAdminPageLoggedInReturnAdminPage() {
        // Arrange
        when(session.getAttribute("email")).thenReturn("test@example.com");

        // Act
        String viewName = projectController.showAdminPage(session);

        // Assert
        assertEquals("Admin/adminPage", viewName);
    }

    @Test
    public void testToggleCheckedCallServiceAndRedirect() {
        // Arrange
        int pid = 1;
        doNothing().when(projectService).toggleCheckedAndCascade(pid);

        // Act
        String viewName = projectController.toggleChecked(pid);

        // Assert
        verify(projectService).toggleCheckedAndCascade(pid);
        assertEquals("redirect:/projects", viewName);
    }

    @Test
    public void testListProjectsNotLoggedInRedirectToLogin() {
        // Arrange
        when(session.getAttribute("email")).thenReturn(null);

        // Act
        String viewName = projectController.listProjects(model, session, sessionStatus);

        // Assert
        assertEquals("redirect:/users/login", viewName);
    }

    @Test
    public void testEditProjectUpdateProjectAndRedirect() {
        // Arrange
        int pid = 42;
        Project project = new Project();
        project.setProjectManager("pmUser");

        when(projectService.getProjectManagerId("pmUser")).thenReturn(7);
        doNothing().when(projectService).updateProject(project, pid);

        // Act
        String viewName = projectController.editProject(pid, project);

        // Assert
        assertEquals("redirect:/projects", viewName);
        assertEquals(7, project.getCreatedBy());
        assertEquals(false, project.getChecked());
        verify(projectService).updateProject(project, pid);
    }

}
