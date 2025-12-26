package com.impal.demo_brew4u.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.impal.demo_brew4u.models.DetailPesanan;
import com.impal.demo_brew4u.models.Pesanan;
@Repository
public interface DetailPesananRepository extends JpaRepository<DetailPesanan, Long> {
    List<DetailPesanan> findByPesanan(Pesanan pesanan);
}