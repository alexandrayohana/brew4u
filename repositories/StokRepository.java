package com.impal.demo_brew4u.repositories;

import com.impal.demo_brew4u.models.Stok;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StokRepository extends JpaRepository<Stok, Long> {
    
    // Query manual filter by cabang
    @Query("SELECT s FROM Stok s WHERE s.kode_cabang = :kodeCabang")
    List<Stok> findByKode_cabang(@Param("kodeCabang") Long kodeCabang);
}