// src/main/java/com/tpo/proceso/notification/EmailNotificationAdapter.java
package com.tpo.proceso.notification;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component("emailAdapter")
public class EmailNotificationAdapter implements NotificationAdapter {
    private final JavaMailSender mailSender;

    public EmailNotificationAdapter(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendNotification(String targetEmail, String title, String message, Long partyId) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(targetEmail);
        mail.setSubject(title);
        mail.setText(message);
        mailSender.send(mail);
    }
}
