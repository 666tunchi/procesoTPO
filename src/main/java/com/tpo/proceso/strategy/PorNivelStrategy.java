// src/main/java/com/tpo/proceso/strategy/EmparejamientoPorNivel.java
package com.tpo.proceso.strategy;

import com.tpo.proceso.model.Nivel;
import com.tpo.proceso.model.Partido;
import com.tpo.proceso.model.Usuario;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component("porNivel")
public class PorNivelStrategy implements EmparejamientoStrategy {

    @Override
    public List<Usuario> emparejar(Partido partido, List<Usuario> candidatos) {
        // 1) Jugadores ya asignados
        List<Usuario> asignados = new ArrayList<>(partido.getJugadores());

        // 2) Cuántos faltan
        int faltan = partido.getCantidadJugadoresRequeridos() - asignados.size();
        if (faltan <= 0) {
            return asignados;
        }

        // 3) Filtrar por nivel dentro del rango [min, max]
        Nivel min = partido.getNivelMinimo();
        Nivel max = partido.getNivelMaximo();

        List<Usuario> recomendados = candidatos.stream()
                // evitamos duplicar quienes ya están asignados
                .filter(u -> !asignados.contains(u))
                // sólo niveles dentro del rango
                .filter(u -> nivelValido(u.getNivel(), min, max))
                // limitamos al cupo faltante
                .limit(faltan)
                .collect(Collectors.toList());

        // 4) Unir asignados + recomendados
        asignados.addAll(recomendados);
        return asignados;
    }

    /** Comprueba que nivel esté entre min y max (inclusive). */
    private boolean nivelValido(Nivel nivel, Nivel min, Nivel max) {
        return nivel.ordinal() >= min.ordinal()
                && nivel.ordinal() <= max.ordinal();
    }
}
