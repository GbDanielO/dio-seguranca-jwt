package br.com.dio.dio_spring_security.config;

import br.com.dio.dio_spring_security.model.Usuario;
import br.com.dio.dio_spring_security.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

//Adiciona usuários iniciais a aplicação
@Component
public class StartApplication implements CommandLineRunner {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        Usuario user = repository.findByEmail("admin@email.com");
        if(user==null){
            user = new Usuario();
            user.setNome("ADMIN");
            user.setEmail("admin@email.com");
            user.setSenha(passwordEncoder.encode("master123"));
            user.getRoles().add("MANAGERS");
            user.getRoles().add("USERS");
            repository.save(user);
        }
        user = repository.findByEmail("usuario@email.com");
        if(user ==null){
            user = new Usuario();
            user.setNome("USUARIO");
            user.setEmail("usuario@email.com");
            user.setSenha(passwordEncoder.encode("user123"));
            user.getRoles().add("USERS");
            repository.save(user);
        }
    }
}