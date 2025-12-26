package com.impal.demo_brew4u.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.impal.demo_brew4u.models.Pelanggan;
import com.impal.demo_brew4u.repositories.PelangganRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class PelangganController {

    private final PelangganRepository pelangganRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PelangganController(PelangganRepository pelangganRepository, PasswordEncoder passwordEncoder) {
        this.pelangganRepository = pelangganRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // --- REGISTER ---
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("pelanggan", new Pelanggan());
        return "register"; 
    }

    @PostMapping("/register")
    public String registerPelanggan(@ModelAttribute Pelanggan pelanggan) {
        // Enkripsi password saat register
        String hashedPassword = passwordEncoder.encode(pelanggan.getPassword());
        pelanggan.setPassword(hashedPassword);
        pelangganRepository.save(pelanggan);
        return "redirect:/login?registered"; 
    }
    
    // --- LOGIN (Cuma Tampilkan Halaman) ---
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // ❌ HAPUS BAGIAN @PostMapping("/login") atau "/proses-login"
    // Biarkan Spring Security yang menangani POST-nya secara otomatis.

    // --- LOGOUT ---
    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }
}
