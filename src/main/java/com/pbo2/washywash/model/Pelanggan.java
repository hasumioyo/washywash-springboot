package com.pbo2.washywash.model;

import jakarta.persistence.*;


@Entity
@Table(name = "pelanggan")

public class Pelanggan {
    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    @Column(name = "kode_pelanggan")
    private String kodePelanggan;

    @Column(name = "nama_pelanggan")
    private String namaPelanggan;

    @Column(name = "no_hp")
    private String noHP;

    @Column(name = "email")
    private String Email;


    // public Pelanggan() {
    //     this.kodePelanggan = kodePelanggan;
    //     this.namaPelanggan = namaPelanggan;
    //     this.noHP = noHP;
    //     this.Email = Email;
    // }

    public String getKodePelanggan() {
        return kodePelanggan;
    }

    public void setKodePelanggan(String kodePelanggan) {
        this.kodePelanggan = kodePelanggan;
    }
    
    public String getNamaPelanggan() {
        return namaPelanggan;
    }

    public void setNamaPelanggan(String namaPelanggan) {
        this.namaPelanggan = namaPelanggan;
    }

    public String getNoHP() {
        return noHP;
    }

    public void setNoHP(String noHP) {
        this.noHP = noHP;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }
}