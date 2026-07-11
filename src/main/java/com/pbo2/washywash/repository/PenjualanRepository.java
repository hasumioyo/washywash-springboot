package com.pbo2.washywash.repository;

import com.pbo2.washywash.model.Pelanggan;
import com.pbo2.washywash.model.Penjualan;
import com.pbo2.washywash.model.Barang;

import java.util.List;

// import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.stereotype.Repository;

import java.time.LocalDate;



public interface PenjualanRepository extends JpaRepository<Penjualan, String> {

    List<Penjualan> findByTanggalPenjualan(LocalDate tanggalPenjualan);

    List<Penjualan> findByPelanggan(Pelanggan pelanggan);

    // List<Penjualan> findByBarang(Barang barang);

    boolean existsByPelanggan(Pelanggan pelanggan); //check pelanggan
    
}