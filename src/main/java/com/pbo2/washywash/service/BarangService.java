package com.pbo2.washywash.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pbo2.washywash.model.Barang;
import com.pbo2.washywash.repository.BarangRepository;
import com.pbo2.washywash.repository.DetailPenjualanRepository;

@Service
public class BarangService {
    
    private final BarangRepository barangRepository;
    private final DetailPenjualanRepository detailPenjualanRepository;

    public BarangService(BarangRepository barangRepository, DetailPenjualanRepository detailPenjualanRepository) {
        this.barangRepository = barangRepository;
        this.detailPenjualanRepository = detailPenjualanRepository;
    }

    public List<Barang> getAllBarang() {
        return barangRepository.findByStatus("Aktif");
    }

    public Barang getBarangByKode(String kodeBarang) {
        return barangRepository.findBarangAktifByKode(kodeBarang);
    }

    public void tambahBarang(Barang barang) {
        if (barang.getKodeBarang() == null || barang.getKodeBarang().isEmpty()) {
            barang.setKodeBarang(generateKodeBarang(barang.getNamaBarang()));
        }

        barang.setStatus("Aktif");
        barangRepository.save(barang);
    }

    public void updateBarang(Barang barang) {

       Barang lama = barangRepository.findBarangAktifByKode(barang.getKodeBarang());
        if (lama == null) {
            throw new RuntimeException("Barang tidak ditemukan");
        }

        lama.setNamaBarang(barang.getNamaBarang());
        lama.setKategori(barang.getKategori());
        lama.setSatuan(barang.getSatuan());
        lama.setHarga(barang.getHarga());
        lama.setStok(barang.getStok());

        barangRepository.save(lama);
    }
    
    public void hapusBarang(String kodeBarang) {
        if (detailPenjualanRepository.existsByBarang_KodeBarang(kodeBarang)) {
            throw new IllegalArgumentException(
                    "Barang tidak dapat dihapus karena sudah pernah digunakan dalam transaksi.");
        }

        Barang barang = barangRepository.findByKodeBarang(kodeBarang);

        if (barang != null) {
            barang.setStatus("Tidak Aktif");
            barangRepository.save(barang);
        }
    }

    public String generateKodeBarang(String namaBarang) {

        String prefix = namaBarang.replaceAll("\\s+", "").toUpperCase();

        if (prefix.length() >= 3) {
            prefix = prefix.substring(0, 3);
        } else {
            while (prefix.length() < 3) {
                prefix += "X";
            }
        }
        String lastKode = barangRepository.findLastKodeBarangByPrefix(prefix);

        if (lastKode == null) {
            return prefix + "001";
        }

        int nomor = Integer.parseInt(lastKode.substring(3));
        return prefix + String.format("%03d", nomor + 1);
    }

    public List<Barang> getStokBarang10Terendah() {
        return barangRepository.find10stokterendah();
    }

    public List<Barang> cariBarang(String keyword) {
    return barangRepository.findByStatusAndKeyword("Aktif", keyword);
}
    
// @Repository
// public class BarangRepository {
}
