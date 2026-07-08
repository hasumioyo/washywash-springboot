package com.pbo2.washywash.controller;

import com.pbo2.washywash.repository.PenjualanRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@Controller
@RequestMapping("/penjualan")
public class PenjualanController {
    private final PenjualanRepository penjualanRepository;
    private final PelangganService pelangganService;
    private final PenjualanService penjualanService;
    private final BarangService barangService;
    private final DetailPenjualanService detailPenjualanService;

    public PenjualanController(PenjualanService penjualanService, DetailPenjualanService detailPenjualanService, PelangganService pelangganService, BarangService barangService, PenjualanRepository penjualanRepository) {
        this.penjualanService = penjualanService;
        this.detailPenjualanService = detailPenjualanService;
        this.pelangganService = pelangganService;
        this.barangService = barangService;
        this.penjualanRepository = penjualanRepository;
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

    @GetMapping("/kurang/{kodeBarang}")
    public String kurangBarang(@PathVariable String kodeBarang, HttpSession session) {

    List<DetailPenjualan> basketBarang =(List<DetailPenjualan>) session.getAttribute("basketBarang");


    if (basketBarang != null) {

        for (int i = 0; i < basketBarang.size(); i++) {

            DetailPenjualan detail =
                    basketBarang.get(i);

            if (detail.getBarang()
                    .getKodeBarang()
                    .equals(kodeBarang)) {

                if (detail.getQty() > 1) {

                    detail.setQty(
                            detail.getQty() - 1);

                    detail.setSubTotal(
                            detail.getQty()
                            * detail.getHarga());

                } else {

                    basketBarang.remove(i);
                }

                break;
            }
        }
    }

    session.setAttribute(
            "basketBarang",
            basketBarang);

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
    public String tambahBarang(@PathVariable String kodeBarang, HttpSession session,  RedirectAttributes redirectAttributes) {

        Barang barang = barangService.getBarangByKode(kodeBarang);

        List<DetailPenjualan> basketBarang = (List<DetailPenjualan>) session.getAttribute("basketBarang");

        if(basketBarang == null) {
            basketBarang = new ArrayList<>();
        }
        
        if (barang.getStok() <= 0) {
            redirectAttributes.addFlashAttribute("error", "Stok barang habis.");
            return "redirect:/penjualan";
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

        // barang.setStok(barang.getStok() - 1);
        // barangService.updateBarang(barang);

            // detail.setBarang(barang);
            // detail.setQty(1);
            // detail.setHarga(barang.getHarga());
            // detail.setSubTotal(barang.getHarga());

            // basketBarang.add(detail);

            session.setAttribute("basketBarang", basketBarang);

            return "redirect:/penjualan";
    }
    

    @PostMapping("/bayar")
        public String bayar(@ModelAttribute Penjualan penjualan, HttpSession session, Model model) {

        List<DetailPenjualan> basketBarang = (List<DetailPenjualan>) session.getAttribute("basketBarang");

        double total = 0;

        for (DetailPenjualan d : basketBarang) {
            total += d.getSubTotal();
        }

        penjualan.setTotalPenjualan(total);

        double kembalian = penjualan.getTotalPembayaran()- total;

        if(kembalian < 0){
            model.addAttribute("error", "Uang pembayaran kurang");

            return "penjualan/form";
        } else {

            penjualan.setHasilKembalian(kembalian);

            // Membuat kode penjualan otomatis
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

            penjualan.setKodePenjualan(
                    "PJ" + LocalDateTime.now().format(formatter));

            // Menyimpan tanggal transaksi
            penjualan.setTanggalPenjualan(LocalDate.now());

            // Mengambil pelanggan dari session
            penjualan.setPelanggan((Pelanggan) session.getAttribute("pelanggan"));

            // Simpan penjualan
            penjualanService.tambahPenjualan(penjualan);

            // Simpan detail penjualan
            for (DetailPenjualan detail : basketBarang) {
                detail.setPenjualan(penjualan);
                detailPenjualanService.tambahDetail(detail);
            }


            //kurangkan stok
            for(DetailPenjualan detail : basketBarang) {
                Barang barang = detail.getBarang();
                barang.setStok(barang.getStok()- detail.getQty());

                barangService.updateBarang(barang);
            }

            session.removeAttribute("basketBarang");
            session.removeAttribute("pelanggan");

            model.addAttribute("kembalian", kembalian);
        }

        //btw ini biar kekirim semua data jak
        model.addAttribute("details", basketBarang);
        model.addAttribute("penjualan", penjualan);
        model.addAttribute("listBarang", barangService.getAllBarang());
        model.addAttribute("pelanggan", session.getAttribute("pelanggan"));
        model.addAttribute("total", total);

        return "penjualan/form";
    }

    @GetMapping("/selesai")
    public String selesai(HttpSession session){

        session.removeAttribute("basketBarang");
        session.removeAttribute("pelanggan");

        return "redirect:/penjualan";
    }

    @GetMapping("/listpenjualan")
    public String listPenjualan(HttpSession session, Model model) {
        if (belumLogin(session)) {
            return "redirect:/";
        }
        
        model.addAttribute("listPenjualan", penjualanService.getAllPenjualan());

        return "penjualan/index";
    }
    

}
