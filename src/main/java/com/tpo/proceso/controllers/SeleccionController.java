package com.tpo.proceso.controllers;

import com.tpo.proceso.dto.GeolocalizacionDto;
import com.tpo.proceso.dto.PartidoResponseDto;
import com.tpo.proceso.model.Partido;
import com.tpo.proceso.model.Usuario;
import com.tpo.proceso.model.UsuarioDTO;
import com.tpo.proceso.service.PartidoSeleccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/seleccion")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class SeleccionController {

    private final PartidoSeleccionService seleccionService;

    /**
     * Busca el mejor partido "abierto" al que se puede unir el solicitante,
     * según el criterio ("porCercaniaPartido", "porNivelPartido" o "porHistorialPartido").
     *
     * @param criterio     clave de la estrategia de selección
     * @param solicitante  usuario autenticado
     * @return partido escogido o 204 No Content si no hay ninguno
     */
    @GetMapping("/buscar")
    public ResponseEntity<PartidoResponseDto> buscar(
            @RequestParam String criterio,
            @AuthenticationPrincipal Usuario solicitante) {

        return seleccionService.buscar(criterio, solicitante)
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    /**
     * Permite que el usuario autenticado se una al partido con el ID dado.
     *
     * @param id           ID del partido
     * @param solicitante  usuario autenticado
     * @return partido actualizado con el nuevo jugador
     */
    @PostMapping("/{id}/unirse")
    public ResponseEntity<PartidoResponseDto> unirse(
            @PathVariable int id,
            @AuthenticationPrincipal Usuario solicitante) {

        Partido actualizado = seleccionService.unirseAPartido(id, solicitante.getId());
        return ResponseEntity.ok(toDto(actualizado));
    }

    // —————— Mapeos de entidad a DTO ——————

    private PartidoResponseDto toDto(Partido p) {
        List<UsuarioDTO> jugadores = p.getJugadores().stream()
                .map(this::toUsuarioDto)
                .collect(Collectors.toList());

        return PartidoResponseDto.builder()
                .id(p.getId())
                .fechaCreacion(p.getFechaCreacion())
                .deporte(p.getDeporte())
                .geolocalizacion(GeolocalizacionDto.builder()
                        .latitud(p.getGeolocalizacion().getLatitud())
                        .longitud(p.getGeolocalizacion().getLongitud())
                        .build())
                .cantidadJugadoresRequeridos(p.getCantidadJugadoresRequeridos())
                .estadoNombre(p.getEstadoNombre())
                .jugadores(jugadores)
                .build();
    }

    private UsuarioDTO toUsuarioDto(Usuario u) {
        return UsuarioDTO.builder()
                .nombreUsuario(u.getNombreUsuario())
                .mail(u.getMail())
                .nombre(u.getNombre())
                .apellido(u.getApellido())
                .rol(u.getRol().name())
                .deporte(u.getDeporte())
                .geolocalizacion(GeolocalizacionDto.builder()
                        .latitud(u.getGeolocalizacion().getLatitud())
                        .longitud(u.getGeolocalizacion().getLongitud())
                        .build())
                .build();
    }
}
