package controller;

import model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.ProjectService;

import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public String listProjects(Model model) {
        List<Project> projects = projectService.getAllProjects();
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
}


