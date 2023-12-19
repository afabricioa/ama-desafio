package br.com.fabricio.ama.amadesafio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import br.com.fabricio.ama.amadesafio.models.Usuario;

@Repository
public interface IUsuarioRepositorio extends JpaRepository<Usuario, Integer>{
    UserDetails findByUsername(String username);
}
