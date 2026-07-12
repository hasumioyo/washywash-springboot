package com.pbo2.washywash.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pbo2.washywash.model.Pelanggan;
import com.pbo2.washywash.repository.PelangganRepository;
import com.pbo2.washywash.repository.PenjualanRepository;

@Service
public class PelangganService {
    private final PelangganRepository pelangganRepository;
    private final PenjualanRepository penjualanRepository;

    public PelangganService(PelangganRepository pelangganRepository, PenjualanRepository penjualanRepository) {
        this.pelangganRepository = pelangganRepository;
        this.penjualanRepository = penjualanRepository;
    }

    public String generateKodePelanggan(String name) {
        String prefix = name.substring(0, 1).toUpperCase();

        String lastkode = pelangganRepository.findLastKodeByPrefix(prefix);

        if (lastkode == null) {
            return prefix + "0001";
        }

        int angka = Integer.parseInt(lastkode.substring(1));

        return prefix + String.format("%04d", angka + 1);

    }

    public List<Pelanggan> getAllPelanggan() {
        return pelangganRepository.findAll();
    }

    public Pelanggan getPelangganbyKode(String kodePelanggan) {
        return pelangganRepository.findByKodePelanggan(kodePelanggan);
    }

    public void tambahPelanggan(Pelanggan pelanggan) {
        if(pelanggan.getKodePelanggan() == null || pelanggan.getKodePelanggan().isEmpty()) {
            pelanggan.setKodePelanggan(generateKodePelanggan(pelanggan.getNamaPelanggan()));
        }
        pelangganRepository.save(pelanggan);
    }

    public List<Pelanggan> cariPelanggan(String keyword) {
        return pelangganRepository.findByKodePelangganContainingIgnoreCaseOrNamaPelangganIgnoreCase(keyword, keyword);
    }

    public void hapusPelanggan(String kodePelanggan) {

        Pelanggan pelanggan = getPelangganbyKode(kodePelanggan);

        if(penjualanRepository.existsByPelanggan(pelanggan)) {
            throw new IllegalArgumentException("Pelanggan masih memiliki riwayat penjualan. Hapus riwayat penjualan terlebih dahulu.");
        }
        
        pelangganRepository.deleteById(kodePelanggan);
    }

    public void updatePelanggan(Pelanggan pelanggan) {
        Pelanggan lama = pelangganRepository.findByKodePelanggan(pelanggan.getKodePelanggan());

        if (lama == null) {
            throw new RuntimeException("Pelanggan tidak ditemukan.");
        }

        lama.setNamaPelanggan(pelanggan.getNamaPelanggan());
        lama.setNoHP(pelanggan.getNoHP());
        lama.setEmail(pelanggan.getEmail());

        pelangganRepository.save(lama);
    }

}
