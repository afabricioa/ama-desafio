package br.com.fabricio.ama.amadesafio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fabricio.ama.amadesafio.models.Categoria;
import java.util.Optional;


@Repository
public interface ICategoriaRepositorio extends JpaRepository<Categoria, Integer>{
    Optional<Categoria> findById(Integer id);
    Optional<Categoria> findByTipo(String filtroCategoria);
}
