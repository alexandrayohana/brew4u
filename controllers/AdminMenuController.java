package com.impal.demo_brew4u.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.impal.demo_brew4u.models.Admin;
import com.impal.demo_brew4u.models.Menu;
import com.impal.demo_brew4u.models.Rekomendasi;
import com.impal.demo_brew4u.repositories.MenuRepository;
import com.impal.demo_brew4u.repositories.RekomendasiRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/admin")
public class AdminMenuController {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RekomendasiRepository rekomendasiRepository;

    // ==========================================
    // 1. TAMPILKAN HALAMAN MENU (READ)
    // ==========================================
    @GetMapping("/menu")
    public String viewMenuPage(Model model, HttpSession session) {
        // 1. Cek Login Session
        Admin admin = (Admin) session.getAttribute("adminLog");
        if (admin == null) {
            return "redirect:/login";
        }

        // 2. Ambil Menu Sesuai Cabang Admin
        // Pastikan getter di Admin.java sesuai (getKodeCabang atau getKode_cabang)
        List<Menu> menus = menuRepository.findByKode_cabang(admin.getKodeCabang());

        // 3. LOGIC REKOMENDASI:
        // Cek ke tabel rekomendasi, apakah menu ini terdaftar?
        // Jika ya, set isRecommended = true (agar bintang muncul di HTML)
        for (Menu m : menus) {
            if (rekomendasiRepository.existsByKode_menu(m.getKode_menu())) {
                m.setIsRecommended(true);
            } else {
                m.setIsRecommended(false);
            }
        }

        model.addAttribute("listMenu", menus);
        return "admin/menu";
    }

    // ==========================================
    // 2. API: AMBIL DETAIL UNTUK EDIT (JSON)
    // ==========================================
    @GetMapping("/api/menu/{id}")
    @ResponseBody
    public Menu getMenuDetail(@PathVariable long id) {
        // Ambil data menu
        Menu menu = menuRepository.findById(id).orElse(null);
        
        if (menu != null) {
            // Cek status rekomendasi di database agar checkbox di Modal tercentang otomatis
            boolean isRekom = rekomendasiRepository.existsByKode_menu(menu.getKode_menu());
            menu.setIsRecommended(isRekom);
        }
        
        return menu;
    }

    // ==========================================
    // 3. SIMPAN MENU BARU / UPDATE
    // ==========================================
    @PostMapping("/menu/save")
    @Transactional // Menjamin data Menu dan Rekomendasi tersimpan bersamaan atau batal semua jika error
    public String saveMenu(@ModelAttribute Menu menu, 
                           @RequestParam(value = "isRecommended", required = false) boolean isRecommended,
                           HttpSession session) {
        
        // 1. Cek Login
        Admin admin = (Admin) session.getAttribute("adminLog");
        if (admin == null) return "redirect:/login";

        // 2. Set Kode Cabang Otomatis
        menu.setKode_cabang(admin.getKodeCabang());
        
        // 3. Simpan ke Tabel Menu dulu (Insert/Update)
        Menu savedMenu = menuRepository.save(menu);

        // 4. LOGIC UPDATE TABEL REKOMENDASI
        Long kodeMenu = savedMenu.getKode_menu();

        if (isRecommended) {
            // A. Jika Admin mencentang "Rekomendasi"
            // Cek dulu, kalau belum ada di tabel rekomendasi, baru simpan (hindari duplikat)
            if (!rekomendasiRepository.existsByKode_menu(kodeMenu)) {
                Rekomendasi rek = new Rekomendasi(kodeMenu);
                rekomendasiRepository.save(rek);
            }
        } else {
            // B. Jika Admin TIDAK mencentang (atau menghapus centang)
            // Cari datanya, jika ada -> HAPUS.
            Optional<Rekomendasi> rek = rekomendasiRepository.findByKode_menu(kodeMenu);
            if (rek.isPresent()) {
                rekomendasiRepository.delete(rek.get());
            }
        }
        
        return "redirect:/admin/menu";
    }

    // ==========================================
    // 4. HAPUS MENU (DELETE)
    // ==========================================
    @GetMapping("/menu/delete/{id}")
    @Transactional // Penting agar penghapusan bersih
    public String deleteMenu(@PathVariable long id, HttpSession session) {
        // 1. Cek Login
        Admin admin = (Admin) session.getAttribute("adminLog");
        if (admin == null) return "redirect:/login";

        // 2. Validasi Keamanan (Cek apakah menu milik cabang admin ini)
        Menu menu = menuRepository.findById(id).orElse(null);
        
        if (menu != null && menu.getKode_cabang().equals(admin.getKodeCabang())) {
            
            // A. Hapus dulu dari tabel rekomendasi (jika ada) agar tidak error foreign key
            Optional<Rekomendasi> rek = rekomendasiRepository.findByKode_menu(id);
            if (rek.isPresent()) {
                rekomendasiRepository.delete(rek.get());
            }

            // B. Hapus dari tabel menu
            menuRepository.deleteById(id);
            
            System.out.println("Sukses menghapus menu & rekomendasi: " + menu.getNama_menu());
        } else {
             System.out.println("Gagal Hapus: Menu tidak ditemukan atau beda cabang.");
        }

        return "redirect:/admin/menu";
    }
}
