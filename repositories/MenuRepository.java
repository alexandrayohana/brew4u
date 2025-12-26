package com.impal.demo_brew4u.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.impal.demo_brew4u.models.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    
    // 1. Ambil semua menu berdasarkan cabang
    @Query("SELECT m FROM Menu m WHERE m.kode_cabang = :kodeCabang")
    List<Menu> findByKode_cabang(@Param("kodeCabang") Long kodeCabang);

    // 2. [PERBAIKAN] Tambahkan method ini!
    // Query ini artinya: "Ambil Menu (m) dimana kode_cabang sesuai DAN kode_menu-nya ADA di dalam tabel Rekomendasi (r)"
    @Query("SELECT m FROM Menu m WHERE m.kode_cabang = :kodeCabang AND m.kode_menu IN (SELECT r.kode_menu FROM Rekomendasi r)")
    List<Menu> findRecommended(@Param("kodeCabang") Long kodeCabang);

    // ... query count lainnya biarkan saja ...
    @Query("SELECT COUNT(m) FROM Menu m WHERE m.kode_cabang = :kodeCabang")
    long countTotalMenu(@Param("kodeCabang") Long kodeCabang);

    @Query("SELECT COUNT(m) FROM Menu m WHERE m.kode_cabang = :kodeCabang AND m.stok < 10")
    long countStokMenipis(@Param("kodeCabang") Long kodeCabang);
}