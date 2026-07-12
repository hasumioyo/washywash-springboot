package com.pbo2.washywash.service;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;


import com.pbo2.washywash.model.Penjualan;
import com.pbo2.washywash.repository.PenjualanRepository;

@Service
public class PenjualanService {
    
    private final PelangganService pelangganService;
    private final PenjualanRepository penjualanRepository;
    private final DetailPenjualanService detailPenjualanService;

    public PenjualanService(PenjualanRepository penjualanRepository, DetailPenjualanService detailPenjualanService, PelangganService pelangganService) {
        this.penjualanRepository = penjualanRepository;
        this.detailPenjualanService = detailPenjualanService;
        this.pelangganService = pelangganService;
    }

    public void tambahPenjualan(Penjualan penjualan) {

    double kembalian = penjualan.getTotalPembayaran()
            - penjualan.getTotalPenjualan();

    if (kembalian < 0) {
        throw new IllegalArgumentException("Uang pembayaran kurang");
    }

    penjualan.setHasilKembalian(kembalian);

    penjualanRepository.save(penjualan);
    }

    public List<Penjualan> getAllPenjualan() {
        return penjualanRepository.findAll();
    }

    public void hapusPenjualan(String kodePenjualan) {
        penjualanRepository.deleteById(kodePenjualan);
    }

    public Penjualan getByKode(String kodePenjualan) {
        return penjualanRepository.findById(kodePenjualan).orElseThrow(() -> new IllegalArgumentException("Penjualan tidak ditemukan"));
    }

    public List<Penjualan> getLaporanBulanan(int bulan, int tahun){
        return penjualanRepository.findLaporanBulanan(bulan, tahun);
    }

    public Double getTotalPendapatanBulanan(int bulan, int tahun) {
        Double total = penjualanRepository.getTotalPendapatanBulanan(bulan, tahun);

        return total == null ? 0.0 : total;
    }

    public Long getJumlahTransaksiBulanan(int bulan, int tahun) {
        return penjualanRepository.getJumlahTransaksiBulanan(bulan, tahun);
    }

    public List<Penjualan> cariPenjualan(String keyword) {
        return penjualanRepository.cariKodePenjualan(keyword);
    }
}
