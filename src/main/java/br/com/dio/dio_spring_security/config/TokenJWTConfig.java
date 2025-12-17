package br.com.dio.dio_spring_security.config;

import br.com.dio.dio_spring_security.model.Usuario;
import br.com.dio.dio_spring_security.model.UsuarioLogado;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TokenJWTConfig {

    /**
     * Em projeto real essa secret vai ser carregada de algum lugar (.properties, keyvalues, etc)
     * É usada na criptografia do TOKEN como uma chave pra validar a integridade do token
     */
    @Value("${security.config.key}")
    private String chaveSecreta;

    @Value("${security.config.expiration}")
    private Long expiracao;

    public String gerarToken(Usuario usuario){

        Algorithm algoritmo = Algorithm.HMAC256(chaveSecreta);

        return JWT.create()
                .withClaim("userId", usuario.getId())                   // dados - pode adicionar o que precisar
                .withClaim("userName", usuario.getNome())               // dados - pode adicionar o que precisar (perfis, etc)
                .withClaim("roles", usuario.getRoles())                 // dados - roles
                .withSubject(usuario.getEmail())                              //
                .withExpiresAt(Instant.now().plusSeconds(expiracao)) // expiração (1 dia)
                .withIssuedAt(Instant.now())                                  // data da emissão
                .sign(algoritmo);                                             // assinatura para validação
    }

    public Optional<UsuarioLogado> validarToken(String token){

            Algorithm algoritmo = Algorithm.HMAC256(chaveSecreta);

            DecodedJWT decode = JWT.require(algoritmo).build().verify(token);

            return Optional.of(UsuarioLogado.builder()
                    .id(decode.getClaim("userId").asLong())       // pegando todos os dados que passei como claim, perfis, etc
                    .nome(decode.getClaim("userName").asString())
                    .roles(decode.getClaim("roles").asList(String.class))
                    .autorizacoes(decode.getClaim("roles").asList(String.class).stream()
                                    .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                                    .map(SimpleGrantedAuthority::new)
                                    .collect(Collectors.toSet()))
                    .email(decode.getSubject())
                    .build());
    }

}
