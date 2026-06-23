package com.pbo2.washywash.model;

import jakarta.persistence.*;
import java.sql.Date;
import java.time.LocalDate;

@Entity
@Table(name = "penjualan")

public class Penjualan {
    @Id

    @Column(name = "kode_penjualan")
    private String kodePenjualan;

    @Column(name = "tanggal_penjualan")
    private LocalDate tanggalPenjualan;

    @Column(name = "total")
    private double totalPenjualan;

    @Column(name = "diskon")
    private double diskonPenjualan;

    public String getKodePenjualan() {
        return kodePenjualan;
    }

    public void setKodePenjualan(String kodePenjualan) {
        this.kodePenjualan = kodePenjualan;
    }

    public LocalDate getTanggalPenjualan() {
        return tanggalPenjualan;
    }

    public void setTanggalPenjualan(LocalDate tanggalPenjualan) {
        this.tanggalPenjualan = tanggalPenjualan;
    }

    public double gettotalPenjualan() {
        return totalPenjualan;
    }

    public void settotalPenjualan(double totalPenjualan) {
        this.totalPenjualan = totalPenjualan; 
    }

    public double getDiskon() {
        return diskonPenjualan;
    }

    public void setDiskon(double diskonPenjualan){
        this.diskonPenjualan = diskonPenjualan;
    }

    
}
