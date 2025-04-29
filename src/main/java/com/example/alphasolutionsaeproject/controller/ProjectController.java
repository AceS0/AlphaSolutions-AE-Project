package com.example.alphasolutionsaeproject.controller;

import com.example.alphasolutionsaeproject.model.Project;
import com.example.alphasolutionsaeproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.alphasolutionsaeproject.service.ProjectService;

import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;

    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("email") != null;
    }

    @GetMapping
    public String listProjects(Model model, HttpSession session) {
        String mail = (String) session.getAttribute("email");

        if (!isLoggedIn(session)) {
            return "redirect:/users/login";
        }

        int userId = userService.getUserIdByMail(mail);
        List<Project> projects = projectService.getAllProjectsByUserId(userId);
        model.addAttribute("projects", projects);
        return "projects";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("project", new Project());
        return "addProject";
    }

    @PostMapping("/add")
    public String addProject(@ModelAttribute("project") Project project) {
        projectService.addProject(project);
        return "redirect:/projects";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Project project = projectService.getProjectById(id);
        model.addAttribute("project", project);
        return "editProject";
    }

    @PostMapping("/edit/{id}")
    public String editProject(@PathVariable int id, @ModelAttribute("project") Project project) {
        project.setId(id);
        projectService.updateProject(project);
        return "redirect:/projects";
    }

    @GetMapping("/delete/{id}")
    public String deleteProject(@PathVariable int id) {
        projectService.deleteProject(id);
        return "redirect:/projects";
    }

    @PostMapping("/toggleChecked/{id}")
    public String toggleChecked(@PathVariable int id) {
        projectService.toggleChecked(id);
        return "redirect:/projects";  // Redirect back to the projects list
    }

    @GetMapping("/projects/{id}")
    public String getProjectManager(@PathVariable int id, Model model) {
        // Get the username of the user who created the project
        String createdByUsername = projectService.getProjectCreatedByUsername(id);

        model.addAttribute("createdByUsername", createdByUsername);

        return "projects";  // Return the appropriate view
    }
}


