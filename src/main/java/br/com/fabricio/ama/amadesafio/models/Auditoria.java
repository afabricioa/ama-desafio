package br.com.fabricio.ama.amadesafio.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Auditoria {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    private String objetoAlteado;

    private String acaoRealizada;

    @CreationTimestamp
    private LocalDateTime dataHora;

    private String usuario;

    private String campoAlterado;

    private String valorAntigo;
    
    private String valorAtual;
}
