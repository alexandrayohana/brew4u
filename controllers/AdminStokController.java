package com.impal.demo_brew4u.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.impal.demo_brew4u.models.Admin;
import com.impal.demo_brew4u.models.Menu;
import com.impal.demo_brew4u.repositories.MenuRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminStokController {

    @Autowired
    private MenuRepository menuRepository; // Pake MenuRepository, bukan StokRepository

    // 1. TAMPILKAN HALAMAN STOK
    @GetMapping("/stok")
    public String viewStokPage(Model model, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("adminLog");
        if (admin == null) return "redirect:/login";

        // Ambil data MENU berdasarkan cabang (karena stok nempel di tabel menu)
        // Pastikan getter admin.getKodeCabang() mengembalikan Long/Integer sesuai MenuRepository
        model.addAttribute("listMenu", menuRepository.findByKode_cabang(admin.getKodeCabang()));
        
        return "admin/stok"; // Mengarah ke stok.html
    }

    // 2. API: AMBIL DETAIL STOK (Untuk Modal Edit)
    @GetMapping("/api/stok-menu/{id}")
    @ResponseBody
    public Menu getMenuStokDetail(@PathVariable long id) {
        // Ambil data menu lengkap (termasuk stok saat ini)
        return menuRepository.findById(id).orElse(null);
    }

    // 3. UPDATE STOK
    @PostMapping("/stok/update")
    public String updateStok(@RequestParam("kode_menu") Long kodeMenu,
                             @RequestParam("stok") Integer stokBaru,
                             HttpSession session) {
        
        Admin admin = (Admin) session.getAttribute("adminLog");
        if (admin == null) return "redirect:/login";

        // Cari menu berdasarkan ID
        Menu menu = menuRepository.findById(kodeMenu).orElse(null);
        
        if (menu != null) {
            // Validasi: Pastikan menu ini milik cabang admin yang login (Keamanan)
            // (Asumsi tipe data kode_cabang sudah sama-sama Long)
            if (menu.getKode_cabang().equals(admin.getKodeCabang())) {
                menu.setStok(stokBaru + menu.getStok()); // Update kolom stok saja
                menuRepository.save(menu);
                System.out.println("Stok updated untuk: " + menu.getNama_menu());
            }
        }
        
        return "redirect:/admin/stok";
    }
}
