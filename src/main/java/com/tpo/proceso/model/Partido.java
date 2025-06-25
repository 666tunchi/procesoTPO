package com.tpo.proceso.model;

import com.tpo.proceso.state.IEstadoPartido;
import com.tpo.proceso.state.EstadoPartidoFactory;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;

@Data
@Entity
@Table(name = "partidos")
public class Partido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private String deporte;

    @Embedded
    private Geolocalizacion geolocalizacion;

    private int cantidadJugadoresRequeridos;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Nivel nivelMinimo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Nivel nivelMaximo;

    @ManyToMany
    @JoinTable(
            name = "partido_jugadores",
            joinColumns = @JoinColumn(name = "partido_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private Set<Usuario> jugadores = new HashSet<>();

    private String estadoNombre;

    @Transient
    private IEstadoPartido estado;

    public Partido() {
        this.fechaCreacion = LocalDateTime.now();
        // Inicializar con el estado por defecto
        this.estadoNombre = "Necesitamos jugadores";
        this.estado = EstadoPartidoFactory.getEstado("Necesitamos jugadores");
    }

    /**
     * Constructor de conveniencia incluyendo niveles.
     */
    public Partido(String deporte,
                   Geolocalizacion geolocalizacion,
                   int cantidadJugadoresRequeridos,
                   Nivel nivelMinimo,
                   Nivel nivelMaximo) {
        this();
        this.deporte = deporte;
        this.geolocalizacion = geolocalizacion;
        this.cantidadJugadoresRequeridos = cantidadJugadoresRequeridos;
        this.nivelMinimo = nivelMinimo;
        this.nivelMaximo = nivelMaximo;
    }

    @PostLoad
    private void initEstado() {
        // Este método ahora será manejado por PartidoContext
        if (estadoNombre != null && estado == null) {
            this.estado = EstadoPartidoFactory.getEstado(estadoNombre);
        }
    }
}
