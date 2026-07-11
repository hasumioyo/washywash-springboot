package com.pbo2.washywash.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pbo2.washywash.model.Pelanggan;
import com.pbo2.washywash.service.PelangganService;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@Controller
@RequestMapping("/pelanggan")
public class PelangganController {
    private final PelangganService pelangganService;

    public PelangganController(PelangganService pelangganService) {
        this.pelangganService = pelangganService;
    }

    private boolean belumLogin(HttpSession session) {
        return session.getAttribute("loggedInUser") == null;
    }

    @GetMapping
    public String index(HttpSession session, Model model) {
        if (belumLogin(session)) {
            return "redirect:/";
        }

        model.addAttribute("listPelanggan", pelangganService.getAllPelanggan());
        return "pelanggan/index";
    }

    @GetMapping("/tambah")
    public String tambahPelanggan(HttpSession session, Model model) {
        if (belumLogin(session)) {
            return "redirect:/";
        }

        Pelanggan pelanggan = new Pelanggan();
        
        model.addAttribute("pelanggan", pelanggan);
        model.addAttribute("mode", "tambah");
        return "pelanggan/formpelanggan";
    }

    @PostMapping("/simpan")
    public String simpan(@ModelAttribute Pelanggan pelanggan) {
        pelangganService.tambahPelanggan(pelanggan);
        pelanggan.setKodePelanggan(pelangganService.generateKodePelanggan(pelanggan.getNamaPelanggan()));
        System.out.println(pelanggan.getKodePelanggan());
        return "redirect:/pelanggan";
    }

    @GetMapping("/edit/{kodePelanggan}")
    public String editForm(
            @PathVariable String kodePelanggan,
            HttpSession session,
            Model model) {

        if (belumLogin(session)) {
            return "redirect:/";
        }

        model.addAttribute("pelanggan", pelangganService.getPelangganbyKode(kodePelanggan));
        model.addAttribute("mode", "edit");
        return "pelanggan/form";
    }

    // @PostMapping("/update")
    // public String update(@ModelAttribute Pelanggan pelanggan) {
    //     pelangganService.updatePelanggan(pelanggan);
    //     return "redirect:/barang";
    // }

    @GetMapping("/cari")
    public String cariPelanggan(@RequestParam String keyword, Model model, HttpSession session) {
        if(belumLogin(session)) {
            return "redirect:/";
        }

        model.addAttribute("listPelanggan", pelangganService.cariPelanggan(keyword));
        
        return "pelanggan/index";
    }
    

    @GetMapping("/hapus/{kodePelanggan}")
    public String hapusPelanggan(@PathVariable String kodePelanggan, RedirectAttributes redirectAttributes) {
        // pelangganService.hapusPelanggan(kodePelanggan);

        try {
        pelangganService.hapusPelanggan(kodePelanggan);
        redirectAttributes.addFlashAttribute("success", "Pelanggan berhasil dihapus.");
        
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/pelanggan";
    }
    

    


}
