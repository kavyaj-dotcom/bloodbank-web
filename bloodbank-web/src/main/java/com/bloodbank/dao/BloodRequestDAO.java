package com.bloodbank.dao;

import com.bloodbank.model.BloodRequest;

import java.sql.SQLException;
import java.util.List;

public interface BloodRequestDAO {
    int addRequest(BloodRequest request) throws SQLException;
    BloodRequest getRequestById(int requestId) throws SQLException;
    List<BloodRequest> getAllRequests() throws SQLException;
    List<BloodRequest> getRequestsByStatus(String status) throws SQLException;
    boolean updateStatus(int requestId, String newStatus) throws SQLException;
    boolean deleteRequest(int requestId) throws SQLException;
}
