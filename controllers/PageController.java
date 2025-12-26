package com.impal.demo_brew4u.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/dashboard")
    public String showDashboard() {
        return "dashboard"; // Mengarah ke dashboard.html
    }

    
}
