package com.pbo2.washywash.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pbo2.washywash.model.Pelanggan;
import com.pbo2.washywash.repository.PelangganRepository;

@Service
public class PelangganService {
    private final PelangganRepository pelangganRepository;

    public PelangganService(PelangganRepository pelangganRepository) {
        this.pelangganRepository = pelangganRepository;
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
}
