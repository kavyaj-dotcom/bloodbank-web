package com.bloodbank.model;

import java.time.LocalDate;

public class BloodInventory {

    private int inventoryId;
    private String bloodGroup;
    private int availableUnits;
    private LocalDate collectionDate;
    private LocalDate expiryDate;
    private String storageLocation;

    public BloodInventory() {
    }

    public BloodInventory(String bloodGroup, int availableUnits, LocalDate collectionDate,
                           LocalDate expiryDate, String storageLocation) {
        this.bloodGroup = bloodGroup;
        this.availableUnits = availableUnits;
        this.collectionDate = collectionDate;
        this.expiryDate = expiryDate;
        this.storageLocation = storageLocation;
    }

    public BloodInventory(int inventoryId, String bloodGroup, int availableUnits, LocalDate collectionDate,
                           LocalDate expiryDate, String storageLocation) {
        this(bloodGroup, availableUnits, collectionDate, expiryDate, storageLocation);
        this.inventoryId = inventoryId;
    }

    public int getInventoryId() { return inventoryId; }
    public void setInventoryId(int inventoryId) { this.inventoryId = inventoryId; }
    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
    public int getAvailableUnits() { return availableUnits; }
    public void setAvailableUnits(int availableUnits) { this.availableUnits = availableUnits; }
    public LocalDate getCollectionDate() { return collectionDate; }
    public void setCollectionDate(LocalDate collectionDate) { this.collectionDate = collectionDate; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    public String getStorageLocation() { return storageLocation; }
    public void setStorageLocation(String storageLocation) { this.storageLocation = storageLocation; }

    public boolean isExpired() { return expiryDate != null && expiryDate.isBefore(LocalDate.now()); }

    @Override
    public String toString() {
        return "BloodInventory{" + "inventoryId=" + inventoryId + ", bloodGroup='" + bloodGroup + '\'' +
                ", availableUnits=" + availableUnits + ", expiryDate=" + expiryDate +
                ", storageLocation='" + storageLocation + '\'' + '}';
    }
}
