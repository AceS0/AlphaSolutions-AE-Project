package controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/projects")


public class ProjectController {
    private List<String> projects = new ArrayList<>();

    // 1. Læs (se) alle projekter
    @GetMapping
    public String getAllProjects(Model model) {
        model.addAttribute("projects", projects);
        return "projects"; // Her skal du have en Thymeleaf-fil: projects.html
    }

    // 2. Form til at oprette et nyt projekt
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("project", new String());
        return "addProject"; // Her skal du have en Thymeleaf-fil: addProject.html
    }

    // 3. Gem et nyt projekt
    @PostMapping("/add")
    public String addProject(@ModelAttribute("project") String project) {
        projects.add(project);
        return "redirect:/projects";
    }

    // 4. Form til at redigere et eksisterende projekt
    @GetMapping("/edit/{index}")
    public String showEditForm(@PathVariable int index, Model model) {
        String project = projects.get(index);
        model.addAttribute("project", project);
        model.addAttribute("index", index);
        return "editProject"; // Her skal du have en Thymeleaf-fil: editProject.html
    }

    // 5. Gem ændringerne efter redigering
    @PostMapping("/edit/{index}")
    public String editProject(@PathVariable int index, @ModelAttribute("project") String project) {
        projects.set(index, project);
        return "redirect:/projects";
    }

    // 6. Slet et projekt
    @GetMapping("/delete/{index}")
    public String deleteProject(@PathVariable int index) {
        projects.remove(index);
        return "redirect:/projects";
    }
}

