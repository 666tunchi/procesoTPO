package com.tpo.proceso.dto;

import com.tpo.proceso.model.UsuarioDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO que devuelve datos de Partido (incluye estado y lista de jugadores).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartidoResponseDto {
    private Long id;
    private LocalDateTime fechaCreacion;
    private String deporte;
    private GeolocalizacionDto geolocalizacion;
    private int cantidadJugadoresRequeridos;
    private String estadoNombre;
    private List<UsuarioDTO> jugadores;
}

