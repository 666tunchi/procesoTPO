package com.tpo.proceso.state;

import com.tpo.proceso.model.Partido;
import com.tpo.proceso.model.Usuario;

public class EnJuegoState implements IEstadoPartido{
    @Override
    public String getNombreEstado() {
        return "En juego";
    }

    @Override
    public void agregarJugador(Partido partido, Usuario jugador) {
        throw new IllegalStateException("No se pueden agregar jugadores mientras el partido está en juego.");
    }

    @Override
    public void confirmar(Partido partido) {
        throw new IllegalStateException("El partido ya está en juego.");
    }

    @Override
    public void iniciar(Partido partido) {
        // no-op: ya inició
    }

    @Override
    public void finalizar(Partido partido) {
        partido.cambiarEstado(EstadoPartidoFactory.getEstado("Finalizado"));
    }

    @Override
    public void cancelar(Partido partido) {
        throw new IllegalStateException("No se puede cancelar: el partido ya está en juego.");
    }
}
