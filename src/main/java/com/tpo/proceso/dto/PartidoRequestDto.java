package com.tpo.proceso.dto;

import com.tpo.proceso.model.Nivel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

/**
 * DTO para crear o actualizar un Partido.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartidoRequestDto {
    @NotBlank
    private String deporte;

    @Valid @NotNull
    private GeolocalizacionDto geolocalizacion;

    @Min(1)
    private int cantidadJugadoresRequeridos;

    @NotNull
    private Nivel nivelMinimo;    // <— nuevo

    @NotNull
    private Nivel nivelMaximo;    // <— nuevo
}
