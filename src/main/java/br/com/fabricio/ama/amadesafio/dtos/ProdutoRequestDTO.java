package br.com.fabricio.ama.amadesafio.dtos;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ProdutoRequestDTO {
  
    private String nome;

    private String sku;

    private Integer categoriaId;

    private Float valorDeCusto = 0.0f;

    private Float valorDeVenda = 0.0f;

    private Float icms = 0.0f;

    private MultipartFile imagemDoProduto;

    private Integer quantidadeEmEstoque = 0;
}
