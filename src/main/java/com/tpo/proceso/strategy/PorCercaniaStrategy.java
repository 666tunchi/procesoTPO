package com.tpo.proceso.strategy;

import com.tpo.proceso.model.Partido;
import com.tpo.proceso.model.Usuario;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component("porCercania")
public class PorCercaniaStrategy implements EmparejamientoStrategy {

    @Override
    public List<Usuario> emparejar (Partido partido, List<Usuario> candidatos) {
        var origen = partido.getGeolocalizacion();
        if (origen == null) {
            throw new IllegalStateException("Partido sin geolocalización definida");
        }

        return candidatos.stream()
                .filter(u -> u.getGeolocalizacion() != null)
                .sorted(Comparator.comparingDouble(
                        u -> origen.distanciaA(u.getGeolocalizacion())
                ))
                .limit(partido.getCantidadJugadoresRequeridos())
                .collect(Collectors.toList());
    }
}
