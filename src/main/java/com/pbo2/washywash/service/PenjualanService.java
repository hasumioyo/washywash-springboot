package com.pbo2.washywash.service;

import java.util.List;

import org.springframework.stereotype.Service;


import com.pbo2.washywash.model.Penjualan;
import com.pbo2.washywash.repository.PenjualanRepository;

@Service
public class PenjualanService {
    
    private final PenjualanRepository penjualanRepository;
    private final DetailPenjualanService detailPenjualanService;

    public PenjualanService(PenjualanRepository penjualanRepository, DetailPenjualanService detailPenjualanService) {
        this.penjualanRepository = penjualanRepository;
        this.detailPenjualanService = detailPenjualanService;
    }

    public void tambahPenjualan(Penjualan penjualan) {
        double total = detailPenjualanService.hitungTotal(penjualan.getKodePenjualan());

        penjualan.settotalPenjualan(total - penjualan.getDiskon());

        penjualanRepository.save(penjualan);
    }

    public List<Penjualan> getAllPenjualan() {
        return penjualanRepository.findAll();
    }

    public void hapusPenjualan(String kodePenjualan) {
        penjualanRepository.deleteById(kodePenjualan);
    }
}
