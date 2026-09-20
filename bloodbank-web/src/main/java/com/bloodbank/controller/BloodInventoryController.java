package com.bloodbank.controller;

import com.bloodbank.dao.BloodInventoryDAO;
import com.bloodbank.dao.BloodInventoryDAOImpl;
import com.bloodbank.model.BloodInventory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class BloodInventoryController {

    private static final String[] BLOOD_GROUPS = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};

    private final BloodInventoryDAO inventoryDAO = new BloodInventoryDAOImpl();

    @GetMapping("/inventory")
    public String showInventory(Model model) {
        model.addAttribute("bloodInventory", new BloodInventory());
        loadInventoryData(model);
        return "inventory";
    }

    @PostMapping("/inventory")
    public String addStock(@ModelAttribute BloodInventory bloodInventory, Model model) {
        System.out.println(">>> POST /inventory received, blood group: " + bloodInventory.getBloodGroup()
                + ", units: " + bloodInventory.getAvailableUnits());

        if (bloodInventory.getCollectionDate() == null || bloodInventory.getExpiryDate() == null) {
            model.addAttribute("addSuccess", false);
            model.addAttribute("addError", "Please provide both a collection date and an expiry date.");
        } else {
            try {
                int inventoryId = inventoryDAO.addStock(bloodInventory);
                System.out.println(">>> Stock added successfully, inventoryId=" + inventoryId);
                model.addAttribute("addSuccess", true);
            } catch (SQLException e) {
                model.addAttribute("addSuccess", false);
                model.addAttribute("addError", "Could not add stock: " + e.getMessage());
            }
        }

        model.addAttribute("bloodInventory", new BloodInventory());
        loadInventoryData(model);
        return "inventory";
    }

    private void loadInventoryData(Model model) {
        try {
            model.addAttribute("allStock", inventoryDAO.getAllStock());

            Map<String, Integer> totals = new LinkedHashMap<>();
            for (String group : BLOOD_GROUPS) {
                totals.put(group, inventoryDAO.getTotalAvailableUnits(group));
            }
            model.addAttribute("totals", totals);

        } catch (SQLException e) {
            model.addAttribute("loadError", "Could not load inventory: " + e.getMessage());
        }
    }
}