package com.tpo.proceso.controllers;

import com.tpo.proceso.Observer.PartidoObserver;
import com.tpo.proceso.dto.GeolocalizacionDto;
import com.tpo.proceso.dto.PartidoRequestDto;
import com.tpo.proceso.dto.PartidoResponseDto;
import com.tpo.proceso.model.UsuarioDTO;
import com.tpo.proceso.model.Geolocalizacion;
import com.tpo.proceso.model.Partido;
import com.tpo.proceso.model.Usuario;
import com.tpo.proceso.model.UsuarioDTO;
import com.tpo.proceso.repository.PartidoRepository;
import com.tpo.proceso.repository.UserRepository;
import com.tpo.proceso.service.EmparejamientoService;
import com.tpo.proceso.service.ObserverRegistrarService;
import com.tpo.proceso.service.PartidoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/partidos")
@RequiredArgsConstructor
public class PartidoController {

    private final PartidoRepository partidoRepository;
    private final UserRepository usuarioRepository;
    private final EmparejamientoService emparejamientoService;
    private final ObserverRegistrarService observerRegistrarService;
    private final List<PartidoObserver> observers;  // inyecta todos tus observers


    @GetMapping
    public List<PartidoResponseDto> getAllPartidos() {
        return partidoRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartidoResponseDto> getPartidoById(@PathVariable int id) {
        return partidoRepository.findById(id)
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PartidoResponseDto> createPartido(
            @Valid @RequestBody PartidoRequestDto dto) {

        Partido p = new Partido();
        p.setDeporte(dto.getDeporte());
        p.setGeolocalizacion(toGeo(dto.getGeolocalizacion()));
        p.setCantidadJugadoresRequeridos(dto.getCantidadJugadoresRequeridos());
        p.setNivelMinimo(dto.getNivelMinimo());   // <— asignamos
        p.setNivelMaximo(dto.getNivelMaximo());   // <— asignamos

        Partido saved = partidoRepository.save(p);

        // Aquí “adaptamos” el Partido al Observer pattern
        observerRegistrarService.attachAll(saved);

        saved.notifyObservers();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartidoResponseDto> updatePartido(
            @PathVariable int id,
            @Valid @RequestBody PartidoRequestDto dto) {
        return partidoRepository.findById(id)
                .map(existing -> {
                    existing.setDeporte(dto.getDeporte());
                    existing.setGeolocalizacion(toGeo(dto.getGeolocalizacion()));
                    existing.setCantidadJugadoresRequeridos(dto.getCantidadJugadoresRequeridos());
                    Partido updated = partidoRepository.save(existing);
                    return ResponseEntity.ok(toDto(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartido(@PathVariable int id) {
        return partidoRepository.findById(id)
                .map(p -> {
                    partidoRepository.delete(p);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/jugadores")
    public ResponseEntity<PartidoResponseDto> agregarJugador(
            @PathVariable int id,
            @RequestParam("usuarioId") int usuarioId) {
        Usuario jugador = usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Usuario no encontrado: " + usuarioId));
        Partido p = partidoRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Partido no encontrado: " + id));
        p.agregarJugador(jugador);
        Partido saved = partidoRepository.save(p);
        return ResponseEntity.ok(toDto(saved));
    }

    @PostMapping("/{id}/confirmar")
    public ResponseEntity<PartidoResponseDto> confirmar(@PathVariable int id) {
        Partido p = partidoRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Partido no encontrado: " + id));

        // Registrar observers
        observerRegistrarService.attachAll(p);

        try {
            // Intentamos confirmar (si el estado no lo permite, lanza)
            p.confirmar();
        } catch (IllegalStateException ex) {
            // Retornamos 400 con el mensaje de por qué no se pudo confirmar
            return ResponseEntity
                    .badRequest()
                    .body(PartidoResponseDto.builder()
                            .id(p.getId())
                            .estadoNombre(p.getEstadoNombre())
                            .build());
        }

        // Persistimos el cambio y devolvemos el DTO
        Partido saved = partidoRepository.save(p);
        return ResponseEntity.ok(toDto(saved));
    }

    @PostMapping("/{id}/iniciar")
    public ResponseEntity<PartidoResponseDto> iniciar(@PathVariable int id) {
        Partido p = partidoRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Partido no encontrado: " + id));
        observerRegistrarService.attachAll(p);
        p.iniciar();
        return ResponseEntity.ok(toDto(partidoRepository.save(p)));
    }

    @PostMapping("/{id}/finalizar")
    public ResponseEntity<PartidoResponseDto> finalizar(@PathVariable int id) {
        Partido p = partidoRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Partido no encontrado: " + id));
        observerRegistrarService.attachAll(p);

        p.finalizar();
        return ResponseEntity.ok(toDto(partidoRepository.save(p)));
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<PartidoResponseDto> cancelar(@PathVariable int id) {
        Partido p = partidoRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Partido no encontrado: " + id));
        observerRegistrarService.attachAll(p);

        p.cancelar();
        return ResponseEntity.ok(toDto(partidoRepository.save(p)));
    }

    @GetMapping("/{id}/emparejar")
    public ResponseEntity<List<UsuarioDTO>> emparejar(
            @PathVariable int id,
            @RequestParam(defaultValue = "porCercania") String criterio) {
        List<Usuario> matched = emparejamientoService.emparejarParaPartido(id, criterio);
        List<UsuarioDTO> dtos = matched.stream()
                .map(this::toUsuarioDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // —————— Mapeos DTO ⇄ Entidad ——————


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
                .deporte(u.getDeporte())
                .geolocalizacion(GeolocalizacionDto.builder()
                        .latitud(u.getGeolocalizacion().getLatitud())
                        .longitud(u.getGeolocalizacion().getLongitud())
                        .build())
                .build();
    }

    private Geolocalizacion toGeo(GeolocalizacionDto dto) {
        return new Geolocalizacion(dto.getLatitud(), dto.getLongitud());
    }
}