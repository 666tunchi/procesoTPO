package com.tpo.proceso.state;

import com.tpo.proceso.model.PartidoContext;
import com.tpo.proceso.model.Usuario;

public interface IEstadoPartido {
    void agregarJugador(PartidoContext context, Usuario jugador);
    void confirmar(PartidoContext context);
    void iniciar(PartidoContext context);
    void finalizar(PartidoContext context);
    void cancelar(PartidoContext context);
    String getNombreEstado();
}

