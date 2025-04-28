package com.example.alphasolutionsaeproject.controller;

import com.example.alphasolutionsaeproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String viewLogin(){
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("eid") String eid, @RequestParam("uid") String uid, @RequestParam("pw") String pw,
                        HttpSession session, Model model){
        if (userService.login(eid,pw)){
            session.setAttribute("email",eid);
            session.setMaxInactiveInterval(600);
            return "redirect:/projects";
        }

        model.addAttribute("wrongCredentials", true);
        return "login";
    }

    @GetMapping("/register")
    public String viewRegister(){
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam("eid") String eid, @RequestParam("uid") String uid, @RequestParam("pw") String pw,
                           RedirectAttributes redirectAttributes){

        if (!eid.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")){
            redirectAttributes.addFlashAttribute("error", "Email isn't correct. Must be a real email address.");
            return "redirect:/register";
        }

        if (uid.length() < 3){
            redirectAttributes.addFlashAttribute("error", "Username is too short. Must be at least 3 characters.");
            return "redirect:/register";
        }

        if (pw.length() < 8){
            redirectAttributes.addFlashAttribute("error","Password is too short. Must be at least 8 characters.");
            return "redirect:/register";
        }

        if (!userService.register(eid,uid, pw)) {
            redirectAttributes.addFlashAttribute("error", "Account already exists.");
            return "redirect:/register";
        }

        return "redirect:/login";
    }

}
