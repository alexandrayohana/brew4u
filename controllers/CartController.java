package com.impal.demo_brew4u.controllers;

import com.impal.demo_brew4u.models.Menu;
import com.impal.demo_brew4u.models.CartItemView; 
import com.impal.demo_brew4u.repositories.MenuRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class CartController {

    private final MenuRepository menuRepository;

    @Autowired
    public CartController(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @SuppressWarnings("unchecked")
    private Map<Long, Integer> getCart(HttpSession session) {
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    @GetMapping("/keranjang")
    public String showCartPage(Model model, HttpSession session) {
        
        Map<Long, Integer> cart = getCart(session);
        if (cart.isEmpty()) {
            model.addAttribute("cartItems", Collections.emptyList());
            model.addAttribute("totalItems", 0);
            return "keranjang";
        }

        List<CartItemView> cartItems = cart.entrySet().stream()
            .map(entry -> {
                Optional<Menu> menuOpt = menuRepository.findById(entry.getKey());
                return menuOpt.map(menu -> new CartItemView(menu, entry.getValue())).orElse(null);
            })
            .filter(item -> item != null)
            .collect(Collectors.toList());

        int totalItems = cartItems.stream().mapToInt(CartItemView::getQuantity).sum();
        
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalItems", totalItems);
        
        return "keranjang";
    }

    @PostMapping("/cart/update")
    @ResponseBody
    public Map<String, Object> updateCart(@RequestParam("kodeMenu") Long kodeMenu,
                                          @RequestParam("delta") int delta,
                                          HttpSession session) {
        
        Map<Long, Integer> cart = getCart(session);

        int currentQuantity = cart.getOrDefault(kodeMenu, 0);
        int newQuantity = currentQuantity + delta;


        if (delta > 0) { // Cek hanya jika MENAMBAH item
            Optional<Menu> menuOpt = menuRepository.findById(kodeMenu);
            
            if (menuOpt.isEmpty()) {
                return Map.of("status", "error", "message", "Menu tidak ditemukan.");
            }
            
            Menu menu = menuOpt.get();
            if (newQuantity > menu.getStok()) {
                // Kuantitas baru melebihi stok
                return Map.of(
                    "status", "error", 
                    "message", "Stok tidak cukup. Tersisa: " + menu.getStok()
                );
            }
        }

        if (newQuantity <= 0) {
            cart.remove(kodeMenu); // Hapus jika kuantitas 0
        } else {
            cart.put(kodeMenu, newQuantity);
        }
        
        session.setAttribute("cart", cart); 
        return Collections.singletonMap("status", "success"); 
    }
}
