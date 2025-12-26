package com.impal.demo_brew4u.models;

import jakarta.persistence.*;

@Entity
@Table(name = "stok")
public class Stok {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_stok;

    private String nama_barang;
    private Integer jumlah;
    private String satuan; // Contoh: Kg, Liter, Pcs, Dus
    private Long kode_cabang; // Wajib Long agar sesuai dengan Admin

    // --- Getters & Setters ---
    public Long getId_stok() { return id_stok; }
    public void setId_stok(Long id_stok) { this.id_stok = id_stok; }

    public String getNama_barang() { return nama_barang; }
    public void setNama_barang(String nama_barang) { this.nama_barang = nama_barang; }

    public Integer getJumlah() { return jumlah; }
    public void setJumlah(Integer jumlah) { this.jumlah = jumlah; }

    public String getSatuan() { return satuan; }
    public void setSatuan(String satuan) { this.satuan = satuan; }

    public Long getKode_cabang() { return kode_cabang; }
    public void setKode_cabang(Long kode_cabang) { this.kode_cabang = kode_cabang; }
}