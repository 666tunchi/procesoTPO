package com.tpo.proceso.state;

import com.tpo.proceso.model.Partido;
import com.tpo.proceso.model.Usuario;

public interface IEstadoPartido {
    void agregarJugador(Partido partido, Usuario jugador);
    void confirmar(Partido partido);
    void iniciar(Partido partido);
    void finalizar(Partido partido);
    void cancelar(Partido partido);
    String getNombreEstado();
}

