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

        List<DetailPenjualan> basketBarang = (List<DetailPenjualan>) session.getAttribute("basketBarang");

        if (basketBarang != null) {

            for (int i = 0; i < basketBarang.size(); i++) {

                DetailPenjualan detail = basketBarang.get(i);

                if (detail.getBarang().getKodeBarang().equals(kodeBarang)) {

                    if (detail.getQty() > 1) {

                        detail.setQty(detail.getQty() - 1);
                        detail.setSubTotal(detail.getQty() * detail.getHarga());

                    } else {

                        basketBarang.remove(i);
                    }

                    break;
                }
            }
        }

        session.setAttribute("basketBarang", basketBarang);

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
    public String tambahBarang(@PathVariable String kodeBarang,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        // Ambil barang dari database
        Barang barang = barangService.getBarangByKode(kodeBarang);

        // Ambil basket dari session
        List<DetailPenjualan> basketBarang = (List<DetailPenjualan>) session.getAttribute("basketBarang");

        if (basketBarang == null) {
            basketBarang = new ArrayList<>();
        }

        // Jika stok habis
        if (barang.getStok() <= 0) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Stok " + barang.getNamaBarang() + " habis.");

            return "redirect:/penjualan";
        }

        boolean ditemukan = false;

        // Cek apakah barang sudah ada di keranjang
        for (DetailPenjualan detail : basketBarang) {

            if (detail.getBarang().getKodeBarang().equals(kodeBarang)) {

                // Qty tidak boleh melebihi stok
                if (detail.getQty() >= barang.getStok()) {

                    redirectAttributes.addFlashAttribute(
                            "error",
                            "Stok " + barang.getNamaBarang() + " tidak mencukupi.");

                    return "redirect:/penjualan";
                }

                detail.setQty(detail.getQty() + 1);
                detail.setSubTotal(detail.getQty() * detail.getHarga());

                ditemukan = true;
                break;
            }
    }

    // Jika belum ada di keranjang
    if (!ditemukan) {

        DetailPenjualan detail = new DetailPenjualan();

        detail.setBarang(barang);
        detail.setQty(1);
        detail.setHarga(barang.getHarga());
        detail.setSubTotal(barang.getHarga());

        basketBarang.add(detail);
    }

    // Simpan kembali basket ke session
    session.setAttribute("basketBarang", basketBarang);

    return "redirect:/penjualan";
}
    

    @PostMapping("/bayar")
    public String bayar(@ModelAttribute Penjualan penjualan,
                        HttpSession session,
                        Model model) {  

        List<DetailPenjualan> basketBarang =
                (List<DetailPenjualan>) session.getAttribute("basketBarang");

        if (basketBarang == null || basketBarang.isEmpty()) {
            model.addAttribute("error", "Keranjang masih kosong.");
            return "penjualan/form";
        }

        // Hitung total
        double total = 0;

        for (DetailPenjualan detail : basketBarang) {
            total += detail.getSubTotal();
        }

        penjualan.setTotalPenjualan(total);

        // Hitung kembalian
        double kembalian = penjualan.getTotalPembayaran() - total;

        if (kembalian < 0) {

            model.addAttribute("error", "Uang pembayaran kurang.");

            model.addAttribute("details", basketBarang);
            model.addAttribute("penjualan", penjualan);
            model.addAttribute("listBarang", barangService.getAllBarang());
            model.addAttribute("pelanggan", session.getAttribute("pelanggan"));
            model.addAttribute("total", total);

            return "penjualan/form";
        }

        // ===============================
        // CEK STOK DULU
        // ===============================
        for (DetailPenjualan detail : basketBarang) {

            Barang barang = barangService.getBarangByKode(
                    detail.getBarang().getKodeBarang());

            if (barang.getStok() < detail.getQty()) {

                model.addAttribute("error",
                        "Stok barang " + barang.getNamaBarang() + " tidak mencukupi.");

                model.addAttribute("details", basketBarang);
                model.addAttribute("penjualan", penjualan);
                model.addAttribute("listBarang", barangService.getAllBarang());
                model.addAttribute("pelanggan", session.getAttribute("pelanggan"));
                model.addAttribute("total", total);

                return "penjualan/form";
            }
        }

        // ===============================
        // SET DATA PENJUALAN
        // ===============================
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        penjualan.setKodePenjualan(
                "PJ" + LocalDateTime.now().format(formatter));

        penjualan.setTanggalPenjualan(LocalDateTime.now());

        Pelanggan pelanggan = (Pelanggan) session.getAttribute("pelanggan");

         if(pelanggan != null) {
            penjualan.setPelanggan(pelanggan);
        }

        penjualan.setHasilKembalian(kembalian);

        // ===============================
        // SIMPAN PENJUALAN
        // ===============================
        penjualanService.tambahPenjualan(penjualan);

        // ===============================
        // SIMPAN DETAIL PENJUALAN
        // ===============================
        for (DetailPenjualan detail : basketBarang) {

            detail.setPenjualan(penjualan);

            detailPenjualanService.tambahDetail(detail);
        }

        //stok dari database mulai berkurang setelah bayar
        for (DetailPenjualan detail : basketBarang) {

            Barang barang = barangService.getBarangByKode(
                    detail.getBarang().getKodeBarang());

            barang.setStok(
                    barang.getStok() - detail.getQty());

            barangService.updateBarang(barang);
        }
        
        model.addAttribute("kembalian", kembalian);

        model.addAttribute("details", basketBarang);
        model.addAttribute("penjualan", penjualan);
        model.addAttribute("listBarang", barangService.getAllBarang());
        model.addAttribute("pelanggan", session.getAttribute("pelanggan"));

       
        model.addAttribute("total", total);

  
    //menghapus sesi setelah keluar dari halaman pembayaran
    session.removeAttribute("basketBarang");
    session.removeAttribute("pelanggan");

    // Redirect agar form kosong kembali
    return "redirect:/penjualan/strukpembayaran/" + penjualan.getKodePenjualan();    
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

    @GetMapping("/strukpembayaran/{kodePenjualan}")
    public String strukPembayaran(@PathVariable String kodePenjualan, Model model) {

        Penjualan penjualan = penjualanService.getByKode(kodePenjualan);

        List<DetailPenjualan> detail = detailPenjualanService.getByKodePenjualan(kodePenjualan);

        model.addAttribute("penjualan", penjualan);
        model.addAttribute("details", detail);
        model.addAttribute("mode", "detail");
        model.addAttribute("pelanggan", penjualan.getPelanggan());

        return "penjualan/strukpembayaran";
    }

    @GetMapping("/detail/{kodePenjualan}")
    public String detailPenjualan(@PathVariable String kodePenjualan, Model model) {
        Penjualan penjualan = penjualanService.getByKode(kodePenjualan);

        List<DetailPenjualan> details = detailPenjualanService.getByKodePenjualan(kodePenjualan);

        model.addAttribute("penjualan", penjualan);
        model.addAttribute("details", details);
        model.addAttribute("mode", "detail");

        return "penjualan/detail";
    }
    
    

}
