package com.tpo.proceso.model;

import lombok.Data;
import org.springframework.http.ResponseEntity;

@Data
public class ActualizarDatosDTO {

    private String nombre;
    private String apellido;
    private String mail;
    

    public ResponseEntity validarDatos() {
        
        if (nombre != null && mail != null && apellido != null) {
            if (nombre.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El nombre no puede estar vacío");
            }
            if (esNumero(nombre)) {
                return ResponseEntity.badRequest().body("El nombre no puede ser un número");
            }
            
            if (mail.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El mail no puede estar vacío");
            }
            if (esNumero(mail)) {
                return ResponseEntity.badRequest().body("El mail no puede ser un número");
            }
            if (!esEmailValido(mail)) {
                return ResponseEntity.badRequest().body("El mail no tiene un formato válido");
            }
            
            if (apellido.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El apellido no puede estar vacío");
            }
            if (esNumero(apellido)) {
                return ResponseEntity.badRequest().body("El apellido no puede ser un número");
            }
        } else {
            return ResponseEntity.badRequest().body("Los campos no pueden ser nulos");
        }
        return null;
    }

    private boolean esNumero(String valor) {
        try {
            Integer.parseInt(valor);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    private boolean esEmailValido(String email) {
        return email.matches("^[A-Za-z0-9]+[A-Za-z0-9._%+-]*@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

}
