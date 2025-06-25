package com.tpo.proceso.state;

import com.tpo.proceso.model.PartidoContext;
import com.tpo.proceso.model.Usuario;

public class ConfirmadoState implements IEstadoPartido{
    @Override
    public String getNombreEstado() {
        return "Confirmado";
    }

    @Override
    public void agregarJugador(PartidoContext context, Usuario jugador) {
        throw new IllegalStateException("No se pueden agregar más jugadores: ya está confirmado.");
    }

    @Override
    public void confirmar(PartidoContext context) {
        // no-op: ya está confirmado
    }

    @Override
    public void iniciar(PartidoContext context) {
        context.cambiarEstado(EstadoPartidoFactory.getEstado("En juego"));
    }

    @Override
    public void finalizar(PartidoContext context) {
        throw new IllegalStateException("No se puede finalizar: el partido no ha empezado.");
    }

    @Override
    public void cancelar(PartidoContext context) {
        context.cambiarEstado(EstadoPartidoFactory.getEstado("Cancelado"));
    }
}
