package br.com.fabricio.ama.amadesafio.dtos;

import br.com.fabricio.ama.amadesafio.utils.TipoCategoria;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class CategoriaRequestDTO {
    private Integer id;
    
    private String nome;

    @Enumerated(EnumType.STRING)
    private TipoCategoria tipo;

    public CategoriaRequestDTO(Integer id, String nome, TipoCategoria tipo) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
    }
}
