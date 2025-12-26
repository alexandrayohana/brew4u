package com.impal.demo_brew4u.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.impal.demo_brew4u.models.Pelanggan;
import com.impal.demo_brew4u.repositories.PelangganRepository;
import com.impal.demo_brew4u.repositories.PesananRepository;

@Controller
public class RiwayatController {

    @Autowired
    private PesananRepository pesananRepository;

    @Autowired
    private PelangganRepository pelangganRepository;

    @GetMapping("/konfirmasi_sukses")
    public String tampilkanRiwayat(Model model, Authentication authentication) {
        // 1. Cek Login
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        // 2. Ambil Data Pelanggan yang Login
        String email = authentication.getName();
        Pelanggan user = pelangganRepository.findByEmail(email);

        // 3. Ambil Pesanan Milik User Tersebut (Pakai method baru tadi)
        model.addAttribute("listRiwayat", pesananRepository.findByPelangganOrderByTanggalPesananDesc(user));
        
        // 4. Kirim Info User (untuk Navbar dsb)
        model.addAttribute("user", user);

        return "konfirmasi_sukses"; // Nama file HTML
    }
}
