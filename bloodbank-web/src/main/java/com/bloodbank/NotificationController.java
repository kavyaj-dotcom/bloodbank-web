package com.bloodbank.controller;

import com.bloodbank.dao.DonorDAO;
import com.bloodbank.dao.DonorDAOImpl;
import com.bloodbank.dao.NotificationLogDAO;
import com.bloodbank.dao.NotificationLogDAOImpl;
import com.bloodbank.model.Donor;
import com.bloodbank.model.NotificationLog;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class NotificationController {

    private final DonorDAO donorDAO = new DonorDAOImpl();
    private final NotificationLogDAO notificationLogDAO = new NotificationLogDAOImpl();

    @PostMapping("/notify")
    public String notifyDonors(@RequestParam int requestId,
                               @RequestParam String hospitalName,
                               @RequestParam String bloodGroup,
                               @RequestParam String emergencyLevel,
                               @RequestParam List<Integer> donorIds,
                               Model model) {

        System.out.println(">>> POST /notify received, requestId=" + requestId + ", donors=" + donorIds.size());

        String message = "URGENT: " + bloodGroup + " blood needed at " + hospitalName +
                " (Emergency level: " + emergencyLevel + "). Please respond if you are able to donate.";

        List<NotificationLog> sentLogs = new ArrayList<>();

        try {
            for (int donorId : donorIds) {
                Donor donor = donorDAO.getDonorById(donorId);
                if (donor == null) continue;

                NotificationLog log = new NotificationLog();
                log.setRequestId(requestId);
                log.setDonorId(donorId);
                log.setDonorName(donor.getName());
                log.setDonorContact(donor.getContactNumber());
                log.setMessage(message);
                log.setSentAt(LocalDateTime.now());

                notificationLogDAO.logNotification(log);
                sentLogs.add(log);
            }

            model.addAttribute("success", true);
            model.addAttribute("notifications", sentLogs);
            model.addAttribute("requestId", requestId);

        } catch (SQLException e) {
            model.addAttribute("success", false);
            model.addAttribute("error", "Could not send notifications: " + e.getMessage());
        }

        return "notification-result";
    }
}
