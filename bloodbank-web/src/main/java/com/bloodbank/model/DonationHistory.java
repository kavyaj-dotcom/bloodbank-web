package com.bloodbank.model;

import java.time.LocalDate;

public class DonationHistory {

    private int donationId;
    private int donorId;
    private LocalDate donationDate;
    private String bloodGroup;
    private double quantityDonated;

    public DonationHistory() {
    }

    public DonationHistory(int donorId, LocalDate donationDate, String bloodGroup, double quantityDonated) {
        this.donorId = donorId;
        this.donationDate = donationDate;
        this.bloodGroup = bloodGroup;
        this.quantityDonated = quantityDonated;
    }

    public DonationHistory(int donationId, int donorId, LocalDate donationDate, String bloodGroup, double quantityDonated) {
        this(donorId, donationDate, bloodGroup, quantityDonated);
        this.donationId = donationId;
    }

    public int getDonationId() { return donationId; }
    public void setDonationId(int donationId) { this.donationId = donationId; }
    public int getDonorId() { return donorId; }
    public void setDonorId(int donorId) { this.donorId = donorId; }
    public LocalDate getDonationDate() { return donationDate; }
    public void setDonationDate(LocalDate donationDate) { this.donationDate = donationDate; }
    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
    public double getQuantityDonated() { return quantityDonated; }
    public void setQuantityDonated(double quantityDonated) { this.quantityDonated = quantityDonated; }

    @Override
    public String toString() {
        return "DonationHistory{" + "donationId=" + donationId + ", donorId=" + donorId +
                ", donationDate=" + donationDate + ", bloodGroup='" + bloodGroup + '\'' +
                ", quantityDonated=" + quantityDonated + '}';
    }
}
