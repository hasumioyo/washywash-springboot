package com.pbo2.washywash.repository;

import com.pbo2.washywash.model.Barang;
import com.pbo2.washywash.model.Pelanggan;

import java.util.List;

// import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface BarangRepository extends JpaRepository<Barang, String> {
    Barang findByKodeBarang(String kodeBarang);


    @Query(value = "SELECT kode_barang FROM barang ORDER BY kode_barang DESC LIMIT 1",
           nativeQuery = true)
    String findLastKodeBarang();

    @Query(value = "SELECT * FROM barang WHERE stok_barang <= 10 ORDER BY stok_barang ASC LIMIT 5",
       nativeQuery = true)
    List<Barang> find10stokterendah();
 
    List<Barang> findByKodeBarangContainingIgnoreCaseOrNamaBarangContainingIgnoreCase(String kodeBarang, String namaBarang);
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
