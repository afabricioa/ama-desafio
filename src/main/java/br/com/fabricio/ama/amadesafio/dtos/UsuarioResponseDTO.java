package br.com.fabricio.ama.amadesafio.dtos;

import java.util.ArrayList;
import java.util.List;

import br.com.fabricio.ama.amadesafio.models.Usuario;
import lombok.Data;

@Data
public class UsuarioResponseDTO {
    private String nome;

    private String username;

    private List<String> roles;

    public UsuarioResponseDTO(Usuario usuario) {
        List<String> roles = new ArrayList<>();
        if(usuario.getIsAdmin()){
            roles.add("ADMIN");
            roles.add("ESTOQUISTA");
        } 
        else {
             roles.add("ESTOQUISTA");
        }
        this.nome = usuario.getNome();
        this.username = usuario.getUsername();
        this.roles = roles;
    }
}
