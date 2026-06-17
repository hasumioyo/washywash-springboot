package com.pbo2.washywash.repository;

import com.pbo2.washywash.model.Pelanggan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface PelangganRepository extends JpaRepository<Pelanggan, String>{
    Pelanggan findByKodePelanggan(String kodePelanggan);

    @Query(value = "SELECT p.kodePelanggan FROM Pelanggan p WHERE p.kodePelanggan LIKE CONCAT(:prefix, '%') ORDER BY p.kodePelanggan DESC LIMIT 1")
    String findLastKodeByPrefix(String prefix);
}
