package com.bloodbank.dao;

import com.bloodbank.model.DonationHistory;

import java.sql.SQLException;
import java.util.List;

public interface DonationHistoryDAO {
    int addRecord(DonationHistory record) throws SQLException;
    List<DonationHistory> getHistoryForDonor(int donorId) throws SQLException;
    List<DonationHistory> getAllHistory() throws SQLException;
}
