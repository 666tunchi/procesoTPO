package com.tpo.proceso.state;

import com.tpo.proceso.model.PartidoContext;
import com.tpo.proceso.model.Usuario;

public class EnJuegoState implements IEstadoPartido{
    @Override
    public String getNombreEstado() {
        return "En juego";
    }

    @Override
    public void agregarJugador(PartidoContext context, Usuario jugador) {
        throw new IllegalStateException("No se pueden agregar jugadores mientras el partido está en juego.");
    }

    @Override
    public void confirmar(PartidoContext context) {
        throw new IllegalStateException("El partido ya está en juego.");
    }

    @Override
    public void iniciar(PartidoContext context) {
        // no-op: ya inició
    }

    @Override
    public void finalizar(PartidoContext context) {
        context.cambiarEstado(EstadoPartidoFactory.getEstado("Finalizado"));
    }

    @Override
    public void cancelar(PartidoContext context) {
        throw new IllegalStateException("No se puede cancelar: el partido ya está en juego.");
    }
}
