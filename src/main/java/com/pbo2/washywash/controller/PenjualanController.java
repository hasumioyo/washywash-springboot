package com.pbo2.washywash.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.pbo2.washywash.model.Penjualan;
import com.pbo2.washywash.service.PenjualanService;
import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/penjualan")
public class PenjualanController {
    private final PenjualanService penjualanService;

    public PenjualanController(PenjualanService penjualanService) {
        this.penjualanService = penjualanService;
    }

    private boolean belumLogin(HttpSession session) {
        return session.getAttribute("loggedInUser") == null;
    }

    @GetMapping
    public String index(HttpSession session, Model model) {
        if (belumLogin(session)) {
            return "redirect:/";
        }

        model.addAttribute("penjualan", new Penjualan());
        return "penjualan/form";
    }

    @PostMapping("path")
    public String simpan(@ModelAttribute Penjualan penjualan, HttpSession session, Model model) {
        penjualanService.tambahPenjualan(penjualan);
        
        return "redirect:/penjualan";
    }

    @GetMapping("/hapus/{kodePenjualan}")
    public String hapus(@PathVariable String kodePenjualan) {
        penjualanService.hapusPenjualan(kodePenjualan);
        return "redirect:/penjualan";
    }



}
