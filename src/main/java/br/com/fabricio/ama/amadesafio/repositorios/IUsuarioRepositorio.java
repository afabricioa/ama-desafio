package br.com.fabricio.ama.amadesafio.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fabricio.ama.amadesafio.models.Usuario;

public interface IUsuarioRepositorio extends JpaRepository<Usuario, Integer>{
    Usuario findByUsername(String username);
}
