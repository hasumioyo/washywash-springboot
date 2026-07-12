package com.pbo2.washywash.repository;

import java.util.List;

import com.pbo2.washywash.model.DetailPenjualan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetailPenjualanRepository extends JpaRepository<DetailPenjualan, Long>{
    List<DetailPenjualan> findByPenjualan_KodePenjualan(String kodePenjualan);

     boolean existsByBarang_KodeBarang(String kodeBarang);
}
