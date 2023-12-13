package br.com.fabricio.ama.amadesafio.repositories;

import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fabricio.ama.amadesafio.models.Produto;
@Repository
public interface IProdutoRepositorio extends JpaRepository<Produto, Integer>{   
    Optional<Produto> findBySku(String sku);
}
    
