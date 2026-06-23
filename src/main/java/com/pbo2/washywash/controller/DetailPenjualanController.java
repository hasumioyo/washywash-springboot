package com.pbo2.washywash.controller;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.pbo2.washywash.model.DetailPenjualan;
import com.pbo2.washywash.model.Penjualan;
import com.pbo2.washywash.service.DetailPenjualanService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/detailpenjualan")
public class DetailPenjualanController {
    private final DetailPenjualanService detailPenjualanService;

    public DetailPenjualanController(DetailPenjualanService detailPenjualanService) {
        this.detailPenjualanService = detailPenjualanService;
    }

    private boolean belumLogin(HttpSession session) {
        return session.getAttribute("loggedInUser") == null;
    }

    @GetMapping("/{kodePenjualan}")
    public String index(@PathVariable String kodePenjualan, HttpSession session, Model model) {

        if (belumLogin(session)) {
            return "redirect:/";
        }

        model.addAttribute("listDetail", detailPenjualanService.getByKodePenjualan(kodePenjualan));
        model.addAttribute("kodePenjualan", kodePenjualan);

        return "detailpenjualan/index";
    }

    
    

}
