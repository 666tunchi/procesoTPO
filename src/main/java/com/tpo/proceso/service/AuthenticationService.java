package com.tpo.proceso.service;

import com.tpo.proceso.controllers.GestionUsuarioController.config.JwtService;
import com.tpo.proceso.model.*;
import com.tpo.proceso.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

        private final UserRepository repository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;

        public AuthenticationResponse register(RegisterRequest request) throws Exception
        {
                Optional<Usuario> userMail = repository.findByMail(request.getEmail());
                Optional<Usuario> userName = repository.findByNombreUsuario(request.getUserName());

                if (userMail.isPresent()) {
                        throw new Exception("El correo electrónico ya está en uso.");
                }
                if (userName.isPresent()) {
                        throw new Exception("El nombre de usuario ya está en uso.");
                }

                var user = Usuario.builder()
                        .nombreUsuario(request.getUserName())
                        .nombre(request.getFirstname())
                        .apellido(request.getLastname())
                        .mail(request.getEmail())
                        .contrasena(passwordEncoder.encode(request.getPassword()))
                        .fechaNacimiento(request.getFechaNacimiento())
                        .rol(request.getRole() != null ? request.getRole() : Role.USER)
                        .deporte(request.getDeporte())
                        .nivel(request.getNivel())
                        .geolocalizacion(request.getGeolocalizacion())
                        .build();

                repository.save(user);
                var jwtToken = jwtService.generateToken(user);
                return AuthenticationResponse.builder()
                        .accessToken(jwtToken)
                        .build();
        }

        public AuthenticationResponse authenticate(UsuarioInicioSesion request) {

                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()));

                var user = repository.findByMail(request.getEmail())
                        .orElseThrow();

                var jwtToken = jwtService.generateToken(user);
                return AuthenticationResponse.builder()
                        .accessToken(jwtToken)
                        .build();

        }

        public Usuario getUsuarioAutenticado() {
                return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
}