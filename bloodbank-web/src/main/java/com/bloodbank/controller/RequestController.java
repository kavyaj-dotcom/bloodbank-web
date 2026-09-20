package com.bloodbank.controller;

import com.bloodbank.dao.*;
import com.bloodbank.exception.InsufficientInventoryException;
import com.bloodbank.model.BloodRequest;
import com.bloodbank.service.DonorMatchingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Controller
public class RequestController {

    private final BloodRequestDAO requestDAO = new BloodRequestDAOImpl();
    private final BloodInventoryDAO inventoryDAO = new BloodInventoryDAOImpl();
    private final DonorDAO donorDAO = new DonorDAOImpl();
    private final DonorMatchingService matchingService = new DonorMatchingService(donorDAO);

    @GetMapping("/request")
    public String showForm(Model model) {
        model.addAttribute("bloodRequest", new BloodRequest());
        return "request-form";
    }

    @PostMapping("/request")
    public String handleRequest(@ModelAttribute BloodRequest bloodRequest, Model model) {
        System.out.println(">>> POST /request received, blood group: " + bloodRequest.getPatientBloodGroup());
        bloodRequest.setRequestDate(LocalDate.now());
        bloodRequest.setStatus(BloodRequest.STATUS_PENDING);
        model.addAttribute("bloodRequest", bloodRequest);

        try {
            int requestId = requestDAO.addRequest(bloodRequest);
            bloodRequest.setRequestId(requestId);

            inventoryDAO.deductUnits(bloodRequest.getPatientBloodGroup(), bloodRequest.getUnitsRequired());
            requestDAO.updateStatus(requestId, BloodRequest.STATUS_FULFILLED);
            model.addAttribute("fulfilled", true);
            System.out.println(">>> Returning request-result view, fulfilled=true, requestId=" + bloodRequest.getRequestId());
            return "request-result";

        } catch (InsufficientInventoryException e) {
            model.addAttribute("shortageMessage", e.getMessage());
            try {
                requestDAO.updateStatus(bloodRequest.getRequestId(), BloodRequest.STATUS_PARTIALLY_FULFILLED);
                List<DonorMatchingService.DonorMatch> matches = matchingService.findMatchingDonors(bloodRequest);
                model.addAttribute("fulfilled", false);
                model.addAttribute("matches", matches);
            } catch (SQLException ex) {
                model.addAttribute("fulfilled", false);
                model.addAttribute("error", "Database error while searching for donors.");
            }
            return "request-result";

        } catch (SQLException e) {
            model.addAttribute("fulfilled", false);
            model.addAttribute("error", "Database error: " + e.getMessage());
            return "request-result";
        }
    }
}
