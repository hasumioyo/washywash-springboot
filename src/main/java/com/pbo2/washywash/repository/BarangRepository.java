package com.pbo2.washywash.repository;

import com.pbo2.washywash.model.Barang;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BarangRepository extends JpaRepository<Barang, String> {

    Barang findByKodeBarang(String kodeBarang);

    List<Barang> findByStatus(String status);

    @Query("SELECT b FROM Barang b WHERE b.status = 'Aktif'")
        List<Barang> findBarangAktif();

    @Query(value = "SELECT kode_barang FROM barang WHERE kode_barang LIKE CONCAT(:prefix,'%') ORDER BY kode_barang DESC LIMIT 1", nativeQuery = true)
        String findLastKodeBarangByPrefix(@Param("prefix") String prefix);

    @Query(value = "SELECT * FROM barang WHERE status='Aktif' AND stok_barang <= 10 ORDER BY stok_barang ASC LIMIT 5", nativeQuery = true)
        List<Barang> find10stokterendah();

    @Query("SELECT b FROM Barang b WHERE b.status = :status AND (LOWER(b.kodeBarang) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(b.namaBarang) LIKE LOWER(CONCAT('%', :keyword, '%')))")
        List<Barang> findByStatusAndKeyword(@Param("status") String status, @Param("keyword") String keyword);

    @Query("SELECT b FROM Barang b WHERE b.kodeBarang = :kodeBarang AND b.status = 'Aktif'")
        Barang findBarangAktifByKode(@Param("kodeBarang") String kodeBarang);
    

   

}

// @Repository
// public class BarangRepository {
//     private final List<Barang> daftarBarang = new ArrayList<>();

//     public BarangRepository() {
//         daftarBarang.add(new Barang("ELK001", "Mouse Wireless", "Aksesoris", 150000, 20));
//         daftarBarang.add(new Barang("ELK002", "Keyboard Mechanical", "Aksesoris", 450000, 10));
//     }

//     public List<Barang> findAll() {
//         return daftarBarang;
//     }

//     public Barang findByKode(String kodeBarang) {
//         for (Barang barang : daftarBarang) {
//             if (barang.getKodeBarang().equals(kodeBarang)) {
//                 return barang;
//             }
//         }
//         return null;
//     }

//     public void save(Barang barang) {
//         daftarBarang.add(barang);
//     }

//     public void update(Barang barangBaru) {
//         Barang barangLama = findByKode(barangBaru.getKodeBarang());

//         if (barangLama != null) {
//             barangLama.setNamaBarang(barangBaru.getNamaBarang());
//             barangLama.setKategori(barangBaru.getKategori());
//             barangLama.setHarga(barangBaru.getHarga());
//             barangLama.setStok(barangBaru.getStok());
//         }
//     }

//     public void delete(String kodeBarang) {
//         daftarBarang.removeIf(barang -> barang.getKodeBarang().equals(kodeBarang));
//     }
// }
