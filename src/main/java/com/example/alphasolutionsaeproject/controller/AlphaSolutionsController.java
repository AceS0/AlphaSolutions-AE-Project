package com.example.alphasolutionsaeproject.controller;


import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class AlphaSolutionsController {



    @GetMapping("/")
    public String showHomePage(HttpSession session) {
        session.invalidate();
        return "index";
    }
}
