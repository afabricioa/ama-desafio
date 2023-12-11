package br.com.fabricio.ama.amadesafio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fabricio.ama.amadesafio.models.Categoria;

@Repository
public interface ICategoriaRepositorio extends JpaRepository<Categoria, Integer>{
    
}
