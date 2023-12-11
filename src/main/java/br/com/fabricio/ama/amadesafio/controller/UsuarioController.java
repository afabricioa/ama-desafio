package br.com.fabricio.ama.amadesafio.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.fabricio.ama.amadesafio.models.Usuario;
import br.com.fabricio.ama.amadesafio.repositories.IUsuarioRepositorio;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    
    private IUsuarioRepositorio usuarioRepositorio;

    @PostMapping
    public ResponseEntity create(@RequestBody Usuario usuario) {
        var usuariEncontrado = usuarioRepositorio.findByUsername(usuario.getUsername());
        
        if(usuariEncontrado != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe");
        }

        var hashPassword = BCrypt.withDefaults().hashToString(12, usuario.getPassword().toCharArray());

        usuario.setPassword(hashPassword);

        var usuarioCriado = usuarioRepositorio.save(usuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCriado);
    }

    @GetMapping
    public List<Usuario> getAllUsers(){
        return this.usuarioRepositorio.findAll();
    }

}

  