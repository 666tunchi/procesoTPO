package com.tpo.proceso.model;

//import com.example.demo.repository.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String userName;
    private String firstname;
    private String lastname;
    private Date fechaNacimiento;
    private String email;
    private String password;
    private Role role;
    private Nivel nivel;
    private String deporte;
    private Geolocalizacion geolocalizacion;
}
