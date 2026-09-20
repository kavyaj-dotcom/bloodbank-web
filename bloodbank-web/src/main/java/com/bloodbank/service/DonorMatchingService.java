package com.bloodbank.service;

import com.bloodbank.dao.DonorDAO;
import com.bloodbank.model.BloodRequest;
import com.bloodbank.model.Donor;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class DonorMatchingService {

    public static final int MIN_DAYS_BETWEEN_DONATIONS = 90;

    private static final Map<String, List<String>> COMPATIBLE_DONOR_GROUPS = buildCompatibilityChart();

    private final DonorDAO donorDAO;

    public DonorMatchingService(DonorDAO donorDAO) {
        this.donorDAO = donorDAO;
    }

    public List<DonorMatch> findMatchingDonors(BloodRequest request) throws SQLException {
        List<String> compatibleGroups = COMPATIBLE_DONOR_GROUPS.getOrDefault(
                request.getPatientBloodGroup(), Collections.emptyList());

        List<Donor> candidates = new ArrayList<>();
        for (String group : compatibleGroups) {
            candidates.addAll(donorDAO.getDonorsByBloodGroup(group));
        }

        LocalDate today = LocalDate.now();
        String hospitalCity = request.getHospitalCity();

        return candidates.stream()
                .filter(Donor::isHealthEligible)
                .filter(Donor::isCurrentlyAvailable)
                .filter(donor -> daysSinceLastDonation(donor, today) >= MIN_DAYS_BETWEEN_DONATIONS)
                .map(donor -> new DonorMatch(
                        donor,
                        daysSinceLastDonation(donor, today),
                        request.getEmergencyLevel(),
                        isSameCity(donor.getCity(), hospitalCity)))
                .sorted(Comparator.comparing(DonorMatch::isSameCity).reversed()
                        .thenComparing(Comparator.comparingLong(DonorMatch::getDaysSinceLastDonation).reversed()))
                .collect(Collectors.toList());
    }

    private long daysSinceLastDonation(Donor donor, LocalDate today) {
        if (donor.getLastDonationDate() == null) {
            return Long.MAX_VALUE;
        }
        return ChronoUnit.DAYS.between(donor.getLastDonationDate(), today);
    }

    private boolean isSameCity(String donorCity, String hospitalCity) {
        if (donorCity == null || hospitalCity == null) return false;
        return donorCity.trim().equalsIgnoreCase(hospitalCity.trim());
    }

    private static Map<String, List<String>> buildCompatibilityChart() {
        Map<String, List<String>> chart = new HashMap<>();
        chart.put("O-",  List.of("O-"));
        chart.put("O+",  List.of("O+", "O-"));
        chart.put("A-",  List.of("A-", "O-"));
        chart.put("A+",  List.of("A+", "A-", "O+", "O-"));
        chart.put("B-",  List.of("B-", "O-"));
        chart.put("B+",  List.of("B+", "B-", "O+", "O-"));
        chart.put("AB-", List.of("AB-", "A-", "B-", "O-"));
        chart.put("AB+", List.of("AB+", "AB-", "A+", "A-", "B+", "B-", "O+", "O-"));
        return chart;
    }

    public static class DonorMatch {
        private final Donor donor;
        private final long daysSinceLastDonation;
        private final String requestEmergencyLevel;
        private final boolean sameCity;

        public DonorMatch(Donor donor, long daysSinceLastDonation, String requestEmergencyLevel, boolean sameCity) {
            this.donor = donor;
            this.daysSinceLastDonation = daysSinceLastDonation;
            this.requestEmergencyLevel = requestEmergencyLevel;
            this.sameCity = sameCity;
        }

        public Donor getDonor() { return donor; }
        public long getDaysSinceLastDonation() { return daysSinceLastDonation; }
        public String getRequestEmergencyLevel() { return requestEmergencyLevel; }
        public boolean isSameCity() { return sameCity; }
    }
}