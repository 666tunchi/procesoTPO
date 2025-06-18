package com.tpo.proceso;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpo.proceso.dto.PartidoRequestDto;
import com.tpo.proceso.model.UsuarioInicioSesion;
import com.tpo.proceso.model.RegisterRequest;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import com.tpo.proceso.model.Geolocalizacion;
import com.tpo.proceso.model.Nivel;
import com.tpo.proceso.model.Partido;
import com.tpo.proceso.model.Role;
import com.tpo.proceso.model.Usuario;
import com.tpo.proceso.repository.PartidoRepository;
import com.tpo.proceso.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.Date;
import java.util.List;

import static com.tpo.proceso.model.Nivel.AVANZADO;
import static com.tpo.proceso.model.Nivel.PRINCIPIANTE;
import static com.tpo.proceso.model.Role.USER;
import static org.assertj.core.api.Fail.fail;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ApiIntegrationTestWithJwt {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    UserRepository usuarioRepo;
    @Autowired
    PartidoRepository partidoRepo;

    private Usuario existingUser;
    private String jwtToken;

    @BeforeEach
    void setUp() throws Exception {

        // Insertamos un usuario de base con contraseña encriptada
        existingUser = Usuario.builder()
                .nombreUsuario("martoUser")
                .mail("lucia.martinez@example.com")
                .contrasena("$2a$10$DOWJmX8C9R/1eXJH.GV6xuPwQx/.eE1lY1Fz9O/BS4jP1L6kdg1iy") // pass1234
                .fechaNacimiento(Date.valueOf("2001-08-27"))
                .nombre("Martin")
                .apellido("Dotto")
                .rol(USER)
                .deporte("FUTBOL")
                .geolocalizacion(new Geolocalizacion(-34.61, -58.37))
                .nivel(Nivel.INTERMEDIO)
                .partidos(5)
                .build();

        String password = "Passw0rd!";

        // Obtenemos token llamando al endpoint correcto de autenticación
        UsuarioInicioSesion login = new UsuarioInicioSesion(existingUser.getMail(), password);
        MvcResult loginResult = mockMvc.perform(post("/usuario/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andReturn();
        String respLogin = loginResult.getResponse().getContentAsString();
        System.out.println("Login response: " + respLogin);
        JsonNode node = objectMapper.readTree(respLogin);
        String token = null;
        if (node.has("token")) {
            token = node.get("token").asText();
        } else if (node.has("jwt")) {
            token = node.get("jwt").asText();
        } else if (node.has("accessToken")) {
            token = node.get("accessToken").asText();
        } else if (node.has("access_token")) {
            token = node.get("access_token").asText();
        } else {
            fail("No JWT token field found in login response: " + respLogin);
        }
        jwtToken = token;
        System.out.println("Extracted JWT: " + jwtToken);

        // Insertamos dos partidos abiertos con setters de niveles
        Partido p1 = new Partido();
        p1.setDeporte("FUTBOL");
        p1.setGeolocalizacion(new Geolocalizacion(-34.62, -58.36));
        p1.setCantidadJugadoresRequeridos(3);
        p1.setNivelMinimo(PRINCIPIANTE);
        p1.setNivelMaximo(AVANZADO);
        partidoRepo.save(p1);

        Partido p2 = new Partido();
        p2.setDeporte("BASQUET");
        p2.setGeolocalizacion(new Geolocalizacion(-34.60, -58.38));
        p2.setCantidadJugadoresRequeridos(4);
        p2.setNivelMinimo(PRINCIPIANTE);
        p2.setNivelMaximo(AVANZADO);
        partidoRepo.save(p2);
    }

    @Test
    void createAndListAndDeletePartido() throws Exception {
        PartidoRequestDto dto = PartidoRequestDto.builder()
                .deporte("VOLEY")
                .cantidadJugadoresRequeridos(2)
                .geolocalizacion(new com.tpo.proceso.dto.GeolocalizacionDto(-34.58, -58.37))
                .nivelMinimo(Nivel.PRINCIPIANTE)   // <-- añadido
                .nivelMaximo(Nivel.AVANZADO)       // <-- añadido
                .build();

        // Create
        MvcResult createPartidoResult = mockMvc.perform(post("/api/partidos")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.deporte").value("VOLEY"))
                .andReturn();
        System.out.println("Create Partido response: " + createPartidoResult.getResponse().getContentAsString());

        // List
        MvcResult listPartidosResult = mockMvc.perform(get("/api/partidos")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(3)))
                .andReturn();
        System.out.println("List Partidos response: " + listPartidosResult.getResponse().getContentAsString());

        // Delete
        List<Partido> all = partidoRepo.findAll();
        MvcResult deletePartidoResult = mockMvc.perform(delete("/api/partidos/{id}", all.get(0).getId())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNoContent())
                .andReturn();
        System.out.println("Delete Partido status: " + deletePartidoResult.getResponse().getStatus());
    }

    @Test
    void stateTransitionsAndEmparejar() throws Exception {
        Partido p = partidoRepo.findByDeporte("FUTBOL").get(0);

        // Confirmar
        MvcResult confirmarResult = mockMvc.perform(post("/api/partidos/{id}/confirmar", p.getId())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estadoNombre").value("Confirmado"))
                .andReturn();
        System.out.println("Confirmar response: " + confirmarResult.getResponse().getContentAsString());

        // Iniciar
//        MvcResult iniciarResult = mockMvc.perform(post("/api/partidos/{id}/iniciar", p.getId())
//                        .header("Authorization", "Bearer " + jwtToken))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.estadoNombre").value("En juego"))
//                .andReturn();
//        System.out.println("Iniciar response: " + iniciarResult.getResponse().getContentAsString());
//
//        // Emparejar
//        MvcResult emparejarResult = mockMvc.perform(get("/api/partidos/{id}/emparejar", p.getId())
//                        .header("Authorization", "Bearer " + jwtToken)
//                        .param("criterio", "porNivel"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].mail").value("martin@example.com"))
//                .andReturn();
//        System.out.println("Emparejar response: " + emparejarResult.getResponse().getContentAsString());
    }

    @Test
    void buscarYUnirseAPartido() throws Exception {
        // Buscar
        MvcResult buscarResult = mockMvc.perform(get("/api/seleccion/buscar")
                        .header("Authorization", "Bearer " + jwtToken)
                        .param("criterio", "porCercaniaPartido"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deporte").value("FUTBOL"))
                .andReturn();
        System.out.println("Buscar response: " + buscarResult.getResponse().getContentAsString());

        // Unirse
        Partido chosen = partidoRepo.findByDeporte("FUTBOL").get(0);
        MvcResult unirseResult = mockMvc.perform(post("/api/seleccion/{id}/unirse", chosen.getId())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jugadores", hasSize(1)))
                .andExpect(jsonPath("$.jugadores[0].mail").value("lucia.martinez@example.com"))
                .andReturn();
        System.out.println("Unirse response: " + unirseResult.getResponse().getContentAsString());
    }
}
