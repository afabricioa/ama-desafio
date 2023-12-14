package br.com.fabricio.ama.amadesafio.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.fabricio.ama.amadesafio.models.Categoria;
import br.com.fabricio.ama.amadesafio.models.Produto;
import br.com.fabricio.ama.amadesafio.models.Usuario;
@Repository
public interface IProdutoRepositorio extends JpaRepository<Produto, Integer>, JpaSpecificationExecutor<Produto>{   
    Optional<Produto> findBySku(String sku);
    @Query("SELECT p FROM Produto p WHERE " +
       "(:filtroNome IS NULL OR p.nome LIKE %:filtroNome%) " +
       "AND (:filtroCategoria IS NULL OR p.categoria = :filtroCategoria) " +
       "AND (:filtroSku IS NULL OR p.sku LIKE %:filtroSku%) " +
       "AND (:filtroIcms IS NULL OR p.icms >= :filtroIcms) " +
       "AND (:filtroCusto IS NULL OR p.valorDeCusto >= :filtroCusto) " +
       "AND (:filtroEstoque IS NULL OR p.quantidadeEmEstoque >= :filtroEstoque) " +
       "AND (:filtroUsuario IS NULL OR p.usuario = :filtroUsuario)")
        List<Produto> findProdutoByCriteria(
            @Param("filtroNome") String filtroNome,
            @Param("filtroCategoria") Categoria filtroCategoria,
            @Param("filtroSku") String filtroSku,
            @Param("filtroIcms") Float filtroIcms,
            @Param("filtroCusto") Float filtroCusto,
            @Param("filtroEstoque") Integer filtroEstoque,
            @Param("filtroUsuario") Usuario filtroUsuario);
}
    
