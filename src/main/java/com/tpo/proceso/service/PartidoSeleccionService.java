package com.tpo.proceso.service;

import com.tpo.proceso.model.Partido;
import com.tpo.proceso.model.Usuario;
import com.tpo.proceso.repository.PartidoRepository;
import com.tpo.proceso.repository.UserRepository;
import com.tpo.proceso.strategy.PartidoSeleccionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PartidoSeleccionService {

    private final Map<String, PartidoSeleccionStrategy> strategies;
    private final PartidoRepository partidoRepo;
    private final UserRepository usuarioRepo;

    @Autowired
    public PartidoSeleccionService(
            Map<String, PartidoSeleccionStrategy> strategies,
            PartidoRepository partidoRepo,
            UserRepository usuarioRepo
    ) {
        this.strategies   = strategies;
        this.partidoRepo  = partidoRepo;
        this.usuarioRepo  = usuarioRepo;
    }

    /**
     * Devuelve el mejor Partido “abierto” al que el usuario puede unirse,
     * según el criterio (“porCercaniaPartido”, “porNivelPartido” o “porHistorialPartido”).
     */
    public Optional<Partido> buscar(String criterio, Usuario solicitante) {
        List<Partido> abiertos =
                partidoRepo.findAbiertosPorDeporte(solicitante.getDeporte());
        return Optional.ofNullable(strategies.get(criterio))
                .flatMap(strat -> strat.seleccionar(solicitante, abiertos));
    }

    /**
     * Permite al usuario unirse a un partido concreto.
     */
    @Transactional
    public Partido unirseAPartido(int partidoId, Integer usuarioId) {
        Usuario u = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no existe"));
        Partido p = partidoRepo.findById(partidoId)
                .orElseThrow(() -> new IllegalArgumentException("Partido no existe"));

        p.agregarJugador(u);
        partidoRepo.save(p);
        return p;
    }
}
