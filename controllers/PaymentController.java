package com.impal.demo_brew4u.controllers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.impal.demo_brew4u.config.MidtransConfig;
import com.impal.demo_brew4u.models.CartItemView;
import com.impal.demo_brew4u.models.DetailPesanan;
import com.impal.demo_brew4u.models.Meja;
import com.impal.demo_brew4u.models.Menu;
import com.impal.demo_brew4u.models.Pelanggan;
import com.impal.demo_brew4u.models.Pesanan;
import com.impal.demo_brew4u.repositories.DetailPesananRepository;
import com.impal.demo_brew4u.repositories.MejaRepository;
import com.impal.demo_brew4u.repositories.MenuRepository;
import com.impal.demo_brew4u.repositories.PelangganRepository;
import com.impal.demo_brew4u.repositories.PesananRepository;
import com.midtrans.httpclient.SnapApi;
import com.midtrans.httpclient.error.MidtransError;

import jakarta.servlet.http.HttpSession;

@Controller
public class PaymentController {

    @Autowired private MenuRepository menuRepository;
    @Autowired private MejaRepository mejaRepository;
    @Autowired private PelangganRepository pelangganRepository;
    @Autowired private PesananRepository pesananRepository;
    @Autowired private DetailPesananRepository detailPesananRepository;

    // ... (Method showPaymentPage biarkan seperti sebelumnya) ...
    @GetMapping("/pembayaran")
    @SuppressWarnings("unchecked")
    public String showPaymentPage(Model model, HttpSession session, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) return "redirect:/login";
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) return "redirect:/menu";

        List<CartItemView> cartItems = cart.entrySet().stream()
                .map(entry -> menuRepository.findById(entry.getKey())
                        .map(menu -> new CartItemView(menu, entry.getValue())).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        BigDecimal grandTotal = cartItems.stream()
                .map(CartItemView::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Long currentCabangId = (Long) session.getAttribute("currentCabangId");
        List<Meja> listMeja = (currentCabangId != null) ? 
                mejaRepository.findByKode_cabang(currentCabangId) : mejaRepository.findAll();

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("grandTotal", grandTotal);
        model.addAttribute("listMeja", listMeja);
        
        String email = authentication.getName();
        model.addAttribute("user", pelangganRepository.findByEmail(email));

        return "detailpembayaran"; 
    }

    // ================================================================
    // PERBAIKAN UTAMA ADA DI SINI
    // ================================================================
    @PostMapping("/pembayaran/proses")
    @SuppressWarnings("unchecked")
    public String processPayment(@RequestParam("kodeMeja") Long kodeMeja,
                                 HttpSession session,
                                 Authentication authentication,
                                 Model model) {

        // 1. DEKLARASI DI ATAS (Agar tidak error "cannot be resolved")
        Pelanggan user = null;
        BigDecimal totalHarga = BigDecimal.ZERO; 

        // Cek Login & Isi User
        if (authentication != null && authentication.isAuthenticated()) {
            user = pelangganRepository.findByEmail(authentication.getName());
        } else {
            return "redirect:/login";
        }

        // Cek Cart
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) return "redirect:/menu";

        // Hitung Total (Sekarang aman karena variabel sudah ada di atas)
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Menu menu = menuRepository.findById(entry.getKey()).orElse(null);
            if (menu != null) {
                BigDecimal subtotal = menu.getHarga().multiply(new BigDecimal(entry.getValue()));
                totalHarga = totalHarga.add(subtotal);
            }
        }

        // Ambil Cabang
        Long currentCabangId = (Long) session.getAttribute("currentCabangId");
        if (currentCabangId == null) currentCabangId = 1L;

        // Simpan Header Pesanan
        Pesanan pesanan = new Pesanan();
        pesanan.setPelanggan(user); // Variabel 'user' aman dipakai
        pesanan.setKodeCabang(currentCabangId);
        pesanan.setNo_meja(kodeMeja.intValue());
        pesanan.setTotal_harga(totalHarga.doubleValue()); // Variabel 'totalHarga' aman dipakai
        pesanan.setTanggalPesanan(new Date()); 
        
        // Status Awal
        pesanan.setStatus("Belum");             
        pesanan.setStatusPembayaran("pending"); 
        
        Pesanan savedPesanan = pesananRepository.save(pesanan);

        // Simpan Detail Item (PENTING: Ini harus sukses agar Admin tidak kosong)
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Menu menu = menuRepository.findById(entry.getKey()).orElse(null);
            if (menu != null) {
                DetailPesanan dp = new DetailPesanan();
                dp.setPesanan(savedPesanan);
                dp.setMenu(menu);
                dp.setQuantity(entry.getValue());
                dp.setSubtotal(menu.getHarga().multiply(new BigDecimal(entry.getValue())).doubleValue());
                detailPesananRepository.save(dp);
            }
        }

        // Request Midtrans
        try {
            Map<String, Object> params = new HashMap<>();
            Map<String, String> transactionDetails = new HashMap<>();
            
            String uniqueOrderId = savedPesanan.getKode_pesanan() + "-" + System.currentTimeMillis();
            transactionDetails.put("order_id", uniqueOrderId);
            transactionDetails.put("gross_amount", String.valueOf(totalHarga.intValue()));

            params.put("transaction_details", transactionDetails);

            Map<String, String> custDetail = new HashMap<>();
            custDetail.put("first_name", user.getNama_pelanggan());
            custDetail.put("email", user.getEmail());
            params.put("customer_details", custDetail);

            String snapToken = SnapApi.createTransactionToken(params);

            model.addAttribute("snapToken", snapToken);
            model.addAttribute("clientKey", MidtransConfig.CLIENT_KEY);
            model.addAttribute("pesanan", savedPesanan);
            
            session.removeAttribute("cart");

            return "payment"; 

        } catch (MidtransError e) {
            e.printStackTrace();
            return "redirect:/menu?error=midtrans_fail";
        }
    }
    
    @GetMapping("/pembayaran/sukses")
    public String paymentSuccess(@RequestParam("orderId") Long orderId) {
        Pesanan p = pesananRepository.findById(orderId).orElse(null);
        if(p != null) {
            p.setStatusPembayaran("lunas"); 
            pesananRepository.save(p);
        }
        return "redirect:/konfirmasi_sukses"; 
    }
}
