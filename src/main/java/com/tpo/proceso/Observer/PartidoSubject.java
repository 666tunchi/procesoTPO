package com.tpo.proceso.Observer;

public interface PartidoSubject {
    void registerObserver(PartidoObserver observer);
    void removeObserver(PartidoObserver observer);
    void notifyObservers();
}