package com.tpo.proceso.repository;


import com.tpo.proceso.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Usuario, Integer> {

    @Query("select u from Usuario u where u.nombreUsuario = ?1")
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

    //@Query("select u from Usuario u where u.mail = ?1")
    Optional<Usuario> findByMail(String mail);

    @Query("select u from Usuario u where (u.nombreUsuario = ?1 OR u.mail = ?1)")
    Optional<Usuario> findByIdentificador(String identificador);

    @Query("SELECT c FROM Usuario c WHERE c.id = ?1")
    Optional<Usuario> findByUser(int user_id);

    @Query("SELECT c FROM Usuario c WHERE c.deporte = ?1")
    List<Usuario> findByDeporte(String deporte);

}
