package br.com.fabricio.ama.amadesafio.dtos;

import lombok.Data;

@Data
public class ValorAgregadoDTO {

    private Integer id;

    private String nome;

    private Float valor;

    private Float valorDeVenda;

    private Float valorTotal;

    private Float valorPrevisto;

    private Integer quantidadeEmEstoque;

    private UsuarioResponseDTO usuario;

    public ValorAgregadoDTO(Integer id, String nome, Float valor, Float valorDeVenda, Float valorTotal, Float valorPrevisto, Integer quantidadeEmEstoque,
            UsuarioResponseDTO usuario) {
        this.id = id;
        this.nome = nome;
        this.valor = valor;
        this.valorDeVenda = valorDeVenda;
        this.valorTotal = valorTotal;
        this.valorPrevisto = valorPrevisto;
        this.quantidadeEmEstoque = quantidadeEmEstoque;
        this.usuario = usuario;
    }
}
