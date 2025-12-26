package com.impal.demo_brew4u.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
 
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "detail_pesanan")
public class DetailPesanan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_detail;

    private Integer quantity;
    private Double subtotal;

    @ManyToOne
    @JoinColumn(name = "kode_pesanan")
    @JsonIgnore // PENTING: Agar tidak error looping saat diambil via AJAX/JSON
    private Pesanan pesanan;

    @ManyToOne
    @JoinColumn(name = "kode_menu")
    private Menu menu; // Relasi ke tabel Menu untuk ambil nama menu

    // --- Getters & Setters ---
    public Long getId_detail() { return id_detail; }
    public void setId_detail(Long id_detail) { this.id_detail = id_detail; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

    public Pesanan getPesanan() { return pesanan; }
    public void setPesanan(Pesanan pesanan) { this.pesanan = pesanan; }

    public Menu getMenu() { return menu; }
    public void setMenu(Menu menu) { this.menu = menu; }
}