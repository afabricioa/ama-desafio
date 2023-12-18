package br.com.fabricio.ama.amadesafio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fabricio.ama.amadesafio.models.Auditoria;

@Repository
public interface IAuditoriaRepositorio extends JpaRepository<Auditoria, Integer>{
}
