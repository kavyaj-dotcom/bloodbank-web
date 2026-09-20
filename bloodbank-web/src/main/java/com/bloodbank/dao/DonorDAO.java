package com.bloodbank.dao;

import com.bloodbank.model.Donor;

import java.sql.SQLException;
import java.util.List;

public interface DonorDAO {
    int addDonor(Donor donor) throws SQLException;
    Donor getDonorById(int donorId) throws SQLException;
    List<Donor> getAllDonors() throws SQLException;
    List<Donor> getDonorsByBloodGroup(String bloodGroup) throws SQLException;
    boolean updateDonor(Donor donor) throws SQLException;
    boolean deleteDonor(int donorId) throws SQLException;
}
