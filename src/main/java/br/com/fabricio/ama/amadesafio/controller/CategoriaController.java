package br.com.fabricio.ama.amadesafio.controller;

import br.com.fabricio.ama.amadesafio.models.Categoria;
import br.com.fabricio.ama.amadesafio.repositories.ICategoriaRepositorio;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/categorias")
public class CategoriaController {
    
    @Autowired
    private ICategoriaRepositorio categoriaRepositorio;

    @GetMapping
    public ResponseEntity getCategorias(HttpServletRequest request) {
        System.out.println("produ");

        List<Categoria> categorias = this.categoriaRepositorio.findAll();
        return ResponseEntity.status(200).body(categorias);
    }

    @PostMapping
    public ResponseEntity create(@RequestBody Categoria categoria, HttpServletRequest request) {

        var caetegoriaCriada = this.categoriaRepositorio.save(categoria);

        return ResponseEntity.status(HttpStatus.CREATED).body(caetegoriaCriada);
    }
    
    
}
