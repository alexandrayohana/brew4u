package com.impal.demo_brew4u.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "admin")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_admin") // Mapping ke kolom id_admin di DB
    private Long idAdmin;

    @Column(name = "nama_admin") // Mapping ke kolom nama_admin
    private String namaAdmin;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "kode_cabang") // Mapping ke kolom kode_cabang
    private Long kodeCabang;    // Tipe String karena di PDF datanya "B4U001"

    // --- Constructor Kosong (Wajib untuk JPA) ---
    public Admin() {}

    // --- Constructor dengan Parameter ---
    public Admin(String namaAdmin, String username, String password, String email, Long kodeCabang) {
        this.namaAdmin = namaAdmin;
        this.username = username;
        this.password = password;
        this.email = email;
        this.kodeCabang = kodeCabang;
    }

    // --- Getters & Setters ---

    public Long getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(Long idAdmin) {
        this.idAdmin = idAdmin;
    }

    public String getNamaAdmin() {
        return namaAdmin;
    }

    public void setNamaAdmin(String namaAdmin) {
        this.namaAdmin = namaAdmin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getKodeCabang() {
        return kodeCabang;
    }

    public void setKodeCabang(Long kodeCabang) {
        this.kodeCabang = kodeCabang;
    }
}
