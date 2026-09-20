package com.bloodbank.dao;

import com.bloodbank.model.Donor;
import com.bloodbank.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DonorDAOImpl implements DonorDAO {

    @Override
    public int addDonor(Donor donor) throws SQLException {
        String sql = "INSERT INTO donor " +
                "(name, age, gender, blood_group, contact_number, city, last_donation_date, health_status, availability_status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, donor.getName());
            ps.setInt(2, donor.getAge());
            ps.setString(3, donor.getGender());
            ps.setString(4, donor.getBloodGroup());
            ps.setString(5, donor.getContactNumber());
            ps.setString(6, donor.getCity());
            ps.setDate(7, donor.getLastDonationDate() != null ? Date.valueOf(donor.getLastDonationDate()) : null);
            ps.setString(8, donor.getHealthStatus());
            ps.setString(9, donor.getAvailabilityStatus());

            int rows = ps.executeUpdate();
            if (rows == 0) return -1;
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        }
    }

    @Override
    public Donor getDonorById(int donorId) throws SQLException {
        String sql = "SELECT * FROM donor WHERE donor_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, donorId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    @Override
    public List<Donor> getAllDonors() throws SQLException {
        String sql = "SELECT * FROM donor ORDER BY donor_id";
        List<Donor> donors = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) donors.add(mapRow(rs));
        }
        return donors;
    }

    @Override
    public List<Donor> getDonorsByBloodGroup(String bloodGroup) throws SQLException {
        String sql = "SELECT * FROM donor WHERE blood_group = ?";
        List<Donor> donors = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, bloodGroup);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) donors.add(mapRow(rs));
            }
        }
        return donors;
    }

    @Override
    public boolean updateDonor(Donor donor) throws SQLException {
        String sql = "UPDATE donor SET name=?, age=?, gender=?, blood_group=?, contact_number=?, " +
                "city=?, last_donation_date=?, health_status=?, availability_status=? WHERE donor_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, donor.getName());
            ps.setInt(2, donor.getAge());
            ps.setString(3, donor.getGender());
            ps.setString(4, donor.getBloodGroup());
            ps.setString(5, donor.getContactNumber());
            ps.setString(6, donor.getCity());
            ps.setDate(7, donor.getLastDonationDate() != null ? Date.valueOf(donor.getLastDonationDate()) : null);
            ps.setString(8, donor.getHealthStatus());
            ps.setString(9, donor.getAvailabilityStatus());
            ps.setInt(10, donor.getDonorId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deleteDonor(int donorId) throws SQLException {
        String sql = "DELETE FROM donor WHERE donor_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, donorId);
            return ps.executeUpdate() > 0;
        }
    }

    private Donor mapRow(ResultSet rs) throws SQLException {
        Date lastDonation = rs.getDate("last_donation_date");
        return new Donor(
                rs.getInt("donor_id"), rs.getString("name"), rs.getInt("age"), rs.getString("gender"),
                rs.getString("blood_group"), rs.getString("contact_number"), rs.getString("city"),
                lastDonation != null ? lastDonation.toLocalDate() : null,
                rs.getString("health_status"), rs.getString("availability_status")
        );
    }
}
