package com.bloodbank.model;

import java.time.LocalDateTime;

public class NotificationLog {

    private int notificationId;
    private int requestId;
    private int donorId;
    private String donorName;
    private String donorContact;
    private String message;
    private LocalDateTime sentAt;

    public NotificationLog() {
    }

    public int getNotificationId() { return notificationId; }
    public void setNotificationId(int notificationId) { this.notificationId = notificationId; }
    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }
    public int getDonorId() { return donorId; }
    public void setDonorId(int donorId) { this.donorId = donorId; }
    public String getDonorName() { return donorName; }
    public void setDonorName(String donorName) { this.donorName = donorName; }
    public String getDonorContact() { return donorContact; }
    public void setDonorContact(String donorContact) { this.donorContact = donorContact; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }
}
