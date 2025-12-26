package com.impal.demo_brew4u.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue; 
import jakarta.persistence.GenerationType; 

@Entity
@Table(name = "pelanggan")
public class Pelanggan {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long kode_pelanggan; 

    private String nama_pelanggan;
    private String email;
    private String password; 
    
    public Pelanggan() {
    }

    public Pelanggan(Long kode_pelanggan, String nama_pelanggan, String email, String password) {
        this.kode_pelanggan = kode_pelanggan;
        this.nama_pelanggan = nama_pelanggan;
        this.email = email;
        this.password = password;
    }
    
    
    
    public Long getKode_pelanggan() {
        return kode_pelanggan;
    }

    public void setKode_pelanggan(Long kode_pelanggan) {
        this.kode_pelanggan = kode_pelanggan;
    }
    

    public String getNama_pelanggan() {
        return nama_pelanggan;
    }

    public void setNama_pelanggan(String nama_pelanggan) {
        this.nama_pelanggan = nama_pelanggan;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}