package com.tpo.proceso.Observer;

import com.tpo.proceso.model.Partido;

public interface PartidoObserver {
    void onEstadoCambiado(Partido partido);
}

