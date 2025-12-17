package br.com.dio.dio_spring_security.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "name")
    private String nome;

    private String email;

    @Column(name = "password")
    private String senha;


    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name="role_id")
    @CollectionTable(name="user_roles", joinColumns = @JoinColumn(name = "user_id"))
    private List<String> roles = new ArrayList<>();

    @Transient
    private Set<GrantedAuthority> autorizacoes;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(autorizacoes != null && !autorizacoes.isEmpty()){
            return autorizacoes;
        }
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email; //login
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
