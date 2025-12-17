package br.com.dio.dio_spring_security.service;

import br.com.dio.dio_spring_security.config.TokenJWTConfig;
import br.com.dio.dio_spring_security.model.LoginRequestDTO;
import br.com.dio.dio_spring_security.model.LoginResponseDTO;
import br.com.dio.dio_spring_security.model.RegistrarUsuarioDTO;
import br.com.dio.dio_spring_security.model.Usuario;
import br.com.dio.dio_spring_security.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UsuarioService {

    private UsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder;                   // bean definido no SecurityConfig
    private final AuthenticationManager authenticationManager; // bean definido no SecurityConfig
    private final TokenJWTConfig tokenJWTConfig;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager, TokenJWTConfig tokenJWTConfig) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenJWTConfig = tokenJWTConfig;
    }

    public RegistrarUsuarioDTO criarUsuario(RegistrarUsuarioDTO registrarUsuarioDTO){
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(registrarUsuarioDTO.getNome());
        novoUsuario.setEmail(registrarUsuarioDTO.getEmail());
        novoUsuario.setSenha(passwordEncoder.encode(registrarUsuarioDTO.getSenha()));

        usuarioRepository.save(novoUsuario);
        registrarUsuarioDTO.setSenha(null);
        return registrarUsuarioDTO;
    }

    public LoginResponseDTO login(@Valid @RequestBody LoginRequestDTO login){
        /**
         * Ao receber o login, a classe UsernamePasswordAuthenticationToken vai pegar login e senha
         * e passar para o authenticate do AuthenticationManager. Esse método busca alguém que implemente UserDetailsService.
         * Essa interface garante que haverá um método que retorna o usuário pelo login, pois ela tem o método loadUserByUsername
         * que precisamos implementar. Por baixo dos panos ela já valida se o usuário logado que passamos email e senha
         * bate com o usuário e senha retornados na classe que implementa UserDetailsService (AuthConfig no nosso caso).
         * Outro ponto importante, é que não preciso encodar novamente a senha que veio do login, o Spring se vira com isso de
         * forma abstrata também!!!
         *
         * ALTA ABSTRAÇÃO!!!
         */
        // não preciso encodar a senha no login pois o spring faz isso por baixo dos panos
        UsernamePasswordAuthenticationToken tokenUsuarioESenha = new UsernamePasswordAuthenticationToken(login.email(), login.password());
        Authentication authentication = authenticationManager.authenticate(tokenUsuarioESenha); // authentication recebe o usuário carregado do AuthConfig

        Usuario usuario = (Usuario) authentication.getPrincipal();
        String token = tokenJWTConfig.gerarToken(usuario);
        return new LoginResponseDTO(token);
    }

}
