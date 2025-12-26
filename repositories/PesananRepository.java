package com.impal.demo_brew4u.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.impal.demo_brew4u.models.Pelanggan;
import com.impal.demo_brew4u.models.Pesanan;

@Repository
public interface PesananRepository extends JpaRepository<Pesanan, Long> {
    
    // METHOD BARU: Filter pesanan berdasarkan ID Cabang
    // Asumsi: field di database adalah 'kode_cabang' dan di Model 'kodeCabang'
    List<Pesanan> findByKodeCabangOrderByTanggalPesananDesc(Long kodeCabang);

    // Backup: Ambil semua (untuk Super Admin jika ada)
    List<Pesanan> findAllByOrderByTanggalPesananDesc();
    List<Pesanan> findByPelangganOrderByTanggalPesananDesc(Pelanggan pelanggan);
}