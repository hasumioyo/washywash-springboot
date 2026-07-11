package com.pbo2.washywash.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pbo2.washywash.model.Barang;
import com.pbo2.washywash.service.BarangService;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
@RequestMapping("/barang")
public class BarangController {
     private final BarangService barangService;

    public BarangController(BarangService barangService) {
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

        model.addAttribute("listBarang", barangService.getAllBarang());
        return "barang/index";
    }

    @GetMapping("/tambah")
    public String tambahForm(HttpSession session, Model model) {
        if (belumLogin(session)) {
            return "redirect:/";
        }

        Barang barang = new Barang();
        barang.setKodeBarang(barangService.generateKodeBarang());
        System.out.println(barang.getKodeBarang());
        model.addAttribute("barang", barang);
        model.addAttribute("mode", "tambah");
        return "barang/form";
    }

    @PostMapping("/simpan")
    public String simpan(@ModelAttribute Barang barang) {
        barangService.tambahBarang(barang);
        return "redirect:/barang";
    }

    @GetMapping("/edit/{kodeBarang}")
    public String editForm(
            @PathVariable String kodeBarang,
            HttpSession session,
            Model model) {

        if (belumLogin(session)) {
            return "redirect:/";
        }

        model.addAttribute("barang", barangService.getBarangByKode(kodeBarang));
        model.addAttribute("mode", "edit");
        return "barang/form";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Barang barang) {
        barangService.updateBarang(barang);
        return "redirect:/barang";
    }

    @GetMapping("/hapus/{kodeBarang}")
    public String hapus(@PathVariable String kodeBarang) {
        barangService.hapusBarang(kodeBarang);
        return "redirect:/barang";
    }

    @GetMapping("/cari")
    public String cariBarang(@RequestParam String keyword, Model model, HttpSession session) {
        
        if(belumLogin(session)) {
            return "redirect:/";
        }
        
        model.addAttribute("listBarang", barangService.cariBarang(keyword));

        return "barang/index";
    }


    

    
    
}
