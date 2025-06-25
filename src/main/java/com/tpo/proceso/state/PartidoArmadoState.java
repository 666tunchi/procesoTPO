package com.tpo.proceso.state;

import com.tpo.proceso.model.PartidoContext;
import com.tpo.proceso.model.Usuario;

public class PartidoArmadoState implements IEstadoPartido {
    @Override
    public void agregarJugador(PartidoContext context, Usuario jugador) {

    }

    @Override
    public void confirmar(PartidoContext context) {

    }

    @Override
    public void iniciar(PartidoContext context) {

    }

    @Override
    public void finalizar(PartidoContext context) {

    }

    @Override
    public void cancelar(PartidoContext context) {

    }

    @Override
    public String getNombreEstado() {
        return "";
    }
}
