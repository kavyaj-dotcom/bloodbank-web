package com.bloodbank.exception;

public class InsufficientInventoryException extends BloodBankException {

    private final int unitsShort;

    public InsufficientInventoryException(String message, int unitsShort) {
        super(message);
        this.unitsShort = unitsShort;
    }

    public int getUnitsShort() {
        return unitsShort;
    }
}
