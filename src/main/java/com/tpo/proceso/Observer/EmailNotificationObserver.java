package com.tpo.proceso.Observer;

import com.tpo.proceso.model.Partido;
import com.tpo.proceso.notification.NotificationAdapter;
import com.tpo.proceso.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmailNotificationObserver implements PartidoObserver {

    private final NotificationAdapter emailAdapter;
    private final UserRepository usuarioRepo;

    public EmailNotificationObserver(@Qualifier("emailAdapter") NotificationAdapter emailAdapter,
                                     UserRepository usuarioRepo) {
        this.emailAdapter   = emailAdapter;
        this.usuarioRepo    = usuarioRepo;
    }

    @Override
    public void onEstadoCambiado(Partido partido) {
        String estado = partido.getEstadoNombre();
        String title  = "Partido #" + partido.getId() + " → " + estado;
        
        // Obtener coordenadas del partido
        double latitud = partido.getGeolocalizacion().getLatitud();
        double longitud = partido.getGeolocalizacion().getLongitud();
        String googleMapsLink = "https://www.google.com/maps/place/" + latitud + "," + longitud;
        
        String body   = "El partido de " + partido.getDeporte() +
                " pasó a estado: " + estado + "\n\n" +
                "Ubicación: " + googleMapsLink;

        List<String> targets;
        if ("Necesitamos jugadores".equals(estado)) {
            //  Notificar a TODOS los fans de este deporte
            targets = usuarioRepo.findByDeporte(partido.getDeporte())
                    .stream()
                    .map(u -> u.getMail())
                    .collect(Collectors.toList());
        } else {
            //Notificar sólo a los INSCRITOS
            targets = partido.getJugadores().stream()
                    .map(u -> u.getMail())
                    .collect(Collectors.toList());
        }

        for (String mail : targets) {
            emailAdapter.sendNotification(mail, title, body, partido.getId());
        }
    }
}
