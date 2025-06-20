package com.tpo.proceso.strategy;

import com.tpo.proceso.model.Partido;
import com.tpo.proceso.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface PartidoSeleccionStrategy {

    Optional<Partido> seleccionar(Usuario solicitante, List<Partido> partidos);
}
