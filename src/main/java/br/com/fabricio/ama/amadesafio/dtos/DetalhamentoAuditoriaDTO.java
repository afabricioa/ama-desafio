package br.com.fabricio.ama.amadesafio.dtos;

import java.util.List;

import lombok.Data;

@Data
public class DetalhamentoAuditoriaDTO {
    private Integer id;
    private List<CampoAlteradoDTO> camposAlterados;

    public DetalhamentoAuditoriaDTO(Integer id, List<CampoAlteradoDTO> camposAlterados) {
        this.id = id;
        this.camposAlterados = camposAlterados;
    }
}
