package br.com.dio.dio_spring_security.config;

import br.com.dio.dio_spring_security.model.Usuario;
import br.com.dio.dio_spring_security.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthConfigService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public AuthConfigService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(login);

        if (usuario == null) {
            throw new UsernameNotFoundException(login);
        }
        Set<GrantedAuthority> authorities = new HashSet<>();
        usuario.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        });
        usuario.setAutorizacoes(authorities);
        return usuario;
    }
}
