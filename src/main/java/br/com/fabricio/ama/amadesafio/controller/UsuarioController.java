package br.com.fabricio.ama.amadesafio.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fabricio.ama.amadesafio.models.Usuario;
import br.com.fabricio.ama.amadesafio.repositories.IUsuarioRepositorio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    
    @Autowired
    private IUsuarioRepositorio usuarioRepositorio;

    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @GetMapping
    public List<Usuario> getAllUsers(){
        System.out.println("uuarios");
        return this.usuarioRepositorio.findAll();
    }

}

  