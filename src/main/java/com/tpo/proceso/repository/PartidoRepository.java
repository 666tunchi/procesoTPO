package com.tpo.proceso.repository;

import com.tpo.proceso.model.Partido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface PartidoRepository extends JpaRepository<Partido, Integer> {

    default List<Partido> findAbiertosPorDeporte(String deporte) {
        return findByDeporte(deporte).stream()
                .filter(p -> p.getJugadores().size() < p.getCantidadJugadoresRequeridos())
                .collect(Collectors.toList());
    }

    List<Partido> findByDeporte(String deporte);
}
