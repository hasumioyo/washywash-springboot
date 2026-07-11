package com.pbo2.washywash.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pbo2.washywash.model.Barang;
import com.pbo2.washywash.repository.BarangRepository;

@Service
public class BarangService {
    
    private final BarangRepository barangRepository;

    public BarangService(BarangRepository barangRepository) {
        this.barangRepository = barangRepository;
    }

    public List<Barang> getAllBarang() {
        return barangRepository.findAll();
    }

    public Barang getBarangByKode(String kodeBarang) {
        return barangRepository.findByKodeBarang(kodeBarang);
    }

    public void tambahBarang(Barang barang) {

        if (barang.getKodeBarang() == null || barang.getKodeBarang().isEmpty()) {
            barang.setKodeBarang(generateKodeBarang());
        }
        barangRepository.save(barang);
    }

    public void updateBarang(Barang barang) {
        barangRepository.save(barang);
    }

    public void hapusBarang(String kodeBarang) {
        barangRepository.deleteById(kodeBarang);
    }

    public String generateKodeBarang() {

        String lastKode = barangRepository.findLastKodeBarang();

        if (lastKode == null) {
            return "BRG0001";
        }

        int angka = Integer.parseInt(lastKode.substring(3));

        return "BRG" + String.format("%04d", angka + 1);
    }

    public List<Barang> getStokBarang10Terendah() {
        return barangRepository.find10stokterendah();
    }

    public List<Barang> cariBarang(String keyword) {
        return barangRepository.findByKodeBarangContainingIgnoreCaseOrNamaBarangContainingIgnoreCase(keyword, keyword);
    }
    
// @Repository
// public class BarangRepository {
}
