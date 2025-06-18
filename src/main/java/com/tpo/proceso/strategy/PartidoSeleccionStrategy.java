package com.tpo.proceso.strategy;

import com.tpo.proceso.model.Partido;
import com.tpo.proceso.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface PartidoSeleccionStrategy {
    /**
     * Dado un solicitante y una lista de partidos “abiertos” (con cupos),
     * devuelve el mejor partido según este criterio, o empty() si no hay ninguno.
     */
    Optional<Partido> seleccionar(Usuario solicitante, List<Partido> partidos);
}
