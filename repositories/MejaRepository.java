package com.impal.demo_brew4u.repositories;

import com.impal.demo_brew4u.models.Meja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MejaRepository extends JpaRepository<Meja, Long> {

    // Query khusus untuk mengambil meja hanya dari cabang tertentu
    // Dan statusnya 'Available' (Opsional, biar user gak pilih meja penuh)
    @Query("SELECT m FROM Meja m WHERE m.kode_cabang = ?1")
    List<Meja> findByKode_cabang(Long kodeCabang);
}