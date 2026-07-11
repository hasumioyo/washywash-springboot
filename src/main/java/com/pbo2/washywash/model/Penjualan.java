package com.pbo2.washywash.model;

import jakarta.persistence.*;
// import java.sql.Date;
// import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "penjualan")

public class Penjualan {
    @Id

    @Column(name = "kode_penjualan")
    private String kodePenjualan;

    @Column(name = "tanggal_penjualan")
    private LocalDateTime tanggalPenjualan;

    @Column(name = "total")
    private double totalPenjualan;

    @Column(name = "diskon")
    private double diskonPenjualan;

    @Column(name = "total_pembayaran")
    private double totalPembayaran;

    @Column(name = "hasil_kembalian")
    private double hasilKembalian;

    @ManyToOne
    @JoinColumn(name = "kode_pelanggan")
    private Pelanggan pelanggan;


    public String getKodePenjualan() {
        return kodePenjualan;
    }

    public void setKodePenjualan(String kodePenjualan) {
        this.kodePenjualan = kodePenjualan;
    }

    public LocalDateTime getTanggalPenjualan() {
        return tanggalPenjualan;
    }

    public void setTanggalPenjualan(LocalDateTime tanggalPenjualan) {
        this.tanggalPenjualan = tanggalPenjualan;
    }

    public double getTotalPenjualan() {
        return totalPenjualan;
    }

    public void setTotalPenjualan(double totalPenjualan) {
        this.totalPenjualan = totalPenjualan;
    }

    public double getDiskonPenjualan() {
        return diskonPenjualan;
    }

    public void setDiskonPenjualan(double diskonPenjualan){
        this.diskonPenjualan = diskonPenjualan;
    }

    public Pelanggan getPelanggan() {
        return pelanggan;
    }

    public void setPelanggan(Pelanggan pelanggan) {
        this.pelanggan = pelanggan;
    }

    public double getTotalPembayaran() {
        return totalPembayaran;
    }

    public void setTotalPembayaran(double totalPembayaran) {
        this.totalPembayaran = totalPembayaran;
    }

    public double getHasilKembalian() {
        return hasilKembalian;
    }

    public void setHasilKembalian(double hasilKembalian) {
        this.hasilKembalian = hasilKembalian;
    }


    
}
