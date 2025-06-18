package com.tpo.proceso.model;


import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UsuarioDTO toDTO(Usuario user) {
        return UsuarioDTO.builder()
                .nombreUsuario(user.getNombreUsuario())
                .nombre(user.getNombre())
                .apellido(user.getApellido())
                .mail(user.getMail()).
                build();
    }

    // Convierte un DTO UserDTO a una entidad User
    public Usuario toEntity(UsuarioDTO dto) {
        Usuario user = new Usuario();
        user.setNombre(dto.getNombre());
        user.setMail(dto.getMail());
        return user;
    }
}
