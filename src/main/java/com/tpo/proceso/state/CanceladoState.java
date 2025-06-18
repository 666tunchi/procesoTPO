package com.tpo.proceso.state;

import com.tpo.proceso.model.Partido;
import com.tpo.proceso.model.Usuario;

public class CanceladoState implements IEstadoPartido{
    @Override
    public String getNombreEstado() {
        return "Cancelado";
    }

    @Override public void agregarJugador(Partido partido, Usuario jugador) {
        throw new IllegalStateException("Partido cancelado.");
    }
    @Override public void confirmar(Partido partido) {
        throw new IllegalStateException("Partido cancelado.");
    }
    @Override public void iniciar(Partido partido) {
        throw new IllegalStateException("Partido cancelado.");
    }
    @Override public void finalizar(Partido partido) {
        throw new IllegalStateException("Partido cancelado.");
    }
    @Override public void cancelar(Partido partido) {
        // no-op: ya cancelado
    }
}
