package br.com.dio.dio_spring_security.config.filter;

import br.com.dio.dio_spring_security.config.TokenJWTConfig;
import br.com.dio.dio_spring_security.model.UsuarioLogado;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Value("${security.config.prefix}")
    private String prefixo;

    private TokenJWTConfig tokenJWTConfig;

    @Autowired
    public SecurityFilter(TokenJWTConfig tokenJWTConfig) {
        this.tokenJWTConfig = tokenJWTConfig;
    }


    /**
     * Aqui faz a validação do token em cada acesso usando o Filter.
     * Esse filtro precisa ser habilitado no SpringSecurity no SecurityConfig
     * nas definições de permissões.
     * */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");
        String inicio = prefixo + " ";

        if(Strings.isNotEmpty(authorization) && authorization.startsWith(inicio)) {
            String token = authorization.substring(inicio.length());
            Optional<UsuarioLogado> opUsuarioLogado = tokenJWTConfig.validarToken(token);
            if (opUsuarioLogado.isPresent()) {
                UsuarioLogado usuarioLogado = opUsuarioLogado.get();
                UsernamePasswordAuthenticationToken tokenUsuario = new UsernamePasswordAuthenticationToken(
                        usuarioLogado, null, usuarioLogado.getAutorizacoes());
                SecurityContextHolder.getContext().setAuthentication(tokenUsuario);

            }

        }
        filterChain.doFilter(request, response);

    }
}
