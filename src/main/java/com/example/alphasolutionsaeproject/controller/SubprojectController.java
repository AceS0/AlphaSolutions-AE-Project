package com.example.alphasolutionsaeproject.controller;
import com.example.alphasolutionsaeproject.model.Role;
import com.example.alphasolutionsaeproject.model.Subproject;
import com.example.alphasolutionsaeproject.model.User;
import com.example.alphasolutionsaeproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.alphasolutionsaeproject.service.SubprojectService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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
            return "PM/projectsPM";
        }

        return "Admin/subprojectsAdmin";
    }


    // 2. Vis form for at tilføje et subproject
    @GetMapping("projects/{pid}/subprojects/add")
    public String showAddForm(@PathVariable int pid, Model model, HttpSession session) {
        if (!isLoggedIn(session)){
            return "redirect:/users/login";
        }

        model.addAttribute("subproject", new Subproject());
        return "CommonProjects/addSubproject";
    }

    // 3. Gem nyt subproject
    @PostMapping("projects/{pid}/subprojects/save")
    public String addSubproject(@ModelAttribute("subproject") Subproject subproject, @PathVariable int pid, RedirectAttributes redirectAttributes) {
        subproject.setProjectId(pid);
        subproject.setChecked(false);
        if (subproject.getPriority() > 5){
            redirectAttributes.addFlashAttribute("error", "Priority should be between 1-5.");
            return "redirect:/projects/{pid}/subprojects/add";
        }
        subprojectService.addSubproject(subproject);
        return "redirect:/projects/{pid}/subprojects";
    }

    // 4. Vis form for at redigere et subproject
    @GetMapping("projects/{pid}/subprojects/edit/{spid}")
    public String showEditForm(@PathVariable int spid, Model model, HttpSession session) {
        if (!isLoggedIn(session)){
            return "redirect:/users/login";
        }

        Subproject subproject = subprojectService.getSubprojectById(spid);
        model.addAttribute("subproject", subproject);
        return "CommonProjects/editSubproject";
    }

    // 5. Gem ændringer på eksisterende subproject
    @PostMapping("projects/{pid}/subprojects/edit/{spid}/save")
    public String editSubproject(@PathVariable int pid,@PathVariable int spid, @ModelAttribute("subproject") Subproject subproject, RedirectAttributes redirectAttributes) {
        subproject.setChecked(false);
        if (subproject.getPriority() > 5){
            redirectAttributes.addFlashAttribute("error", "Priority should be between 1-5.");
            return "redirect:/projects/{pid}/subprojects/edit/{spid}";
        }

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
