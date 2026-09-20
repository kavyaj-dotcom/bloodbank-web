package com.bloodbank.model;

import java.time.LocalDate;

public class BloodRequest {

    public static final String STATUS_PENDING = "Pending";
    public static final String STATUS_FULFILLED = "Fulfilled";
    public static final String STATUS_PARTIALLY_FULFILLED = "Partially Fulfilled";

    private int requestId;
    private String hospitalName;
    private String hospitalCity;
    private String patientBloodGroup;
    private int unitsRequired;
    private String emergencyLevel;
    private LocalDate requestDate;
    private String status;

    public BloodRequest() {
    }

    public BloodRequest(String hospitalName, String hospitalCity, String patientBloodGroup, int unitsRequired,
                        String emergencyLevel, LocalDate requestDate, String status) {
        this.hospitalName = hospitalName;
        this.hospitalCity = hospitalCity;
        this.patientBloodGroup = patientBloodGroup;
        this.unitsRequired = unitsRequired;
        this.emergencyLevel = emergencyLevel;
        this.requestDate = requestDate;
        this.status = status;
    }

    public BloodRequest(int requestId, String hospitalName, String hospitalCity, String patientBloodGroup, int unitsRequired,
                        String emergencyLevel, LocalDate requestDate, String status) {
        this(hospitalName, hospitalCity, patientBloodGroup, unitsRequired, emergencyLevel, requestDate, status);
        this.requestId = requestId;
    }

    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }
    public String getHospitalName() { return hospitalName; }
    public void setHospitalName(String hospitalName) { this.hospitalName = hospitalName; }
    public String getHospitalCity() { return hospitalCity; }
    public void setHospitalCity(String hospitalCity) { this.hospitalCity = hospitalCity; }
    public String getPatientBloodGroup() { return patientBloodGroup; }
    public void setPatientBloodGroup(String patientBloodGroup) { this.patientBloodGroup = patientBloodGroup; }
    public int getUnitsRequired() { return unitsRequired; }
    public void setUnitsRequired(int unitsRequired) { this.unitsRequired = unitsRequired; }
    public String getEmergencyLevel() { return emergencyLevel; }
    public void setEmergencyLevel(String emergencyLevel) { this.emergencyLevel = emergencyLevel; }
    public LocalDate getRequestDate() { return requestDate; }
    public void setRequestDate(LocalDate requestDate) { this.requestDate = requestDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "BloodRequest{" + "requestId=" + requestId + ", hospitalName='" + hospitalName + '\'' +
                ", hospitalCity='" + hospitalCity + '\'' +
                ", patientBloodGroup='" + patientBloodGroup + '\'' + ", unitsRequired=" + unitsRequired +
                ", emergencyLevel='" + emergencyLevel + '\'' + ", status='" + status + '\'' + '}';
    }
}