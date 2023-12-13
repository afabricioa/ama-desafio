package br.com.fabricio.ama.amadesafio.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.Specification;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    private Float icms = (float) 0;

    private String imagemDoProduto;

    @CreationTimestamp
    @Hidden
    private LocalDateTime dataDeCadastro;
    
    private Integer quantidadeEmEstoque = 0;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public static Specification<Produto> filterByNome(String nome){
        return (root, query, builder) -> builder.like(root.get("nome"), "%" + nome + "%");
    }

    public static Specification<Produto> filterByCategoria(Integer categoriaId){
        return (root, query, builder) -> builder.equal(root.get("categoria").get("id"), categoriaId);
    }

    public static Specification<Produto> filterBySku(String sku){
        return (root, query, builder) -> builder.like(root.get("sku"), "%" + sku + "%");
    }

    public static Specification<Produto> filterByValorCusto(Float valorDeCusto){
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("valorDeCusto"), valorDeCusto);
    }

    public static Specification<Produto> filterByIcms(Float icms){
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("icms"), icms);
    }

    public static Specification<Produto> filterByValorEstoque(Float estoque){
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("estoque"), estoque);
    }
}
