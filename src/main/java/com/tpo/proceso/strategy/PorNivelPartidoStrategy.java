package com.tpo.proceso.strategy;

import com.tpo.proceso.model.Nivel;
import com.tpo.proceso.model.Partido;
import com.tpo.proceso.model.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component("porNivelPartido")
public class PorNivelPartidoStrategy implements PartidoSeleccionStrategy {

    @Override
    public Optional<Partido> seleccionar(Usuario solicitante, List<Partido> partidos) {
        Nivel nivel = solicitante.getNivel();
        return partidos.stream()
                .filter(p -> {
                    boolean hayCupo = p.getJugadores().size() < p.getCantidadJugadoresRequeridos();
                    boolean enRango = nivel.ordinal() >= p.getNivelMinimo().ordinal()
                            && nivel.ordinal() <= p.getNivelMaximo().ordinal();
                    return hayCupo && enRango;
                })
                .findFirst();  // o puedes usar min(…) con algún criterio secundario
    }
}
