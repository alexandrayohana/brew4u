package com.impal.demo_brew4u.repositories;

import com.impal.demo_brew4u.models.Pelanggan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository 
public interface PelangganRepository extends JpaRepository<Pelanggan, Long> {
    
    // Method untuk mencari pengguna berdasarkan email untuk login (opsional)
    Pelanggan findByEmail(String email);
}