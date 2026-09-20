package com.bloodbank.dao;

import com.bloodbank.model.DonationHistory;
import com.bloodbank.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DonationHistoryDAOImpl implements DonationHistoryDAO {

    @Override
    public int addRecord(DonationHistory record) throws SQLException {
        String sql = "INSERT INTO donation_history (donor_id, donation_date, blood_group, quantity_donated) " +
                "VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, record.getDonorId());
            ps.setDate(2, Date.valueOf(record.getDonationDate()));
            ps.setString(3, record.getBloodGroup());
            ps.setDouble(4, record.getQuantityDonated());
            int rows = ps.executeUpdate();
            if (rows == 0) return -1;
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        }
    }

    @Override
    public List<DonationHistory> getHistoryForDonor(int donorId) throws SQLException {
        String sql = "SELECT * FROM donation_history WHERE donor_id = ? ORDER BY donation_date DESC";
        List<DonationHistory> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, donorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    @Override
    public List<DonationHistory> getAllHistory() throws SQLException {
        String sql = "SELECT * FROM donation_history ORDER BY donation_date DESC";
        List<DonationHistory> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    private DonationHistory mapRow(ResultSet rs) throws SQLException {
        return new DonationHistory(
                rs.getInt("donation_id"), rs.getInt("donor_id"), rs.getDate("donation_date").toLocalDate(),
                rs.getString("blood_group"), rs.getDouble("quantity_donated")
        );
    }
}
