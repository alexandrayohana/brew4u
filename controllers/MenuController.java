package com.impal.demo_brew4u.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller; // 1. JANGAN LUPA IMPORT INI
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.impal.demo_brew4u.models.Cabang;
import com.impal.demo_brew4u.models.Menu;
import com.impal.demo_brew4u.repositories.CabangRepository;
import com.impal.demo_brew4u.repositories.MenuRepository;
import com.impal.demo_brew4u.repositories.RekomendasiRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class MenuController {

    private final MenuRepository menuRepository;
    private final CabangRepository cabangRepository;
    private final RekomendasiRepository rekomendasiRepository; // 2. DEKLARASIKAN VARIABEL DI SINI

    @Autowired
    public MenuController(MenuRepository menuRepository, 
                          CabangRepository cabangRepository,
                          RekomendasiRepository rekomendasiRepository) { // 3. TAMBAHKAN DI CONSTRUCTOR
        this.menuRepository = menuRepository;
        this.cabangRepository = cabangRepository;
        this.rekomendasiRepository = rekomendasiRepository; // 4. INISIALISASI DI SINI
    }

    @GetMapping("/menu")
    @SuppressWarnings("unchecked")
    public String showMenuPage(
            @RequestParam(value = "kodeCabang", required = false) Long kodeCabang, 
            Model model,
            HttpSession session) { 
        
        // 1. Ambil semua cabang untuk dropdown
        List<Cabang> listCabang = cabangRepository.findAll();
        model.addAttribute("branches", listCabang);

        // 2. Tentukan cabang yang aktif
        Long selectedCabangCode = kodeCabang;
        if (selectedCabangCode == null && !listCabang.isEmpty()) {
            selectedCabangCode = listCabang.get(0).getKode_cabang();
        }

        // Simpan ke Session
        session.setAttribute("currentCabangId", selectedCabangCode);

        // 3. Ambil Menu
        List<Menu> menuItems = menuRepository.findByKode_cabang(selectedCabangCode);
        
        // --- LOGIC REKOMENDASI (Sekarang tidak akan error lagi) ---
        for (Menu m : menuItems) {
            // Cek apakah ada di tabel rekomendasi
            if (rekomendasiRepository.existsByKode_menu(m.getKode_menu())) {
                m.setIsRecommended(true);
            } else {
                m.setIsRecommended(false);
            }
        }
        // ---------------------------------------------------------

        // 4. Ambil Keranjang
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }

        // 5. Kirim ke View
        model.addAttribute("menuItems", menuItems);
        model.addAttribute("selectedCabangCode", selectedCabangCode);
        model.addAttribute("cartQuantities", cart);
        
        return "menu";
    }
}
