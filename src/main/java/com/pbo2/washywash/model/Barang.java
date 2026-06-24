package com.pbo2.washywash.model;

import jakarta.persistence.*;

@Entity
@Table(name = "barang")

//Long means primary

public class Barang {
    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "kode_barang")
    private String kodeBarang;
    
    @Column(name = "nama_barang")
    private String namaBarang;

    @Column(name = "kategori")
    private String kategori;
    
    @Column(name = "satuan")
    private String satuan;

    @Column(name = "harga_barang")
    private double harga;

    @Column(name = "stok_barang")
    private int stok;

    public Barang() {
    }

    // public Barang(String kodeBarang, String namaBarang, String kategori, double harga, int stok) {
    //     this.kodeBarang = kodeBarang;
    //     this.namaBarang = namaBarang;
    //     this.kategori = kategori;
    //     this.satuan = satuan;
    //     this.harga = harga;
    //     this.stok = stok;
    // }

    public String getKodeBarang() {
        return kodeBarang;
    }

    public void setKodeBarang(String kodeBarang) {
        this.kodeBarang = kodeBarang;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public String getSatuan(){
        return satuan;
    }

    public void getSatuan(String satuan) {
        this.satuan = satuan;
    }


    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }
}
