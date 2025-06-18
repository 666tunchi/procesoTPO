// src/main/java/com/tpo/proceso/notification/NotificationAdapter.java
package com.tpo.proceso.notification;

public interface NotificationAdapter {
    /**
     * Envía una notificación.
     * @param target  Para e-mail será la dirección; para Firebase, el topic (p.ej. "deporte_FUTBOL" o "partido_1")
     * @param title   Título de la notificación
     * @param message Cuerpo de la notificación
     * @param partyId ID del partido (puedes usarlo internamente si quieres)
     */
    void sendNotification(String target, String title, String message, Long partyId);
}
