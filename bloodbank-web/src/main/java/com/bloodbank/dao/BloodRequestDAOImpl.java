package com.bloodbank.dao;

import com.bloodbank.model.BloodRequest;
import com.bloodbank.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BloodRequestDAOImpl implements BloodRequestDAO {

    @Override
    public int addRequest(BloodRequest request) throws SQLException {
        String sql = "INSERT INTO blood_request " +
                "(hospital_name, hospital_city, patient_blood_group, units_required, emergency_level, request_date, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, request.getHospitalName());
            ps.setString(2, request.getHospitalCity());
            ps.setString(3, request.getPatientBloodGroup());
            ps.setInt(4, request.getUnitsRequired());
            ps.setString(5, request.getEmergencyLevel());
            ps.setDate(6, Date.valueOf(request.getRequestDate()));
            ps.setString(7, request.getStatus());
            int rows = ps.executeUpdate();
            if (rows == 0) return -1;
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        }
    }

    @Override
    public BloodRequest getRequestById(int requestId) throws SQLException {
        String sql = "SELECT * FROM blood_request WHERE request_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    @Override
    public List<BloodRequest> getAllRequests() throws SQLException {
        String sql = "SELECT * FROM blood_request ORDER BY request_date DESC, request_id DESC";
        List<BloodRequest> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    @Override
    public List<BloodRequest> getRequestsByStatus(String status) throws SQLException {
        String sql = "SELECT * FROM blood_request WHERE status = ? ORDER BY " +
                "FIELD(emergency_level, 'Critical', 'High', 'Normal'), request_date";
        List<BloodRequest> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    @Override
    public boolean updateStatus(int requestId, String newStatus) throws SQLException {
        String sql = "UPDATE blood_request SET status = ? WHERE request_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, requestId);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deleteRequest(int requestId) throws SQLException {
        String sql = "DELETE FROM blood_request WHERE request_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            return ps.executeUpdate() > 0;
        }
    }

    private BloodRequest mapRow(ResultSet rs) throws SQLException {
        return new BloodRequest(
                rs.getInt("request_id"), rs.getString("hospital_name"), rs.getString("hospital_city"),
                rs.getString("patient_blood_group"), rs.getInt("units_required"), rs.getString("emergency_level"),
                rs.getDate("request_date").toLocalDate(), rs.getString("status")
        );
    }
}