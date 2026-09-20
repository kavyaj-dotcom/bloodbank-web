package com.bloodbank.dao;

import com.bloodbank.exception.InsufficientInventoryException;
import com.bloodbank.model.BloodInventory;

import java.sql.SQLException;
import java.util.List;

public interface BloodInventoryDAO {
    int addStock(BloodInventory inventory) throws SQLException;
    List<BloodInventory> getAllStock() throws SQLException;
    List<BloodInventory> getStockByBloodGroup(String bloodGroup) throws SQLException;
    int getTotalAvailableUnits(String bloodGroup) throws SQLException;
    boolean updateStock(BloodInventory inventory) throws SQLException;
    void deductUnits(String bloodGroup, int units) throws SQLException, InsufficientInventoryException;
    boolean deleteStock(int inventoryId) throws SQLException;
}
