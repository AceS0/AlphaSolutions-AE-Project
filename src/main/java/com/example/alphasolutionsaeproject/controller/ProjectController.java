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
import org.springframework.web.bind.support.SessionStatus;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("")
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

    private boolean redirectEmployee(HttpSession session){
        String mail = (String) session.getAttribute("email");
        User me = userService.getUserByMail(mail);
        return me.getRole().equals(Role.EMPLOYEE);
    }

    @GetMapping("/")
    public String showHomePage(HttpSession session) {
        session.invalidate();
        return "index";
    }

    @GetMapping("/admin")
    public String showAdminPage(HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/users/login";
        }
        String mail = (String) session.getAttribute("email");
        User me = userService.getUserByMail(mail);
        if (!me.getRole().equals(Role.ADMIN)) {
        return "redirect:/";
        }
        return "Admin/adminPage";
    }


    @GetMapping("/projects")
    public String listProjects(Model model, HttpSession session, SessionStatus status) {
        status.setComplete();
        if (!isLoggedIn(session)) {
            return "redirect:/users/login";
        }

        String mail = (String) session.getAttribute("email");
        User user = userService.getUserByMail(mail);
        List<Project> createdProjects = projectService.getAllProjectsByUserId(user.getId());

        List<Project> sharedProjects = projectService.getSharedProjectsByUserId(user.getId());

        Set<Project> allUserProjects = new HashSet<>();
        allUserProjects.addAll(createdProjects);
        allUserProjects.addAll(sharedProjects);

        for (Project project : allUserProjects) {
            User projectManager = userService.getUserById(project.getCreatedBy());
            project.setProjectManager(projectManager.getUsername());
        }


        if (user.getRole().equals(Role.EMPLOYEE)) {
            model.addAttribute("projects", sharedProjects);

            return "Employee/projects";
        }

        if (user.getRole().equals(Role.PM)) {
            model.addAttribute("allProjects", createdProjects);

            return "PM/projects";
        }

        List<Project> allProjects = projectService.getAllProjects();
        for (Project project : allProjects) {
            User projectManager = userService.getUserById(project.getCreatedBy());
            project.setProjectManager(projectManager.getUsername());
        }
        model.addAttribute("allProjects", allProjects);

        return "Admin/projectsAdmin";
    }

    @GetMapping("/projects/add")
    public String showAddForm(Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/users/login";
        }
        if (redirectEmployee(session)) {
            return "redirect:/projects";
        }
        List<User> users = userService.getAllPms("PM");
        model.addAttribute("users", users);
        model.addAttribute("project", new Project());
        return "CommonProjects/addProject";
    }

    @PostMapping("/projects/save")
    public String addProject(@ModelAttribute("project") Project project) {
        String getCreatedBy = project.getProjectManager();
        int createdBy = projectService.getProjectManagerId(getCreatedBy);
        project.setCreatedBy(createdBy);
        project.setChecked(false);
        projectService.addProject(project);
        return "redirect:/projects";
    }

    @GetMapping("/projects/edit/{pid}")
    public String showEditForm(@PathVariable int pid, Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/users/login";
        }
        if (redirectEmployee(session)) {
            return "redirect:/projects";
        }
        List<User> users = userService.getAllPms("PM");
        model.addAttribute("users", users);

        Project project = projectService.getProjectById(pid);
        model.addAttribute("project", project);
        return "CommonProjects/editProject";
    }

    @PostMapping("/projects/edit/{pid}")
    public String editProject(@PathVariable int pid, @ModelAttribute("project") Project project) {
        String getCreatedBy = project.getProjectManager();
        int createdBy = projectService.getProjectManagerId(getCreatedBy);
        project.setCreatedBy(createdBy);
        project.setChecked(false);
        projectService.updateProject(project, pid);
        return "redirect:/projects";
    }


    @PostMapping("/projects/delete/{pid}")
    public String deleteProject(@PathVariable int pid) {
        projectService.deleteProject(pid);
        return "redirect:/projects";
    }

    @PostMapping("/projects/toggleChecked/{pid}")
    public String toggleChecked(@PathVariable int pid) {
        projectService.toggleCheckedAndCascade(pid);
        return "redirect:/projects";
    }

    @GetMapping("/projects/{pid}/assign/workers")
    public String getAllWorkers(@PathVariable int pid, Model model, HttpSession session){
        if(session.getAttribute("email") == null){
            return "redirect:/users/login";
        }
        if (redirectEmployee(session)) {
            return "redirect:/projects";
        }
        List<User> AssignedEmployees = userService.getAllAssigned(pid);
        model.addAttribute("AssignedEmployees", AssignedEmployees);


        List<User> UnassignedEmployees = userService.getAllUnassigned(pid);
        model.addAttribute("UnassignedEmployees", UnassignedEmployees);
        return "Admin/assignProjectEmployees";
    }

    @PostMapping("/projects/{pid}/assign/workers/{userId}")
    public String assignToProject(@PathVariable int pid, @PathVariable int userId) {
        projectService.assignToProject(pid, userId);
        return "redirect:/projects/{pid}/assign/workers";
    }

    @PostMapping("/projects/{pid}/unassign/workers/{userId}")
    public String unassignFromProject(@PathVariable int pid, @PathVariable int userId) {
        projectService.unassignFromProject(pid, userId);
        return "redirect:/projects/{pid}/assign/workers";
    }


}


