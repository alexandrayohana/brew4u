package com.impal.demo_brew4u.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query; // Import ini
import org.springframework.data.repository.query.Param;     // Import ini
import org.springframework.stereotype.Repository;

import com.impal.demo_brew4u.models.Rekomendasi;

import jakarta.transaction.Transactional; // Import ini

@Repository
public interface RekomendasiRepository extends JpaRepository<Rekomendasi, Integer> {
    
    // Cek apakah menu ini ada di tabel rekomendasi
    // (Ini biasanya aman walau pake underscore, tapi kalau error ganti jadi query manual juga)
    @Query("SELECT COUNT(r) > 0 FROM Rekomendasi r WHERE r.kode_menu = :kodeMenu")
    boolean existsByKode_menu(@Param("kodeMenu") Long kodeMenu);

    // Cari data rekomendasi
    @Query("SELECT r FROM Rekomendasi r WHERE r.kode_menu = :kodeMenu")
    java.util.Optional<Rekomendasi> findByKode_menu(@Param("kodeMenu") Long kodeMenu);
    
    // --- PERBAIKAN DI SINI ---
    // Gunakan @Modifying dan @Query untuk menghapus manual
    @Modifying
    @Transactional
    @Query("DELETE FROM Rekomendasi r WHERE r.kode_menu = :kodeMenu")
    void deleteByKodeMenu(@Param("kodeMenu") Long kodeMenu);
}