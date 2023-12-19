package br.com.fabricio.ama.amadesafio.dtos;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ProdutoRequestDTO {
  
    private String nome;

    private String sku;

    private Integer categoriaId;

    private Float valorDeCusto;

    private Float valorDeVenda;

    private Float icms;

    private MultipartFile imagemDoProduto = null;

    private Integer quantidadeEmEstoque;
}
