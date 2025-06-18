package com.tpo.proceso.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class EmparejamientoFactory {
    private final Map<String, EmparejamientoStrategy> strategies;

    @Autowired
    public EmparejamientoFactory(Map<String, EmparejamientoStrategy> strategies) {
        this.strategies = strategies;
    }

    public EmparejamientoStrategy getStrategy(String key) {
        EmparejamientoStrategy strat = strategies.get(key);
        if (strat == null) {
            throw new IllegalArgumentException("Estrategia desconocida: " + key);
        }
        return strat;
    }
}

