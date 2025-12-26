package com.impal.demo_brew4u.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.impal.demo_brew4u.models.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    // Method ini digunakan di PelangganController (Login Manual)
    // untuk mencari admin berdasarkan email
    Optional<Admin> findByEmail(String email);
    
    // Opsional: Jika nanti butuh cari berdasarkan username
    Optional<Admin> findByUsername(String username);
}
