package br.com.dio.dio_spring_security.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Builder
@Getter
@Setter
public class UsuarioLogado {

    //Traria os dados do usuário logado (dados de perfil com permissões, etc)
    private Long id;
    private String nome;
    private String email;
    private List<String> roles = new ArrayList<>();
    private Set<GrantedAuthority> autorizacoes;
}
