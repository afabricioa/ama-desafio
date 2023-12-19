package br.com.fabricio.ama.amadesafio.dtos;

import lombok.Data;

@Data
public class AuditoriaHistoricoDTO {
    private Integer id;

    private String objeto;

    private String tipo;

    private String data;

    private String nome;

    public AuditoriaHistoricoDTO(Integer id, String objeto, String tipo, String data, String nome) {
        this.id = id;
        this.objeto = objeto;
        this.tipo = tipo;
        this.data = data;
        this.nome = nome;
    }
}
