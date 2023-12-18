package br.com.fabricio.ama.amadesafio.models;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Usuario implements UserDetails{
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Integer id;

    @Column(unique = true)
    private String username;

    private String nome;

    @Hidden
    private String password;

    private Boolean isAdmin = false;

    public Usuario() {
    }

    public Usuario(String username, String nome, String password, Boolean isAdmin) {
        this.username = username;
        this.nome = nome;
        this.password = password;
        this.isAdmin = isAdmin;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.isAdmin){
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_ESTOQUISTA"));
        }else{
            return List.of(new SimpleGrantedAuthority("ROLE_ESTOQUISTA"));
        }   
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
        //TODO 
        ///fazer uma expiração de 5 minutos
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}