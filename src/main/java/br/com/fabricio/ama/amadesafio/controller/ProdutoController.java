package br.com.fabricio.ama.amadesafio.controller;

import br.com.fabricio.ama.amadesafio.models.Produto;
import br.com.fabricio.ama.amadesafio.repositories.IProdutoRepositorio;
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
@RequestMapping("/produtos")
public class ProdutoController {
    
    @Autowired
    private IProdutoRepositorio produtoRepositorio;

    @GetMapping
    public ResponseEntity getProdutos(HttpServletRequest request) {
        System.out.println("produ");

        List<Produto> produtos = this.produtoRepositorio.findAll();
        return ResponseEntity.status(200).body(produtos);
    }

    @PostMapping
    public ResponseEntity create(@RequestBody Produto produto, HttpServletRequest request) {

        var produtoCriado = this.produtoRepositorio.save(produto);

        return ResponseEntity.status(HttpStatus.CREATED).body(produtoCriado);
    }
    
    
}
