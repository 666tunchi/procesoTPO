package com.tpo.proceso.state;

import com.tpo.proceso.model.Partido;
import com.tpo.proceso.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class NecesitamosJugadoresState implements IEstadoPartido {
    @Override
    public String getNombreEstado() {
        return "Necesitamos jugadores";
    }

    @Override
    public void agregarJugador(Partido partido, Usuario jugador) {
        partido.getJugadores().add(jugador);
        if (partido.getJugadores().size() >= partido.getCantidadJugadoresRequeridos()) {
            partido.cambiarEstado(EstadoPartidoFactory.getEstado("Confirmado"));
        }
    }

    @Override
    public void confirmar(Partido partido) {
        if (partido.getJugadores().size() >= partido.getCantidadJugadoresRequeridos()) {
            partido.cambiarEstado(EstadoPartidoFactory.getEstado("Confirmado"));
        } else {
            throw new IllegalStateException("No hay suficientes jugadores para confirmar.");
        }
    }

    @Override
    public void iniciar(Partido partido) {
        throw new IllegalStateException("No se puede iniciar: aún faltan jugadores.");
    }

    @Override
    public void finalizar(Partido partido) {
        throw new IllegalStateException("No se puede finalizar: el partido no ha comenzado.");
    }

    @Override
    public void cancelar(Partido partido) {
        partido.cambiarEstado(EstadoPartidoFactory.getEstado("Cancelado"));
    }
}