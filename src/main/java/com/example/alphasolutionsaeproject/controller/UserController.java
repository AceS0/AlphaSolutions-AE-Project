package com.example.alphasolutionsaeproject.controller;

import com.example.alphasolutionsaeproject.model.Project;
import com.example.alphasolutionsaeproject.model.Role;
import com.example.alphasolutionsaeproject.model.User;
import com.example.alphasolutionsaeproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequestMapping("")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("email") != null;
    }

    @GetMapping("/users/login")
    public String viewLogin(){
        return "UserAuth/login";  // Viser login-siden
    }

    @PostMapping("/users/login")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password,
                        HttpSession session, Model model){
        if (userService.login(email, password)){
            session.setAttribute("email", email); // Gemmer email i sessionen
            session.setMaxInactiveInterval(600);  // Timeout efter 10 minutter
            User user = userService.getUserByMail(email);
            if (user.getRole().equals(Role.ADMIN)){
                return "redirect:/admin";
            }
            return "redirect:/projects";  // Omdirigerer til projects-siden
        }

        model.addAttribute("wrongCredentials", true);  // Fejl ved login
        return "UserAuth/login";  // Vender tilbage til login-siden
    }

    @GetMapping("/admin/users/create")
    public String viewRegister(Model model, HttpSession session){
        if(!isLoggedIn(session)){
            return "redirect:/users/login";
        }
        return "Admin/createUser";  // Viser registreringssiden
    }

    @PostMapping("/admin/users/create")
    public String register(@RequestParam("email") String email, @RequestParam("username") String username, @RequestParam("password") String password,
                           @RequestParam("role") String role, RedirectAttributes redirectAttributes){

        // Validerer input (email, brugernavn, password)
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")){
            redirectAttributes.addFlashAttribute("error", "Email isn't correct.");
            return "redirect:/users/create";
        }

        if (username.length() < 3){
            redirectAttributes.addFlashAttribute("error", "Username is too short.");
            return "redirect:/users/create";
        }

        if (password.length() < 8){
            redirectAttributes.addFlashAttribute("error","Password is too short.");
            return "redirect:/users/create";
        }

        if (!userService.register(email, username, password, role)) {
            redirectAttributes.addFlashAttribute("error", "Account already exists.");
            return "redirect:/users/create";
        }

        return "redirect:/admin/users"  ;  // Omdirigerer til Admin siden af users.
    }

    @GetMapping("/admin/users/edit/{uid}")
    public String showEditForm(@PathVariable int uid, Model model, HttpSession session) {
        if (!isLoggedIn(session)){
            return "redirect:/users/login";
        }

        User user = userService.getUserById(uid);
        model.addAttribute("user",user);
        return "Admin/editUser";
    }

    @PostMapping("/admin/users/edit/{uid}")
    public String editUser(@ModelAttribute("user") User user) {
        userService.updateUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users")
    public String showAdminUserPage(Model model, HttpSession session){
        if(session.getAttribute("email") == null){
            return "redirect:/users/login";
        }

        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "Admin/adminUsersPage";
    }

    @PostMapping("/admin/users/delete/{id}")
    public String deleteusers(@PathVariable int id) {
        userService.deleteusers(id);
        return "redirect:/admin/users";
    }


}
