package com.bloodbank.dao;

import com.bloodbank.model.NotificationLog;
import com.bloodbank.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationLogDAOImpl implements NotificationLogDAO {

    @Override
    public int logNotification(NotificationLog log) throws SQLException {
        String sql = "INSERT INTO notification_log " +
                "(request_id, donor_id, donor_name, donor_contact, message, sent_at) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, log.getRequestId());
            ps.setInt(2, log.getDonorId());
            ps.setString(3, log.getDonorName());
            ps.setString(4, log.getDonorContact());
            ps.setString(5, log.getMessage());
            ps.setTimestamp(6, Timestamp.valueOf(log.getSentAt()));
            int rows = ps.executeUpdate();
            if (rows == 0) return -1;
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        }
    }

    @Override
    public List<NotificationLog> getNotificationsByRequestId(int requestId) throws SQLException {
        String sql = "SELECT * FROM notification_log WHERE request_id = ? ORDER BY sent_at DESC";
        List<NotificationLog> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    private NotificationLog mapRow(ResultSet rs) throws SQLException {
        NotificationLog log = new NotificationLog();
        log.setNotificationId(rs.getInt("notification_id"));
        log.setRequestId(rs.getInt("request_id"));
        log.setDonorId(rs.getInt("donor_id"));
        log.setDonorName(rs.getString("donor_name"));
        log.setDonorContact(rs.getString("donor_contact"));
        log.setMessage(rs.getString("message"));
        Timestamp ts = rs.getTimestamp("sent_at");
        log.setSentAt(ts != null ? ts.toLocalDateTime() : null);
        return log;
    }
}