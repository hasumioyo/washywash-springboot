package com.pbo2.washywash.service;

import org.springframework.stereotype.Service;

import com.pbo2.washywash.model.DetailPenjualan;
import com.pbo2.washywash.repository.DetailPenjualanRepository;

import java.util.List;

public class DetailPenjualanService {
    
    private final DetailPenjualanRepository detailpenjualanRepository;

    public DetailPenjualanService(DetailPenjualanRepository detailPenjualanRepository) {
        this.detailpenjualanRepository = detailPenjualanRepository;
    }

    public void tambahDetail(DetailPenjualan detail) {
        validateDetail(detail);

        detail.setSubTotal(detail.getQty() * detail.getHarga());

        detailpenjualanRepository.save(detail);
    }

    public List<DetailPenjualan> getByKodePenjualan(String kodePenjualan) {
        return detailpenjualanRepository.findByPenjualan_KodePenjualan(kodePenjualan);
    }

    public double hitungTotal(String kodePenjualan) {
        List<DetailPenjualan> details = getByKodePenjualan(kodePenjualan);

        double total = 0;

        for(DetailPenjualan d : details){
            total += d.getsubTotal();
        }
        return total;
    }

    private void validateDetail(DetailPenjualan detail) {
        if (detail.getPenjualan() == null) {
            throw new IllegalArgumentException(
                    "Penjualan wajib diisi");
        }

        if (detail.getBarang() == null) {
            throw new IllegalArgumentException(
                    "Barang wajib diisi");
        }

        if (detail.getQty() <= 0) {
            throw new IllegalArgumentException(
                    "Qty harus lebih dari 0");
        }
    }

}
