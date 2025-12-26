package com.impal.demo_brew4u.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    // Ini untuk halaman dashboard utama (Home Admin)
    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard"; // Pastikan file dashboard.html / dashboard.jsp ada
    }
}
