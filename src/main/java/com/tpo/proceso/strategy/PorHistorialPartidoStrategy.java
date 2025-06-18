package com.tpo.proceso.strategy;

import com.tpo.proceso.model.Partido;
import com.tpo.proceso.model.Usuario;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component("porHistorialPartido")
public class PorHistorialPartidoStrategy implements PartidoSeleccionStrategy {

    @Override
    public Optional<Partido> seleccionar(Usuario solicitante, List<Partido> partidos) {
        int countSolicitante = solicitante.getPartidos();
        return partidos.stream()
                .filter(p -> p.getJugadores().size() < p.getCantidadJugadoresRequeridos())
                .filter(p -> {
                    // promedio de historial de los participantes
                    double avg = p.getJugadores().stream()
                            .mapToInt(Usuario::getPartidos)
                            .average()
                            .orElse(0);
                    return avg >= countSolicitante - 5 && avg <= countSolicitante + 5;
                })
                .min(Comparator.comparingDouble(p ->
                        Math.abs(
                                p.getJugadores().stream()
                                        .mapToInt(Usuario::getPartidos)
                                        .average().orElse(0)
                                        - countSolicitante
                        )
                ));
    }
}
