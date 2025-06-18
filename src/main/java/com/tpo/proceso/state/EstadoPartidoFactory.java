package com.tpo.proceso.state;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Fábrica para obtener instancias de IEstadoPartido por nombre.
 */
public class EstadoPartidoFactory {

    private static final Map<String, Supplier<IEstadoPartido>> FACTORIES = new HashMap<>();

    static {
        FACTORIES.put("Necesitamos jugadores", NecesitamosJugadoresState::new);
        FACTORIES.put("Confirmado", ConfirmadoState::new);
        FACTORIES.put("En juego", EnJuegoState::new);
        FACTORIES.put("Finalizado", FinalizadoState::new);
        FACTORIES.put("Cancelado", CanceladoState::new);
        // agrega aquí más estados si los tienes
    }

    /**
     * Devuelve una nueva instancia del estado cuyo nombre coincida con {@code nombreEstado}.
     * @param nombreEstado nombre único de tu estado (debe coincidir con getNombreEstado()).
     * @return nueva instancia de IEstadoPartido.
     * @throws IllegalArgumentException si el nombre no existe en la fábrica.
     */
    public static IEstadoPartido getEstado(String nombreEstado) {
        Supplier<IEstadoPartido> supplier = FACTORIES.get(nombreEstado);
        if (supplier == null) {
            throw new IllegalArgumentException("Estado desconocido: " + nombreEstado);
        }
        return supplier.get();
    }
}

