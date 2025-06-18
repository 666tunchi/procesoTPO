package com.tpo.proceso.strategy;

import com.tpo.proceso.model.Partido;
import com.tpo.proceso.model.Usuario;

import java.util.List;

public interface EmparejamientoStrategy {
    List<Usuario> emparejar(Partido partido, List<Usuario> candidatos);
}