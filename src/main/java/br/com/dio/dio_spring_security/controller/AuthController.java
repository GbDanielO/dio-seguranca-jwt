package br.com.dio.dio_spring_security.controller;

import br.com.dio.dio_spring_security.model.LoginRequestDTO;
import br.com.dio.dio_spring_security.model.LoginResponseDTO;
import br.com.dio.dio_spring_security.model.RegistrarUsuarioDTO;
import br.com.dio.dio_spring_security.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    @Autowired
    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Aqui faz o login e geração de token, no SecurityFilter faz a validação do token em cada acesso
     * usando o Filter.
     * @param login
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO login){

        LoginResponseDTO loginResponseDTO = usuarioService.login(login);

        return ResponseEntity.ok(loginResponseDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrarUsuarioDTO> register(@Valid @RequestBody RegistrarUsuarioDTO registrarUsuarioDTO){

        registrarUsuarioDTO = usuarioService.criarUsuario(registrarUsuarioDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(registrarUsuarioDTO);
    }
}
