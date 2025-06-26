package com.tpo.proceso.notification;

public interface NotificationAdapter {

    void sendNotification(String target, String title, String message, Long partyId);
}
