package br.com.fabricio.ama.amadesafio.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ProdutoResponseDTO {
    private String nome;

    private String sku;

    private CategoriaRequestDTO categoria;

    private Float valorDeCusto;

    private Float valorDeVenda;

    private Float icms;

    @Schema(description = "Representação parcial do base64 da Imagem salva")
    private String imagemDoProduto;

    private Integer quantidadeEmEstoque;

    private UsuarioResponseDTO usuario;
}
