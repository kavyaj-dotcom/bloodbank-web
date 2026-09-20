package com.bloodbank.dao;

import com.bloodbank.exception.InsufficientInventoryException;
import com.bloodbank.model.BloodInventory;
import com.bloodbank.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BloodInventoryDAOImpl implements BloodInventoryDAO {

    @Override
    public int addStock(BloodInventory inventory) throws SQLException {
        String sql = "INSERT INTO blood_inventory " +
                "(blood_group, available_units, collection_date, expiry_date, storage_location) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, inventory.getBloodGroup());
            ps.setInt(2, inventory.getAvailableUnits());
            ps.setDate(3, Date.valueOf(inventory.getCollectionDate()));
            ps.setDate(4, Date.valueOf(inventory.getExpiryDate()));
            ps.setString(5, inventory.getStorageLocation());
            int rows = ps.executeUpdate();
            if (rows == 0) return -1;
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        }
    }

    @Override
    public List<BloodInventory> getAllStock() throws SQLException {
        String sql = "SELECT * FROM blood_inventory ORDER BY blood_group, expiry_date";
        List<BloodInventory> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    @Override
    public List<BloodInventory> getStockByBloodGroup(String bloodGroup) throws SQLException {
        String sql = "SELECT * FROM blood_inventory WHERE blood_group = ? ORDER BY expiry_date";
        List<BloodInventory> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, bloodGroup);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    @Override
    public int getTotalAvailableUnits(String bloodGroup) throws SQLException {
        String sql = "SELECT COALESCE(SUM(available_units), 0) AS total FROM blood_inventory " +
                "WHERE blood_group = ? AND expiry_date >= CURDATE()";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, bloodGroup);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt("total") : 0;
            }
        }
    }

    @Override
    public boolean updateStock(BloodInventory inventory) throws SQLException {
        String sql = "UPDATE blood_inventory SET blood_group=?, available_units=?, collection_date=?, " +
                "expiry_date=?, storage_location=? WHERE inventory_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, inventory.getBloodGroup());
            ps.setInt(2, inventory.getAvailableUnits());
            ps.setDate(3, Date.valueOf(inventory.getCollectionDate()));
            ps.setDate(4, Date.valueOf(inventory.getExpiryDate()));
            ps.setString(5, inventory.getStorageLocation());
            ps.setInt(6, inventory.getInventoryId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public void deductUnits(String bloodGroup, int unitsNeeded) throws SQLException, InsufficientInventoryException {
        String selectSql = "SELECT * FROM blood_inventory WHERE blood_group = ? AND expiry_date >= CURDATE() " +
                "AND available_units > 0 ORDER BY expiry_date FOR UPDATE";
        String updateSql = "UPDATE blood_inventory SET available_units = ? WHERE inventory_id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int remaining = unitsNeeded;
                List<int[]> plannedUpdates = new ArrayList<>();

                try (PreparedStatement selectPs = conn.prepareStatement(selectSql)) {
                    selectPs.setString(1, bloodGroup);
                    try (ResultSet rs = selectPs.executeQuery()) {
                        while (rs.next() && remaining > 0) {
                            int batchId = rs.getInt("inventory_id");
                            int batchUnits = rs.getInt("available_units");
                            int take = Math.min(batchUnits, remaining);
                            plannedUpdates.add(new int[]{batchId, batchUnits - take});
                            remaining -= take;
                        }
                    }
                }

                if (remaining > 0) {
                    conn.rollback();
                    throw new InsufficientInventoryException(
                            "Only " + (unitsNeeded - remaining) + " of " + unitsNeeded +
                                    " requested unit(s) of " + bloodGroup + " are in stock.", remaining);
                }

                try (PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
                    for (int[] update : plannedUpdates) {
                        updatePs.setInt(1, update[1]);
                        updatePs.setInt(2, update[0]);
                        updatePs.addBatch();
                    }
                    updatePs.executeBatch();
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    @Override
    public boolean deleteStock(int inventoryId) throws SQLException {
        String sql = "DELETE FROM blood_inventory WHERE inventory_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, inventoryId);
            return ps.executeUpdate() > 0;
        }
    }

    private BloodInventory mapRow(ResultSet rs) throws SQLException {
        return new BloodInventory(
                rs.getInt("inventory_id"), rs.getString("blood_group"), rs.getInt("available_units"),
                rs.getDate("collection_date").toLocalDate(), rs.getDate("expiry_date").toLocalDate(),
                rs.getString("storage_location")
        );
    }
}