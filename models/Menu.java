package com.impal.demo_brew4u.models;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kode_menu; 
    
    private Long kode_cabang; 
    
    private String nama_menu;
    private String gambar;
    private BigDecimal harga;
    private int stok;
    private String deskripsi;
    
    // ... variabel lain ...


    @Transient
    private boolean isRecommended;

    public boolean getIsRecommended() { return isRecommended; }
    public void setIsRecommended(boolean isRecommended) { this.isRecommended = isRecommended; }
    
    public Long getKode_menu() { return kode_menu; }
    public void setKode_menu(Long kode_menu) { this.kode_menu = kode_menu; }

    public Long getKode_cabang() { return kode_cabang; }
    public void setKode_cabang(Long kode_cabang) { this.kode_cabang = kode_cabang; }

    public String getNama_menu() { return nama_menu; }
    public void setNama_menu(String nama_menu) { this.nama_menu = nama_menu; }

    public String getGambar() { return gambar; }
    public void setGambar(String gambar) { this.gambar = gambar; }

    public BigDecimal getHarga() { return harga; }
    public void setHarga(BigDecimal harga) { this.harga = harga; }

    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
}