package com.pbo2.washywash.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.pbo2.washywash.model.Pelanggan;
import com.pbo2.washywash.model.Penjualan;
import com.pbo2.washywash.model.Barang;
import com.pbo2.washywash.model.DetailPenjualan;
import com.pbo2.washywash.service.PenjualanService;
import com.pbo2.washywash.service.BarangService;
import com.pbo2.washywash.service.DetailPenjualanService;
import com.pbo2.washywash.service.PelangganService;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
@RequestMapping("/penjualan")
public class PenjualanController {
    private final PelangganService pelangganService;
    private final PenjualanService penjualanService;
    private final BarangService barangService;
    private final DetailPenjualanService detailPenjualanService;

    public PenjualanController(PenjualanService penjualanService, DetailPenjualanService detailPenjualanService, PelangganService pelangganService, BarangService barangService) {
        this.penjualanService = penjualanService;
        this.detailPenjualanService = detailPenjualanService;
        this.pelangganService = pelangganService;
        this.barangService = barangService;
    }

    private boolean belumLogin(HttpSession session) {
        return session.getAttribute("loggedInUser") == null;
    }

    @GetMapping
    public String index(HttpSession session, Model model) {
        if (belumLogin(session)) {
            return "redirect:/";
        }

        List<DetailPenjualan> basketBarang = (List<DetailPenjualan>) session.getAttribute("basketBarang");

        Pelanggan pelanggan = (Pelanggan) session.getAttribute("pelanggan");

        if(basketBarang == null) {
            basketBarang = new ArrayList<>();
        }

        double total = 0;

        for (DetailPenjualan detail : basketBarang) {
            total += detail.getSubTotal();
        }

        model.addAttribute("penjualan", new Penjualan());
        model.addAttribute("details", basketBarang);
        model.addAttribute("pelanggan", pelanggan);
        model.addAttribute("listBarang", barangService.getAllBarang());
        model.addAttribute("total", total);
        

        return "penjualan/form";
    }

    @PostMapping("pathpenjualan")
    public String simpan(@ModelAttribute Penjualan penjualan, HttpSession session, Model model) {
        penjualanService.tambahPenjualan(penjualan);
        
        return "redirect:/penjualan";
    }

    @GetMapping("/hapus/{kodePenjualan}")
    public String hapus(@PathVariable String kodePenjualan) {
        penjualanService.hapusPenjualan(kodePenjualan);
        return "redirect:/penjualan";
    }

    // @GetMapping("/{kodePenjualan}")
    // public String detailPenjualan(@PathVariable String kodePenjualan, Model model) {
    //     Penjualan penjualan = penjualanService.getByKode(kodePenjualan);

    //     List<DetailPenjualan> details = detailPenjualanService.getByKodePenjualan(kodePenjualan);

    //     model.addAttribute("penjualan", penjualan);
    //     model.addAttribute("details", details);

    //     return "penjualan/detail";
    // }
    
    @GetMapping("/cari")
    public String cariPelanggan(@RequestParam String kodePelanggan, Model model, HttpSession session) {
        if(belumLogin(session)) {
            return "redirect:/";
        }

        List<DetailPenjualan> basketBarang = (List<DetailPenjualan>) session.getAttribute("basketBarang");

        if(basketBarang == null) {
            basketBarang = new ArrayList<>();
        }

        Pelanggan pelanggan = pelangganService.getPelangganbyKode(kodePelanggan);

        model.addAttribute("penjualan", new Penjualan());
        model.addAttribute("details", basketBarang);
        model.addAttribute("listBarang", barangService.getAllBarang());
        model.addAttribute("pelanggan", pelanggan);

        session.setAttribute("pelanggan", pelanggan); //simpan nama
        

        return "penjualan/form";
    }

    @GetMapping("/tambahbarang/{kodeBarang}")
    public String tambahBarang(@PathVariable String kodeBarang, HttpSession session) {

        Barang barang = barangService.getBarangByKode(kodeBarang);

        List<DetailPenjualan> basketBarang = (List<DetailPenjualan>) session.getAttribute("basketBarang");

        if(basketBarang == null) {
            basketBarang = new ArrayList<>();
        }
        
        // DetailPenjualan detail = new DetailPenjualan();

        boolean addBarang = false;

    for (DetailPenjualan detail : basketBarang) {

        if (detail.getBarang().getKodeBarang().equals(kodeBarang)) {

            detail.setQty(detail.getQty() + 1);

            detail.setSubTotal(detail.getQty() * detail.getHarga());
            addBarang = true;
            break;
        }
    }

    if (!addBarang) {

        DetailPenjualan detail = new DetailPenjualan();

        detail.setBarang(barang);
        detail.setQty(1);
        detail.setHarga(barang.getHarga());

        detail.setSubTotal(detail.getQty() * detail.getHarga());
        basketBarang.add(detail);
    }

        // detail.setBarang(barang);
        // detail.setQty(1);
        // detail.setHarga(barang.getHarga());
        // detail.setSubTotal(barang.getHarga());

        // basketBarang.add(detail);

        session.setAttribute("basketBarang", basketBarang);

        return "redirect:/penjualan";
    }
    
    



}
