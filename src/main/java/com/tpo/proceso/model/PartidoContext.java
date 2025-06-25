package com.tpo.proceso.model;

import com.tpo.proceso.Observer.PartidoObserver;
import com.tpo.proceso.Observer.PartidoSubject;
import com.tpo.proceso.state.IEstadoPartido;
import com.tpo.proceso.state.EstadoPartidoFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class PartidoContext implements PartidoSubject {

    private Partido partido;
    private final List<PartidoObserver> observers = new CopyOnWriteArrayList<>();

    public PartidoContext() {
    }

    public PartidoContext(Partido partido) {
        this.partido = partido;
    }

    public void setPartido(Partido partido) {
        this.partido = partido;
    }

    public Partido getPartido() {
        return partido;
    }

    // === Observer ===
    @Override
    public void registerObserver(PartidoObserver o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(PartidoObserver o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (PartidoObserver o : observers) {
            o.onEstadoCambiado(partido);
        }
    }

    // === State ===
    public void cambiarEstado(IEstadoPartido nuevoEstado) {
        partido.setEstado(nuevoEstado);
        partido.setEstadoNombre(nuevoEstado.getNombreEstado());
        if ("En juego".equals(nuevoEstado.getNombreEstado())) {
            incrementarPartidosJugados();
        }
        notifyObservers();
    }

    public IEstadoPartido getEstado() {
        IEstadoPartido estado = partido.getEstado();
        if (estado == null) {
            estado = EstadoPartidoFactory.getEstado(partido.getEstadoNombre());
            partido.setEstado(estado);
        }
        return estado;
    }

    // Métodos delegados al estado
    public void agregarJugador(Usuario jugador) {
        getEstado().agregarJugador(this, jugador);
    }

    public void confirmar() {
        getEstado().confirmar(this);
    }

    public void iniciar() {
        getEstado().iniciar(this);
    }

    public void finalizar() {
        getEstado().finalizar(this);
    }

    public void cancelar() {
        getEstado().cancelar(this);
    }

    private void incrementarPartidosJugados() {
        for (Usuario u : partido.getJugadores()) {
            u.setPartidos(u.getPartidos() + 1);
        }
    }

    // Método para inicializar el estado después de cargar desde la base de datos
    public void initEstado() {
        this.partido.setEstado(EstadoPartidoFactory.getEstado(partido.getEstadoNombre()));
    }
} 