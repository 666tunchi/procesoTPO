package com.tpo.proceso.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nombreUsuario;
    private String mail;
    private String contrasena;
    private Date fechaNacimiento;
    private String nombre;
    private String apellido;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Nivel nivel;

    @Enumerated(EnumType.STRING)
    private Role rol;

    /**
     * Nuevo campo para el deporte favorito o practicado por el usuario.
     */
    private String deporte;
    private int partidos = 0;

    @Embedded
    private Geolocalizacion geolocalizacion;

    /**
     * Constructor parcial sin geolocalización y sin id.
     */
    public Usuario(String nombreUsuario,
                   String mail,
                   String contrasena,
                   Date fechaNacimiento,
                   String nombre,
                   String apellido,
                   Role rol,
                   String deporte) {
        this.nombreUsuario = nombreUsuario;
        this.mail = mail;
        this.contrasena = contrasena;
        this.fechaNacimiento = fechaNacimiento;
        this.nombre = nombre;
        this.apellido = apellido;
        this.rol = rol;
        this.deporte = deporte;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(rol.name()));
    }

    @Override
    public String getPassword() {
        return contrasena;
    }

    @Override
    public String getUsername() {
        return this.mail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
