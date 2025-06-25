package com.tpo.proceso.state;

import com.tpo.proceso.model.PartidoContext;
import com.tpo.proceso.model.Usuario;

public class CanceladoState implements IEstadoPartido{
    @Override
    public String getNombreEstado() {
        return "Cancelado";
    }

    @Override public void agregarJugador(PartidoContext context, Usuario jugador) {
        throw new IllegalStateException("Partido cancelado.");
    }
    @Override public void confirmar(PartidoContext context) {
        throw new IllegalStateException("Partido cancelado.");
    }
    @Override public void iniciar(PartidoContext context) {
        throw new IllegalStateException("Partido cancelado.");
    }
    @Override public void finalizar(PartidoContext context) {
        throw new IllegalStateException("Partido cancelado.");
    }
    @Override public void cancelar(PartidoContext context) {
        // no-op: ya cancelado
    }
}
