package br.com.fabricio.ama.amadesafio.models;

import br.com.fabricio.ama.amadesafio.listeners.AuditoriaEntityListener;
import br.com.fabricio.ama.amadesafio.utils.TipoCategoria;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@EntityListeners(AuditoriaEntityListener.class)
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Hidden
    private Integer id;

    @NotBlank
    private String nome;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoCategoria tipo;
}
