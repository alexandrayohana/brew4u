package com.impal.demo_brew4u.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.impal.demo_brew4u.models.DetailPesanan;
import com.impal.demo_brew4u.models.Menu;
import com.impal.demo_brew4u.models.Pesanan;
import com.impal.demo_brew4u.repositories.DetailPesananRepository;
import com.impal.demo_brew4u.repositories.MenuRepository;
import com.impal.demo_brew4u.repositories.PesananRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminPesananController {

    @Autowired private PesananRepository pesananRepository;
    @Autowired private DetailPesananRepository detailPesananRepository;
    @Autowired private MenuRepository menuRepository;

    // 1. HALAMAN LIST (DIFILTER PER CABANG)
    @GetMapping("/pesanan")
    public String viewPesananPage(Model model, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("adminLog");
        if (admin == null) return "redirect:/login";

        // Filter berdasarkan Cabang Admin
        // Pastikan Admin punya relasi getCabang() atau getIdCabang()
        if (admin.getKodeCabang() != null) {
            Long idCabangAdmin = admin.getKodeCabang();
            model.addAttribute("listPesanan", pesananRepository.findByKodeCabangOrderByTanggalPesananDesc(idCabangAdmin));
        } else {
            // Fallback jika admin tidak punya cabang (tampilkan semua)
            model.addAttribute("listPesanan", pesananRepository.findAllByOrderByTanggalPesananDesc());
        }
        
        return "admin/pesanan"; 
    }

    // 2. API DETAIL (PAKET KOMPLIT HEADER + ITEMS)
    @GetMapping("/api/pesanan/{id}")
    @ResponseBody
    public Map<String, Object> getPesananDetail(@PathVariable long id) {
        Map<String, Object> response = new HashMap<>();
        
        Pesanan pesanan = pesananRepository.findById(id).orElse(null);
        List<DetailPesanan> details = detailPesananRepository.findByPesanan(pesanan);

        response.put("header", pesanan); 
        response.put("items", details);  
        
        return response;
    }

    // 3. UPDATE STATUS PENYAJIAN & POTONG STOK
    @PostMapping("/pesanan/update")
    public String updateStatus(@RequestParam("kodePesanan") long kodePesanan, 
                               @RequestParam("status") String status) {
        
        Pesanan pesanan = pesananRepository.findById(kodePesanan).orElse(null);
        
        if (pesanan != null) {
            String statusLama = pesanan.getStatus();
            pesanan.setStatus(status); // Update status masak (Belum/Sedang/Siap)
            pesananRepository.save(pesanan);

            // Stok berkurang jika status jadi 'Siap'
            if ("Siap".equalsIgnoreCase(status) && !"Siap".equalsIgnoreCase(statusLama)) {
                List<DetailPesanan> items = detailPesananRepository.findByPesanan(pesanan);
                for (DetailPesanan item : items) {
                    Menu menu = item.getMenu();
                    if (menu != null) {
                        int stokBaru = menu.getStok() - item.getQuantity();
                        if (stokBaru < 0) stokBaru = 0; 
                        menu.setStok(stokBaru);
                        menuRepository.save(menu);
                    }
                }
            }
        }
        return "redirect:/admin/pesanan";
    }
}
