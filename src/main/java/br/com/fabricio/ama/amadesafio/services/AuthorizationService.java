package br.com.fabricio.ama.amadesafio.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.fabricio.ama.amadesafio.repositories.IUsuarioRepositorio;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    IUsuarioRepositorio usuarioRepositorio;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var usuario = usuarioRepositorio.findByUsername(username);
        
        if(usuario == null){
            throw new UsernameNotFoundException("Usuário ou senha incorretos!");
        }
        return usuario;
    }
    
}
