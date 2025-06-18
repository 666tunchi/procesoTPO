package com.tpo.proceso.service;

import com.tpo.proceso.Observer.PartidoObserver;
import com.tpo.proceso.model.Partido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObserverRegistrarService {

    private final List<PartidoObserver> observers;

    @Autowired
    public ObserverRegistrarService(List<PartidoObserver> observers) {
        this.observers = observers;
    }

    /**
     * Registra en el partido todos los observers disponibles.
     */
    public void attachAll(Partido partido) {
        for (PartidoObserver o : observers) {
            partido.registerObserver(o);
        }
    }
}
