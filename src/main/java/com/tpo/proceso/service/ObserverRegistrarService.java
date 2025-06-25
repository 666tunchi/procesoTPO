package com.tpo.proceso.service;

import com.tpo.proceso.Observer.PartidoObserver;
import com.tpo.proceso.model.Partido;
import com.tpo.proceso.model.PartidoContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObserverRegistrarService {

    private final List<PartidoObserver> observers;
    private final PartidoContext partidoContext;

    @Autowired
    public ObserverRegistrarService(List<PartidoObserver> observers, PartidoContext partidoContext) {
        this.observers = observers;
        this.partidoContext = partidoContext;
    }

    /**
     * Registra en el partido todos los observers disponibles.
     */
    public void attachAll(Partido partido) {
        partidoContext.setPartido(partido);
        for (PartidoObserver o : observers) {
            partidoContext.registerObserver(o);
        }
    }
}
