package com.tpo.proceso.state;

import com.tpo.proceso.model.Partido;
import com.tpo.proceso.model.Usuario;

public class FinalizadoState implements IEstadoPartido{
    @Override
    public String getNombreEstado() {
        return "Finalizado";
    }

    @Override public void agregarJugador(Partido partido, Usuario jugador) {
        throw new IllegalStateException("Partido finalizado: no se pueden agregar jugadores.");
    }
    @Override public void confirmar(Partido partido) {
        throw new IllegalStateException("Partido finalizado.");
    }
    @Override public void iniciar(Partido partido) {
        throw new IllegalStateException("Partido finalizado.");
    }
    @Override public void finalizar(Partido partido) {
        // no-op: ya finalizado
    }
    @Override public void cancelar(Partido partido) {
        throw new IllegalStateException("Partido finalizado.");
    }
}
