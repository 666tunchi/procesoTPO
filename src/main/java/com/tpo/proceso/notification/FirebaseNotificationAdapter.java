// src/main/java/com/tpo/proceso/notification/FirebaseNotificationAdapter.java
package com.tpo.proceso.notification;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import org.springframework.stereotype.Component;

@Component("firebaseAdapter")
public class FirebaseNotificationAdapter implements NotificationAdapter {

    private final FirebaseMessaging firebaseMessaging;

    public FirebaseNotificationAdapter(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }

    @Override
    public void sendNotification(String topic, String title, String message, Long partyId) {
        Message msg = Message.builder()
                .putData("title", title)
                .putData("body", message)
                .setTopic(topic)
                .build();
        try {
            firebaseMessaging.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
