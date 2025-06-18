package com.tpo.proceso.strategy;

import com.tpo.proceso.model.Partido;
import com.tpo.proceso.model.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component("historial")
public class PorHistorialStrategy implements EmparejamientoStrategy {

    @Override
    public List<Usuario> emparejar(Partido partido, List<Usuario> candidatos) {
        // 1) Usuario que hace la petición
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario solicitante = (Usuario) auth.getPrincipal();
        int countSolicitante = partidosJugados(solicitante);

        // 2) Jugadores ya asignados
        List<Usuario> asignados = new ArrayList<>(partido.getJugadores());

        // 3) Cuántos faltan para completar el partido
        int faltan = partido.getCantidadJugadoresRequeridos() - asignados.size();
        if (faltan <= 0) {
            return asignados;
        }

        // 4) Filtrar y ordenar candidatos por proximidad de partidos jugados
        List<Usuario> recomendados = candidatos.stream()
                .filter(u -> !asignados.contains(u))  // no duplicar
                .filter(u -> {
                    int pj = partidosJugados(u);
                    return pj >= countSolicitante - 5 && pj <= countSolicitante + 5;
                })
                .sorted(Comparator.comparingInt(u ->
                        Math.abs(partidosJugados(u) - countSolicitante)
                ))
                .limit(faltan)
                .collect(Collectors.toList());

        // 5) Unión de asignados + recomendados
        asignados.addAll(recomendados);
        return asignados;
    }

    /**
     * Método para obtener la cantidad de partidos previos de un usuario.
     * Debes implementarlo según tu modelo (podría venir de una relación
     * Usuario→PartidosJugados o de un repositorio).
     */
    private int partidosJugados(Usuario u) {
        // Ejemplo: si Usuario tiene un Set<Partido> historial:
        return u.getPartidos();
    }
}
