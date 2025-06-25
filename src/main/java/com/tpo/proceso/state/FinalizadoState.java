package com.tpo.proceso.state;

import com.tpo.proceso.model.PartidoContext;
import com.tpo.proceso.model.Usuario;

public class FinalizadoState implements IEstadoPartido{
    @Override
    public String getNombreEstado() {
        return "Finalizado";
    }

    @Override public void agregarJugador(PartidoContext context, Usuario jugador) {
        throw new IllegalStateException("Partido finalizado: no se pueden agregar jugadores.");
    }
    @Override public void confirmar(PartidoContext context) {
        throw new IllegalStateException("Partido finalizado.");
    }
    @Override public void iniciar(PartidoContext context) {
        throw new IllegalStateException("Partido finalizado.");
    }
    @Override public void finalizar(PartidoContext context) {
        // no-op: ya finalizado
    }
    @Override public void cancelar(PartidoContext context) {
        throw new IllegalStateException("Partido finalizado.");
    }
}
