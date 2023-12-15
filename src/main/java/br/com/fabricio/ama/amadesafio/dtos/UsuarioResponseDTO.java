package br.com.fabricio.ama.amadesafio.dtos;

import java.util.List;

import lombok.Data;

@Data
public class UsuarioResponseDTO {
    private String nome;

    private String username;

    private List<String> roles;

    public UsuarioResponseDTO(String nome, String username, List<String> roles) {
        this.nome = nome;
        this.username = username;
        this.roles = roles;
    }
}
