package com.impal.demo_brew4u.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "cabang")
public class Cabang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kode_cabang; // Tipe Long (Auto Increment)
    
    private String nama_cabang;
    private String alamat;
    private String no_telp;

    // --- Getters and Setters ---
    
    public Long getKode_cabang() { return kode_cabang; }
    public void setKode_cabang(Long kode_cabang) { this.kode_cabang = kode_cabang; }
    
    public String getNama_cabang() { return nama_cabang; }
    public void setNama_cabang(String nama_cabang) { this.nama_cabang = nama_cabang; }
    
    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }
    
    public String getNo_telp() { return no_telp; }
    public void setNo_telp(String no_telp) { this.no_telp = no_telp; }
}