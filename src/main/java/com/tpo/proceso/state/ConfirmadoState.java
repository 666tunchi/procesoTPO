package com.tpo.proceso.state;

import com.tpo.proceso.model.Partido;
import com.tpo.proceso.model.Usuario;

public class ConfirmadoState implements IEstadoPartido{
    @Override
    public String getNombreEstado() {
        return "Confirmado";
    }

    @Override
    public void agregarJugador(Partido partido, Usuario jugador) {
        throw new IllegalStateException("No se pueden agregar más jugadores: ya está confirmado.");
    }

    @Override
    public void confirmar(Partido partido) {
        // no-op: ya está confirmado
    }

    @Override
    public void iniciar(Partido partido) {
        partido.cambiarEstado(EstadoPartidoFactory.getEstado("En juego"));
    }

    @Override
    public void finalizar(Partido partido) {
        throw new IllegalStateException("No se puede finalizar: el partido no ha empezado.");
    }

    @Override
    public void cancelar(Partido partido) {
        partido.cambiarEstado(EstadoPartidoFactory.getEstado("Cancelado"));
    }
}
