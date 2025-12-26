package com.impal.demo_brew4u.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User; 
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.impal.demo_brew4u.models.Admin;
import com.impal.demo_brew4u.models.Pelanggan;
import com.impal.demo_brew4u.repositories.AdminRepository;
import com.impal.demo_brew4u.repositories.PelangganRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PelangganRepository pelangganRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        // 1. Cek Admin
        // (Menggunakan Optional karena Repository Admin Anda mengembalikan Optional)
        Optional<Admin> adminOpt = adminRepository.findByEmail(email);
        
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            
            // KONVERSI: Admin -> UserDetails
            return User.builder()
                    .username(admin.getEmail())    // Set Email sebagai username
                    .password(admin.getPassword()) // Set Password
                    .roles("ADMIN")                // Otomatis jadi ROLE_ADMIN
                    .build();
        }

        // 2. Cek Pelanggan
        // (Mengambil Objek langsung karena Repository Pelanggan Anda mengembalikan Pelanggan)
        Pelanggan pelanggan = pelangganRepository.findByEmail(email);
        
        if (pelanggan != null) {
            // KONVERSI: Pelanggan -> UserDetails
            return User.builder()
                    .username(pelanggan.getEmail())
                    .password(pelanggan.getPassword())
                    .roles("PELANGGAN")            // Otomatis jadi ROLE_PELANGGAN
                    .build();
        }

        // 3. Jika tidak ditemukan
        throw new UsernameNotFoundException("Email tidak ditemukan: " + email);
    }
}
