package com.impal.demo_brew4u.config;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.impal.demo_brew4u.models.Admin;
import com.impal.demo_brew4u.repositories.AdminRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Configuration
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                        HttpServletResponse response, 
                                        Authentication authentication) throws IOException, ServletException {

        // 1. Ambil Session saat ini (atau buat baru jika belum ada)
        HttpSession session = request.getSession();

        // 2. Ambil detail user yang barusan login dari Spring Security
        User userDetails = (User) authentication.getPrincipal();
        String emailUser = userDetails.getUsername(); // Ini berisi email karena config .usernameParameter("email")

        // 3. Cek Role User (Admin atau Pelanggan)
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();

            if (role.equals("ROLE_ADMIN")) {
                // --- LOGIKA MENYIMPAN ADMIN KE SESSION ---
                
                // Cari data admin di database berdasarkan email
                // Pastikan di AdminRepository ada method: Optional<Admin> findByEmail(String email);
                Optional<Admin> adminOptional = adminRepository.findByEmail(emailUser);
                
                if (adminOptional.isPresent()) {
                    Admin admin = adminOptional.get(); // Buka pembungkus Optional
                    
                    // SIMPAN KE SESSION (Ini kunci agar Controller tidak error)
                    session.setAttribute("adminLog", admin);
                    
                    System.out.println("LOGIN SUKSES: Admin " + admin.getNamaAdmin() + " masuk ke session.");
                } else {
                    System.out.println("LOGIN WARNING: Admin login tapi datanya tidak ditemukan di tabel admin.");
                }

                // Redirect ke Dashboard
                response.sendRedirect("/admin/dashboard");
                return;
            } 
            
            else if (role.equals("ROLE_PELANGGAN")) {
                // Redirect ke Menu untuk pelanggan
                response.sendRedirect("/menu");
                return;
            }
        }

        // Default redirect jika role tidak dikenali
        response.sendRedirect("/menu");
    }
}
