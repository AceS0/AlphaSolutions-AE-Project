package com.example.alphasolutionsaeproject.controller;

import com.example.alphasolutionsaeproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String viewLogin(){
        return "/UserAuth/login";  // Viser login-siden
    }

    @PostMapping("/login")
    public String login(@RequestParam("eid") String eid, @RequestParam("uid") String uid, @RequestParam("pw") String pw,
                        HttpSession session, Model model){
        if (userService.login(eid, pw)){
            session.setAttribute("email", eid);  // Gemmer email i sessionen
            session.setMaxInactiveInterval(600);  // Timeout efter 10 minutter
            return "redirect:/projects";  // Omdirigerer til projects-siden
        }

        model.addAttribute("wrongCredentials", true);  // Fejl ved login
        return "/UserAuth/login";  // Vender tilbage til login-siden
    }

    @GetMapping("/register")
    public String viewRegister(){
        return "/UserAuth/register";  // Viser registreringssiden
    }

    @PostMapping("/register")
    public String register(@RequestParam("eid") String eid, @RequestParam("uid") String uid, @RequestParam("pw") String pw,
                           RedirectAttributes redirectAttributes){

        // Validerer input (email, brugernavn, password)
        if (!eid.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")){
            redirectAttributes.addFlashAttribute("error", "Email isn't correct.");
            return "redirect:/users/register";
        }

        if (uid.length() < 3){
            redirectAttributes.addFlashAttribute("error", "Username is too short.");
            return "redirect:/users/register";
        }

        if (pw.length() < 8){
            redirectAttributes.addFlashAttribute("error","Password is too short.");
            return "redirect:/users/register";
        }

        if (!userService.register(eid, uid, pw)) {
            redirectAttributes.addFlashAttribute("error", "Account already exists.");
            return "redirect:/users/register";
        }

        return "redirect:/users/login";  // Omdirigerer til login-siden
    }
}
