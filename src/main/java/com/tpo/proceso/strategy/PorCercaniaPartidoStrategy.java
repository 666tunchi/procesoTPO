package com.tpo.proceso.strategy;

import com.tpo.proceso.model.Geolocalizacion;
import com.tpo.proceso.model.Partido;
import com.tpo.proceso.model.Usuario;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component("porCercaniaPartido")
public class PorCercaniaPartidoStrategy implements PartidoSeleccionStrategy {

    @Override
    public Optional<Partido> seleccionar(Usuario solicitante, List<Partido> partidos) {
        Geolocalizacion origen = solicitante.getGeolocalizacion();
        return partidos.stream()
                .filter(p -> p.getJugadores().size() < p.getCantidadJugadoresRequeridos())
                .min(Comparator.comparingDouble(p ->
                        origen.distanciaA(p.getGeolocalizacion())
                ));
    }
}
