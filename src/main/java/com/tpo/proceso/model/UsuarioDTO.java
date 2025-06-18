package com.tpo.proceso.model;


import com.tpo.proceso.dto.GeolocalizacionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private String nombreUsuario;
    private String mail;
    private String nombre;
    private String apellido;
    private String rol;
    private String deporte;
    private GeolocalizacionDto geolocalizacion;
    private Nivel nivel;
}
