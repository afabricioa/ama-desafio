package br.com.fabricio.ama.amadesafio.dtos;

import lombok.Data;

@Data
public class CampoAlteradoDTO {
    private String campo;

    private String valorAnterior;

    private String valorAtual;
}
