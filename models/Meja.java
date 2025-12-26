package com.impal.demo_brew4u.models;

import jakarta.persistence.*;

@Entity
@Table(name = "meja")
public class Meja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_meja;

    @Column(name = "no_meja")
    private String no_meja; // Menggunakan String agar fleksibel (misal: "A1", "02")

    @Column(name = "kode_cabang")
    private Long kode_cabang; // Relasi ke cabang

    @Column(name = "status")
    private String status; // "Available", "Occupied"

    // --- Constructor ---
    public Meja() {}

    public Meja(String no_meja, Long kode_cabang, String status) {
        this.no_meja = no_meja;
        this.kode_cabang = kode_cabang;
        this.status = status;
    }

    // --- Getters and Setters ---
    public Long getId_meja() { return id_meja; }
    public void setId_meja(Long id_meja) { this.id_meja = id_meja; }

    public String getNo_meja() { return no_meja; }
    public void setNo_meja(String no_meja) { this.no_meja = no_meja; }

    public Long getKode_cabang() { return kode_cabang; }
    public void setKode_cabang(Long kode_cabang) { this.kode_cabang = kode_cabang; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}