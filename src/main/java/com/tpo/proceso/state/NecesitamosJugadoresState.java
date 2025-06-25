package com.tpo.proceso.state;

import com.tpo.proceso.model.PartidoContext;
import com.tpo.proceso.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class NecesitamosJugadoresState implements IEstadoPartido {
    @Override
    public String getNombreEstado() {
        return "Necesitamos jugadores";
    }

    @Override
    public void agregarJugador(PartidoContext context, Usuario jugador) {
        context.getPartido().getJugadores().add(jugador);
        if (context.getPartido().getJugadores().size() >= context.getPartido().getCantidadJugadoresRequeridos()) {
            context.cambiarEstado(EstadoPartidoFactory.getEstado("Confirmado"));
        }
    }

    @Override
    public void confirmar(PartidoContext context) {
        if (context.getPartido().getJugadores().size() >= context.getPartido().getCantidadJugadoresRequeridos()) {
            context.cambiarEstado(EstadoPartidoFactory.getEstado("Confirmado"));
        } else {
            throw new IllegalStateException("No hay suficientes jugadores para confirmar.");
        }
    }

    @Override
    public void iniciar(PartidoContext context) {
        throw new IllegalStateException("No se puede iniciar: aún faltan jugadores.");
    }

    @Override
    public void finalizar(PartidoContext context) {
        throw new IllegalStateException("No se puede finalizar: el partido no ha comenzado.");
    }

    @Override
    public void cancelar(PartidoContext context) {
        context.cambiarEstado(EstadoPartidoFactory.getEstado("Cancelado"));
    }
}