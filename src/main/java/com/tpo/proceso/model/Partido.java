package com.tpo.proceso.model;

import com.tpo.proceso.Observer.PartidoObserver;
import com.tpo.proceso.Observer.PartidoSubject;
import com.tpo.proceso.state.IEstadoPartido;
import com.tpo.proceso.state.EstadoPartidoFactory;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.HashSet;

@Data
@Entity
@Table(name = "partidos")
public class Partido implements PartidoSubject {

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

    @Transient
    private final List<PartidoObserver> observers = new CopyOnWriteArrayList<>();

    public Partido() {
        this.fechaCreacion = LocalDateTime.now();
        cambiarEstado(EstadoPartidoFactory.getEstado("Necesitamos jugadores"));
    }

    /**
     * Constructor de conveniencia incluyendo niveles.
     */
    public Partido(String deporte,
                   Geolocalizacion geolocalizacion,
                   int cantidadJugadoresRequeridos,
                   Nivel nivelMinimo,
                   Nivel nivelMaximo) {
        this();  // inicializa fecha y estado
        this.deporte = deporte;
        this.geolocalizacion = geolocalizacion;
        this.cantidadJugadoresRequeridos = cantidadJugadoresRequeridos;
        this.nivelMinimo = nivelMinimo;
        this.nivelMaximo = nivelMaximo;
    }

    @PostLoad
    private void initEstado() {
        this.estado = EstadoPartidoFactory.getEstado(estadoNombre);
    }

    // === Observer ===
    @Override
    public void registerObserver(PartidoObserver o) {
        observers.add(o);
    }
    @Override
    public void removeObserver(PartidoObserver o) {
        observers.remove(o);
    }
    @Override
    public void notifyObservers() {
        for (PartidoObserver o : observers) {
            o.onEstadoCambiado(this);
        }
    }

    // === State ===
    public void cambiarEstado(IEstadoPartido nuevoEstado) {
        this.estado = nuevoEstado;
        this.estadoNombre = nuevoEstado.getNombreEstado();
        if ("En juego".equals(nuevoEstado.getNombreEstado())) {
            incrementarPartidosJugados();
        }
        notifyObservers();
    }
    public IEstadoPartido getEstado() {
        if (estado == null) {
            this.estado = EstadoPartidoFactory.getEstado(estadoNombre);
        }
        return estado;
    }

    // Métodos delegados al estado
    public void agregarJugador(Usuario jugador) {
        getEstado().agregarJugador(this, jugador);
    }
    public void confirmar() {
        getEstado().confirmar(this);
    }
    public void iniciar() {
        getEstado().iniciar(this);
    }
    public void finalizar() {
        getEstado().finalizar(this);
    }
    public void cancelar() {
        getEstado().cancelar(this);
    }

    private void incrementarPartidosJugados() {
        for (Usuario u : jugadores) {
            u.setPartidos(u.getPartidos() + 1);
        }
    }
}
