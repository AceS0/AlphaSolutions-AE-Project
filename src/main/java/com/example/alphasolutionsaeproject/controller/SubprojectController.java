package com.example.alphasolutionsaeproject.controller;
import com.example.alphasolutionsaeproject.model.Project;
import com.example.alphasolutionsaeproject.model.Role;
import com.example.alphasolutionsaeproject.model.Subproject;
import com.example.alphasolutionsaeproject.model.User;
import com.example.alphasolutionsaeproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.alphasolutionsaeproject.service.SubprojectService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("")
public class SubprojectController {

    private final SubprojectService subprojectService;
    private final UserService userService;


    public SubprojectController(SubprojectService subprojectService, UserService userService) {
        this.subprojectService = subprojectService;
        this.userService = userService;
    }

    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("email") != null;
    }

    @GetMapping("projects/{pid}/subprojects")
    public String listSubprojects(@PathVariable int pid, Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/users/login";
        }
        String mail = (String) session.getAttribute("email");
        User user  = userService.getUserByMail(mail);
        List<Subproject> subprojects = subprojectService.getAllSubprojectsByProjectId(pid);
        model.addAttribute("subprojects", subprojects);
        if (user.getRole().equals(Role.EMPLOYEE)){
            return "Employee/subprojects";
        }

        if (user.getRole().equals(Role.PM)){
            return "PM/subprojectsPM";
        }

        return "Admin/subprojectsAdmin";
    }


    // 2. Vis form for at tilføje et subproject
    @GetMapping("projects/{pid}/subprojects/add")
    public String showAddForm(Model model) {
        model.addAttribute("subproject", new Subproject());
        return "addSubproject";
    }

    // 3. Gem nyt subproject
    @PostMapping("projects/{pid}/subprojects/add")
    public String addSubproject(@ModelAttribute Subproject subproject) {
        subprojectService.addSubproject(subproject);
        return "redirect:/subprojects";
    }

    // 4. Vis form for at redigere et subproject
    @GetMapping("projects/{pid}/subprojects/edit/{spid}")
    public String showEditForm(@PathVariable int spid, Model model) {
        Subproject subproject = subprojectService.getSubprojectById(spid);
        model.addAttribute("subproject", subproject);
        return "editSubproject";
    }

    // 5. Gem ændringer på eksisterende subproject
    @PostMapping("projects/{pid}/subprojects/edit/{spid}")
    public String editSubproject(@PathVariable int spid, @ModelAttribute Subproject subproject) {
        subproject.setId(spid);
        subprojectService.updateSubproject(subproject);
        return "redirect:/subprojects";
    }

    // 6. Slet et subproject
    @PostMapping("/projects/{pid}/subprojects/delete/{spid}")
    public String deleteSubproject(@PathVariable int pid, @PathVariable int spid) {
        subprojectService.deleteSubproject(spid);
        return "redirect:/projects/{pid}/subprojects";
    }
}
