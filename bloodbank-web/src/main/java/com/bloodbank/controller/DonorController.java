package com.bloodbank.controller;

import com.bloodbank.dao.DonorDAO;
import com.bloodbank.dao.DonorDAOImpl;
import com.bloodbank.model.Donor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.SQLException;

@Controller
public class DonorController {

    private final DonorDAO donorDAO = new DonorDAOImpl();

    @GetMapping("/donor")
    public String showForm(Model model) {
        model.addAttribute("donor", new Donor());
        return "donor-form";
    }

    @PostMapping("/donor")
    public String handleRegistration(@ModelAttribute Donor donor, Model model) {
        System.out.println(">>> POST /donor received, name: " + donor.getName());

        try {
            int donorId = donorDAO.addDonor(donor);

            if (donorId == -1) {
                model.addAttribute("donor", donor);
                model.addAttribute("success", false);
                model.addAttribute("error", "Donor could not be registered. Please try again.");
                return "donor-result";
            }

            donor.setDonorId(donorId);
            model.addAttribute("donor", donor);
            model.addAttribute("success", true);
            System.out.println(">>> Donor saved successfully, donorId=" + donorId);
            return "donor-result";

        } catch (SQLException e) {
            model.addAttribute("donor", donor);
            model.addAttribute("success", false);
            model.addAttribute("error", "Database error: " + e.getMessage());
            return "donor-result";
        }
    }
}