package com.bloodbank.dao;

import com.bloodbank.model.NotificationLog;

import java.sql.SQLException;
import java.util.List;

public interface NotificationLogDAO {
    int logNotification(NotificationLog log) throws SQLException;
    List<NotificationLog> getNotificationsByRequestId(int requestId) throws SQLException;
}