package com.tpo.proceso.state;

import com.tpo.proceso.model.Partido;
import com.tpo.proceso.model.Usuario;

public class PartidoArmadoState implements IEstadoPartido {
    @Override
    public void agregarJugador(Partido partido, Usuario jugador) {

    }

    @Override
    public void confirmar(Partido partido) {

    }

    @Override
    public void iniciar(Partido partido) {

    }

    @Override
    public void finalizar(Partido partido) {

    }

    @Override
    public void cancelar(Partido partido) {

    }

    @Override
    public String getNombreEstado() {
        return "";
    }
}
