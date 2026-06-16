package com.pbo2.washywash.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.pbo2.washywash.model.User;
import com.pbo2.washywash.model.Barang;
import com.pbo2.washywash.service.BarangService;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

    private final BarangService barangService;

    public DashboardController(BarangService barangService) {
        this.barangService = barangService;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        // Object user = session.getAttribute("loggedInUser");

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/";
        }

        model.addAttribute("user", user);
        model.addAttribute("StokTerdikit", barangService.getStokBarang10Terendah());
        return "dashboard";
    }


    
}
