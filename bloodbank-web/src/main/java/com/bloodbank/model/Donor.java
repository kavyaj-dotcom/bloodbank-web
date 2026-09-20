package com.bloodbank.model;

import java.time.LocalDate;

public class Donor {

    private int donorId;
    private String name;
    private int age;
    private String gender;
    private String bloodGroup;
    private String contactNumber;
    private String city;
    private LocalDate lastDonationDate;
    private String healthStatus;
    private String availabilityStatus;

    public Donor() {
    }

    public Donor(String name, int age, String gender, String bloodGroup, String contactNumber,
                 String city, LocalDate lastDonationDate, String healthStatus, String availabilityStatus) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.contactNumber = contactNumber;
        this.city = city;
        this.lastDonationDate = lastDonationDate;
        this.healthStatus = healthStatus;
        this.availabilityStatus = availabilityStatus;
    }

    public Donor(int donorId, String name, int age, String gender, String bloodGroup, String contactNumber,
                 String city, LocalDate lastDonationDate, String healthStatus, String availabilityStatus) {
        this(name, age, gender, bloodGroup, contactNumber, city, lastDonationDate, healthStatus, availabilityStatus);
        this.donorId = donorId;
    }

    public int getDonorId() { return donorId; }
    public void setDonorId(int donorId) { this.donorId = donorId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public LocalDate getLastDonationDate() { return lastDonationDate; }
    public void setLastDonationDate(LocalDate lastDonationDate) { this.lastDonationDate = lastDonationDate; }
    public String getHealthStatus() { return healthStatus; }
    public void setHealthStatus(String healthStatus) { this.healthStatus = healthStatus; }
    public String getAvailabilityStatus() { return availabilityStatus; }
    public void setAvailabilityStatus(String availabilityStatus) { this.availabilityStatus = availabilityStatus; }

    public boolean isHealthEligible() { return "Eligible".equalsIgnoreCase(healthStatus); }
    public boolean isCurrentlyAvailable() { return "Available".equalsIgnoreCase(availabilityStatus); }

    @Override
    public String toString() {
        return "Donor{" + "donorId=" + donorId + ", name='" + name + '\'' + ", bloodGroup='" + bloodGroup + '\'' +
                ", city='" + city + '\'' + ", lastDonationDate=" + lastDonationDate +
                ", healthStatus='" + healthStatus + '\'' + ", availabilityStatus='" + availabilityStatus + '\'' + '}';
    }
}
