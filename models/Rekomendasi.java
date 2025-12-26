package com.impal.demo_brew4u.models;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "rekomendasi")
public class Rekomendasi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_rekomendasi;

    // Kita simpan ID Menu saja agar simpel sesuai struktur tabel Anda
    private Long kode_menu;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "tanggal_update", insertable = false, updatable = false)
    private Date tanggalUpdate;

    // Constructors
    public Rekomendasi() {}
    
    public Rekomendasi(Long kode_menu) {
        this.kode_menu = kode_menu;
    }

    // Getters Setters
    public Integer getId_rekomendasi() { return id_rekomendasi; }
    public void setId_rekomendasi(Integer id_rekomendasi) { this.id_rekomendasi = id_rekomendasi; }

    public Long getKodeMenu() { return kode_menu; }
    public void setKodeMenu(Long kode_menu) { this.kode_menu = kode_menu; }
}