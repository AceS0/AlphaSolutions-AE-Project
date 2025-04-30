package com.example.alphasolutionsaeproject.controller;

import com.example.alphasolutionsaeproject.model.Project;
import com.example.alphasolutionsaeproject.model.Role;
import com.example.alphasolutionsaeproject.model.User;
import com.example.alphasolutionsaeproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.alphasolutionsaeproject.service.ProjectService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        if (!isLoggedIn(session)) {
            return "redirect:/users/login";
        }

        String mail = (String) session.getAttribute("email");
        User user  = userService.getUserByMail(mail);
        // Projects the user created
        List<Project> createdProjects = projectService.getAllProjectsByUserId(user.getId());

        // Projects the user is assigned to
        List<Project> sharedProjects = projectService.getSharedProjectsByUserId(user.getId());

        // Combine both lists (optional: remove duplicates)
        Set<Project> allUserProjects = new HashSet<>();
        allUserProjects.addAll(createdProjects);
        allUserProjects.addAll(sharedProjects);

        for (Project project : allUserProjects) {
            User projectManager = userService.getUserById(project.getCreatedBy());
            project.setProjectManager(projectManager.getUsername());
        }


        model.addAttribute("projects", allUserProjects);
        if (user.getRole().equals(Role.EMPLOYEE)){
            return "Employee/projects";
        }

        if (user.getRole().equals(Role.PM)){
            return "PM/projectsPM";
        }

        List<Project> allProjects = projectService.getAllProjects();
        for (Project project : allProjects) {
            User projectManager = userService.getUserById(project.getCreatedBy()); // assumes createdBy is userId
            project.setProjectManager(projectManager.getUsername()); // store creator's name in each project
        }
        model.addAttribute("allProjects", allProjects);

        return "Admin/projectsAdmin";
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
}


