package com.impal.demo_brew4u.controllers;

import com.impal.demo_brew4u.models.Pelanggan;
import com.impal.demo_brew4u.repositories.PelangganRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // Untuk pesan sukses

@Controller
public class ProfileController {

    @Autowired
    private PelangganRepository pelangganRepository;

    @GetMapping("/profile")
    public String showProfilePage(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String email = authentication.getName();
        Pelanggan pelanggan = pelangganRepository.findByEmail(email);

        model.addAttribute("pelanggan", pelanggan);
        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(
            Authentication authentication, 
            @ModelAttribute Pelanggan formPelanggan, 
            RedirectAttributes redirectAttributes) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

       
        String email = authentication.getName();
        Pelanggan currentUser = pelangganRepository.findByEmail(email);

        if (currentUser != null) {
            currentUser.setNama_pelanggan(formPelanggan.getNama_pelanggan());
            pelangganRepository.save(currentUser);

            redirectAttributes.addFlashAttribute("successMessage", "Profil berhasil diperbarui!");
        }

        return "redirect:/profile";
    }
}
