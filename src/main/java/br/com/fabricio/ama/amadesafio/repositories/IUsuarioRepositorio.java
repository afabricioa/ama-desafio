package br.com.fabricio.ama.amadesafio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.fabricio.ama.amadesafio.models.Usuario;

public interface IUsuarioRepositorio extends JpaRepository<Usuario, Integer>{
    UserDetails findByUsername(String username);
}
