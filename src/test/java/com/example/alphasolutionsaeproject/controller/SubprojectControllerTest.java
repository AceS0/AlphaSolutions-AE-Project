package com.example.alphasolutionsaeproject.controller;

import com.example.alphasolutionsaeproject.model.Role;
import com.example.alphasolutionsaeproject.model.Subproject;
import com.example.alphasolutionsaeproject.model.User;
import com.example.alphasolutionsaeproject.service.SubprojectService;
import com.example.alphasolutionsaeproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubprojectControllerTest {

    @Mock
    private SubprojectService subprojectService;

    @Mock
    private UserService userService;

    @Mock
    private HttpSession session;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private SubprojectController subprojectController;

    @Test
    public void testListSubprojectsLoggedInEmployeeReturnsEmployeeView() {
        // Arrange
        int pid = 1;
        String projectName = "Project X";
        User user = new User();
        user.setRole(Role.EMPLOYEE);
        when(session.getAttribute("email")).thenReturn("user@example.com");
        when(userService.getUserByMail("user@example.com")).thenReturn(user);
        when(subprojectService.getAllSubprojectsByProjectId(pid)).thenReturn(List.of());

        Model model = new ExtendedModelMap();

        // Act
        String viewName = subprojectController.listSubprojects(pid, projectName, model, session);

        // Assert
        assertEquals("Employee/subprojects", viewName);
        verify(session).setAttribute("projectName", projectName);
        assertTrue(model.containsAttribute("subprojects"));
    }

    @Test
    public void testAddSubprojectInvalidPriorityRedirectsWithError() {
        // Arrange
        Subproject subproject = new Subproject();
        subproject.setPriority(6);
        int pid = 1;

        when(redirectAttributes.addFlashAttribute(eq("error"), anyString())).thenReturn(redirectAttributes);

        // Act
        String viewName = subprojectController.addSubproject(subproject, pid, redirectAttributes);

        // Assert
        assertEquals("redirect:/projects/{pid}/subprojects/add", viewName);
        verify(redirectAttributes).addFlashAttribute("error", "Priority should be between 1-5.");
    }

    @Test
    public void testEditSubprojectValidPriorityUpdatesAndRedirects() {
        // Arrange
        int pid = 1;
        int spid = 2;
        Subproject subproject = new Subproject();
        subproject.setPriority(3);

        // Act
        String viewName = subprojectController.editSubproject(pid, spid, subproject, redirectAttributes);

        // Assert
        assertEquals("redirect:/projects/{pid}/subprojects", viewName);
        assertFalse(subproject.getChecked());
        verify(subprojectService).updateSubproject(subproject, spid);
    }

    @Test
    public void testDeleteSubprojectDeletesAndRedirects() {
        // Arrange
        int pid = 1;
        int spid = 5;

        // Act
        String viewName = subprojectController.deleteSubproject(pid, spid,session);

        // Assert
        assertEquals("redirect:/projects/{pid}/subprojects", viewName);
        verify(subprojectService).deleteSubproject(spid);
    }
}
