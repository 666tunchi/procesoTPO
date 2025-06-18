package com.tpo.proceso.service;

import com.tpo.proceso.model.Partido;
import com.tpo.proceso.model.Usuario;
import com.tpo.proceso.repository.PartidoRepository;
import com.tpo.proceso.repository.UserRepository;
import com.tpo.proceso.strategy.EmparejamientoFactory;
import com.tpo.proceso.strategy.EmparejamientoStrategy;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmparejamientoService {

    private final EmparejamientoFactory factory;
    private final UserRepository usuarioRepo;
    private final PartidoRepository partidoRepository;


    @Autowired
    public EmparejamientoService(EmparejamientoFactory factory,
                                 UserRepository usuarioRepo,
                                 PartidoRepository partidoRepository) {
        this.factory = factory;
        this.usuarioRepo = usuarioRepo;
        this.partidoRepository = partidoRepository;
    }

    @Transactional
    public List<Usuario> emparejarParaPartido(int partidoId, String criterio) {
        // 1) Cargo el partido
        Partido partido = partidoRepository.findById(partidoId)
                .orElseThrow(() -> new EntityNotFoundException("Partido no encontrado: " + partidoId));

        // 2) Fetch de candidatos
        List<Usuario> candidatos = usuarioRepo.findByDeporte(partido.getDeporte());

        // 3) Aplico la estrategia y obtengo la lista final
        EmparejamientoStrategy strategy = factory.getStrategy(criterio);
        List<Usuario> seleccionados = strategy.emparejar(partido, candidatos);

        // 4) Asigno esos usuarios al partido y persisto
        partido.getJugadores().clear();
        partido.getJugadores().addAll(seleccionados);
        partidoRepository.save(partido);

        return seleccionados;
    }
}
