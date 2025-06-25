package com.tpo.proceso.service;

import com.tpo.proceso.Observer.PartidoObserver;
import com.tpo.proceso.model.Partido;
import com.tpo.proceso.model.PartidoContext;
import com.tpo.proceso.model.Usuario;
import com.tpo.proceso.repository.PartidoRepository;
import com.tpo.proceso.repository.UserRepository;
import com.tpo.proceso.state.EstadoPartidoFactory;
import com.tpo.proceso.state.IEstadoPartido;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PartidoService {

    private final PartidoRepository partidoRepository;
    private final List<PartidoObserver> observers;
    private final UserRepository usuarioRepository;
    private final PartidoContext partidoContext;

    public PartidoService(PartidoRepository partidoRepository,
                          List<PartidoObserver> observers,
                          UserRepository usuarioRepository,
                          PartidoContext partidoContext) {
        this.partidoRepository = partidoRepository;
        this.observers = observers;
        this.usuarioRepository = usuarioRepository;
        this.partidoContext = partidoContext;
    }

    private void registerObservers(Partido partido) {
        partidoContext.setPartido(partido);
        // Primero eliminamos todos los observers para evitar registros duplicados
        observers.forEach(partidoContext::removeObserver);
        // Luego registramos todos los observers activos en Spring
        observers.forEach(partidoContext::registerObserver);
    }

    public Partido crearPartido(int cantidadJugadoresRequeridos) {
        Partido partido = new Partido();
        partido.setCantidadJugadoresRequeridos(cantidadJugadoresRequeridos);
        // El constructor de Partido ya fija fecha y estado inicial
        registerObservers(partido);
        return partidoRepository.save(partido);
    }

    public List<Partido> obtenerTodos() {
        List<Partido> partidos = partidoRepository.findAll();
        partidos.forEach(this::registerObservers);
        return partidos;
    }

    public Partido obtenerPorId(int id) {
        Partido partido = partidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Partido no encontrado con id " + id));
        registerObservers(partido);
        return partido;
    }

    public void eliminarPartido(int id) {
        partidoRepository.deleteById(id);
    }

    public Partido cambiarEstado(int id, String nombreEstado) {
        Partido partido = obtenerPorId(id);
        partidoContext.setPartido(partido);
        IEstadoPartido nuevoEstado = EstadoPartidoFactory.getEstado(nombreEstado);
        partidoContext.cambiarEstado(nuevoEstado);
        return partidoRepository.save(partido);
    }

    public Partido agregarJugador(int id, Usuario jugador) {
        Partido partido = obtenerPorId(id);
        partidoContext.setPartido(partido);
        partidoContext.agregarJugador(jugador);
        return partidoRepository.save(partido);
    }

    public Partido confirmarPartido(int id) {
        Partido partido = obtenerPorId(id);
        partidoContext.setPartido(partido);
        partidoContext.confirmar();
        return partidoRepository.save(partido);
    }

    @Transactional
    public Partido iniciarPartido(int id) {
        Partido partido = obtenerPorId(id);
        partidoContext.setPartido(partido);
        partidoContext.iniciar();
        // Guarda primero el partido (opcional, para actualizar estadoNombre)
        partidoRepository.save(partido);

        // Luego persiste los cambios en los usuarios
        usuarioRepository.saveAll(partido.getJugadores());

        return partido;
    }

    public Partido finalizarPartido(int id) {
        Partido partido = obtenerPorId(id);
        partidoContext.setPartido(partido);
        partidoContext.finalizar();
        return partidoRepository.save(partido);
    }

    public Partido cancelarPartido(int id) {
        Partido partido = obtenerPorId(id);
        partidoContext.setPartido(partido);
        partidoContext.cancelar();
        return partidoRepository.save(partido);
    }
}
