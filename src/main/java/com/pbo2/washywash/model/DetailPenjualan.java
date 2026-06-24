package com.pbo2.washywash.model;

import jakarta.persistence.*;

import jakarta.persistence.Column;

@Entity
@Table(name = "detailpenjualan")

public class DetailPenjualan {
    @Id

    @Column(name = "kode_detail")
    private String kodeDetail;

    @Column(name = "quantity")
    private int qty;

    @Column(name =  "harga")
    private double harga;

    @Column(name = "subtotal")
    private double subtotal;

    @ManyToOne
    @JoinColumn(name = "kode_penjualan")
    private Penjualan penjualan;

    @ManyToOne
    @JoinColumn(name = "kode_barang")
    private Barang barang;


    public String getKodeDetail() {
        return kodeDetail;
    }

    public void setKodeDetail(String kodeDetail) {
        this.kodeDetail = kodeDetail;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga){
        this.harga = harga;
    }

    public double getSubTotal() {
        return subtotal;
    }

    public void setSubTotal(double subtotal) {
        this.subtotal = subtotal;
    }


    public Penjualan getPenjualan() {
        return penjualan;
    }

    public void setPenjualan(Penjualan penjualan) {
        this.penjualan = penjualan;
    }

    public Barang getBarang() {
        return barang;
    }

    public void setBarang(Barang barang) {
        this.barang = barang;
    }


    
}
