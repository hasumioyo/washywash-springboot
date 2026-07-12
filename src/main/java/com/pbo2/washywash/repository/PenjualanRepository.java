package com.pbo2.washywash.repository;

import com.pbo2.washywash.model.Pelanggan;
import com.pbo2.washywash.model.Penjualan;
import com.pbo2.washywash.model.Barang;

import java.util.List;

// import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
// import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;



public interface PenjualanRepository extends JpaRepository<Penjualan, String> {

    List<Penjualan> findByTanggalPenjualan(LocalDate tanggalPenjualan);

    List<Penjualan> findByPelanggan(Pelanggan pelanggan);

    // List<Penjualan> findByBarang(Barang barang);

    boolean existsByPelanggan(Pelanggan pelanggan); //check pelanggan

    @Query(value = "SELECT * FROM penjualan WHERE MONTH(tanggal_penjualan)=:bulan AND YEAR(tanggal_penjualan)=:tahun ORDER BY tanggal_penjualan",
       nativeQuery = true)
    List<Penjualan> findLaporanBulanan(@Param("bulan") int bulan, @Param("tahun") int tahun);

    @Query(value = " SELECT SUM(total) FROM penjualan WHERE MONTH(tanggal_penjualan) =:bulan AND YEAR(tanggal_penjualan) =:tahun", nativeQuery = true)
    Double  getTotalPendapatanBulanan(@Param("bulan") int bulan, @Param("tahun") int tahun);

    @Query(value = "SELECT COUNT(*) FROM penjualan WHERE MONTH(tanggal_penjualan) =:bulan AND YEAR(tanggal_penjualan) =:tahun", nativeQuery = true)
    Long getJumlahTransaksiBulanan(@Param("bulan") int bulan, @Param("tahun") int tahun);
    
    @Query("SELECT p FROM Penjualan p WHERE LOWER(p.kodePenjualan) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Penjualan> cariKodePenjualan(@Param("keyword") String keyword);
}