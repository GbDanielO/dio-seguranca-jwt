package br.com.dio.dio_spring_security.repository;

import br.com.dio.dio_spring_security.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("SELECT u FROM Usuario u JOIN FETCH u.roles WHERE u.email = :email ")
    public Usuario findByEmail(@Param("email") String email);
}
