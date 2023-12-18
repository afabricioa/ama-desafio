package br.com.fabricio.ama.amadesafio.dtos;

import lombok.Data;

@Data
public class CampoRelatorioDTO {
    private String nome;

    private String codigo;

    private Integer order;

    public CampoRelatorioDTO(String nome, String codigo, Integer order) {
        this.nome = nome;
        this.codigo = codigo;
        this.order = order;
    }
}
