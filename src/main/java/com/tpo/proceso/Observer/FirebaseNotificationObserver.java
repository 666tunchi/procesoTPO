package com.tpo.proceso.Observer;

import com.tpo.proceso.model.Partido;
import com.tpo.proceso.notification.NotificationAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class FirebaseNotificationObserver implements PartidoObserver {

    private final NotificationAdapter firebaseAdapter;

    public FirebaseNotificationObserver(@Qualifier("firebaseAdapter") NotificationAdapter firebaseAdapter) {
        this.firebaseAdapter = firebaseAdapter;
    }

    @Override
    public void onEstadoCambiado(Partido partido) {
        String estado = partido.getEstadoNombre();
        String title  = "Partido #" + partido.getId() + " → " + estado;
        String body   = "El partido de " + partido.getDeporte() +
                " pasó a estado: " + estado;

        if ("Necesitamos jugadores".equals(estado)) {
            // i) Topic por deporte - limpiar el nombre del deporte
            String deporteLimpio = partido.getDeporte()
                    .toLowerCase()
                    .replaceAll("[^a-zA-Z0-9]", "_")
                    .replaceAll("_+", "_")
                    .replaceAll("^_|_$", "");
            String topic = "deporte_" + deporteLimpio;
            firebaseAdapter.sendNotification(topic, title, body, partido.getId());
        } else {
            // ii–iv) Topic por partido
            String topic = "partido_" + partido.getId();
            firebaseAdapter.sendNotification(topic, title, body, partido.getId());
        }
    }
}
