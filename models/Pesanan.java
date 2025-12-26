package com.impal.demo_brew4u.models;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "pesanan")
public class Pesanan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kode_pesanan;

    @ManyToOne
    @JoinColumn(name = "kode_pelanggan")
    private Pelanggan pelanggan;

    // Pastikan Anda punya relasi ke Cabang (agar bisa difilter per cabang)
    @Column(name = "kode_cabang") // Atau @ManyToOne jika Anda pakai relasi objek
    private Long kodeCabang; 

    private int no_meja;
    private double total_harga;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "tgl_pesan")
    private Date tanggalPesanan;

    // STATUS 1: PENYAJIAN (Dapur) -> 'Belum', 'Sedang', 'Siap'
    @Column(name = "status", length = 20)
    private String status;

    // STATUS 2: PEMBAYARAN (Keuangan) -> 'pending', 'lunas'
    @Column(name = "status_pembayaran", length = 20)
    private String statusPembayaran;

    @OneToMany(mappedBy = "pesanan", cascade = CascadeType.ALL)
    private List<DetailPesanan> detailPesanan;

    // --- GETTERS & SETTERS (Generate ulang di IDE Anda jika perlu) ---
    public Long getKode_pesanan() { return kode_pesanan; }
    public void setKode_pesanan(Long kode_pesanan) { this.kode_pesanan = kode_pesanan; }

    public Pelanggan getPelanggan() { return pelanggan; }
    public void setPelanggan(Pelanggan pelanggan) { this.pelanggan = pelanggan; }
    
    public Long getKodeCabang() { return kodeCabang; }
    public void setKodeCabang(Long kodeCabang) { this.kodeCabang = kodeCabang; }

    public int getNo_meja() { return no_meja; }
    public void setNo_meja(int no_meja) { this.no_meja = no_meja; }

    public double getTotal_harga() { return total_harga; }
    public void setTotal_harga(double total_harga) { this.total_harga = total_harga; }

    public Date getTanggalPesanan() { return tanggalPesanan; }
    public void setTanggalPesanan(Date tanggalPesanan) { this.tanggalPesanan = tanggalPesanan; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getStatusPembayaran() { return statusPembayaran; }
    public void setStatusPembayaran(String statusPembayaran) { this.statusPembayaran = statusPembayaran; }
}