package br.com.fabricio.ama.amadesafio.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Produto {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private String nome;

    private Boolean ativo;

    @Column(unique = true)
    private String sku;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    private Float valorDeCusto;

    private Float icms;

    private String imagemDoProduto;

    @CreationTimestamp
    private LocalDateTime dataDeCadastro;
    
    private Integer quantidadeEmEstoque;
}
