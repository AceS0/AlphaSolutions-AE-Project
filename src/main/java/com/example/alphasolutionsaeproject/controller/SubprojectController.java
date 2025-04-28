package com.example.alphasolutionsaeproject.controller;
import com.example.alphasolutionsaeproject.model.Subproject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.alphasolutionsaeproject.service.SubprojectService;
import java.util.List;

@Controller
@RequestMapping("/subprojects")
public class SubprojectController {

    private final SubprojectService subprojectService;

    public SubprojectController(SubprojectService subprojectService) {
        this.subprojectService = subprojectService;
    }

    // 1. Vis alle subprojects
    @GetMapping
    public String listSubprojects(Model model) {
        List<Subproject> subprojects = subprojectService.getAllSubprojects();
        model.addAttribute("subprojects", subprojects);
        return "subprojects";
    }

    // 2. Vis form for at tilføje et subproject
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("subproject", new Subproject());
        return "addSubproject";
    }

    // 3. Gem nyt subproject
    @PostMapping("/add")
    public String addSubproject(@ModelAttribute Subproject subproject) {
        subprojectService.addSubproject(subproject);
        return "redirect:/subprojects";
    }

    // 4. Vis form for at redigere et subproject
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Subproject subproject = subprojectService.getSubprojectById(id);
        model.addAttribute("subproject", subproject);
        return "editSubproject";
    }

    // 5. Gem ændringer på eksisterende subproject
    @PostMapping("/edit/{id}")
    public String editSubproject(@PathVariable int id, @ModelAttribute Subproject subproject) {
        subproject.setId(id);
        subprojectService.updateSubproject(subproject);
        return "redirect:/subprojects";
    }

    // 6. Slet et subproject
    @GetMapping("/delete/{id}")
    public String deleteSubproject(@PathVariable int id) {
        subprojectService.deleteSubproject(id);
        return "redirect:/subprojects";
    }
}
