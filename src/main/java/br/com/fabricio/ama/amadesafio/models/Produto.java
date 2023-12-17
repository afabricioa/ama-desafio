package br.com.fabricio.ama.amadesafio.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.Specification;

import br.com.fabricio.ama.amadesafio.dtos.CategoriaRequestDTO;
import br.com.fabricio.ama.amadesafio.dtos.ProdutoResponseDTO;
import br.com.fabricio.ama.amadesafio.dtos.UsuarioResponseDTO;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Produto {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Hidden
    private Integer id;

    @NotBlank
    private String nome;

    @Hidden
    private Boolean ativo = true;

    @NotNull
    @Column(unique = true)
    private String sku;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    private Float valorDeCusto = (float) 0;

    private Float valorDeVenda = (float) 0;

    private Float icms = (float) 0;

    @Lob
    @Column(length = 1048576)
    private byte[] imagemDoProduto;

    @CreationTimestamp
    @Hidden
    private LocalDateTime dataDeCadastro;
    
    private Integer quantidadeEmEstoque = 0;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public static Specification<Produto> filtros(String nome, String sku, Integer categoriaId, Float valorDeCusto, Float icms, Float estoque){
        return new Specification<Produto>(){
            @Override
            public Predicate toPredicate(
                    Root<Produto> root,
                    CriteriaQuery<?> query, 
                    CriteriaBuilder criteriaBuilder) {
                
                List<Predicate> predicates = new ArrayList<>();

                if(nome != "" && nome != null ){
                    predicates.add(criteriaBuilder.like(root.get("nome"), "%" + nome + "%"));
                }

                if(sku != "" && sku != null ){
                    predicates.add(criteriaBuilder.like(root.get("sku"), "%" + sku + "%"));
                }

                if(categoriaId != null ){
                    predicates.add(criteriaBuilder.equal(root.get("categoria").get("id"), categoriaId));
                }

                if(valorDeCusto != null ){
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("valorDeCusto"), valorDeCusto));
                }


                if(icms != null ){
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("icms"), icms));
                }

                if(estoque != null ){
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("quantidadeEmEstoque"), estoque));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

    public ProdutoResponseDTO toDto(){
        ProdutoResponseDTO produtoDTO = new ProdutoResponseDTO();
        produtoDTO.setId(id);
        produtoDTO.setCategoria(new CategoriaRequestDTO(categoria.getId(), categoria.getNome(), categoria.getTipo()));
        produtoDTO.setIcms(icms);
        produtoDTO.setNome(nome);
        produtoDTO.setQuantidadeEmEstoque(quantidadeEmEstoque);
        produtoDTO.setSku(sku);
        produtoDTO.setValorDeCusto(valorDeCusto);
        produtoDTO.setValorDeVenda(valorDeVenda);
        produtoDTO.setImagemDoProduto(imagemDoProduto != null ? 
                                    imagemDoProduto.toString().substring(0, 5) : 
                                    null);        
        produtoDTO.setUsuario(new UsuarioResponseDTO(usuario));
        
        return produtoDTO;
    }
}
