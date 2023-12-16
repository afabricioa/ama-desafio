package br.com.fabricio.ama.amadesafio.dtos;

import lombok.Data;

@Data
public class ProdutoUpdateRequestDTO {
    private String nome;

    private String sku;

    private Integer categoriaId;

    private Float valorDeCusto;

    private Float valorDeVenda;

    private Float icms;

    private Integer quantidadeEmEstoque;
}
