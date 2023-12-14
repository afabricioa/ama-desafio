package br.com.fabricio.ama.amadesafio.controller;

import br.com.fabricio.ama.amadesafio.dtos.CategoriaRequestDTO;
import br.com.fabricio.ama.amadesafio.exceptions.CategoriaNotFoundException;
import br.com.fabricio.ama.amadesafio.models.Categoria;
import br.com.fabricio.ama.amadesafio.repositories.ICategoriaRepositorio;
import br.com.fabricio.ama.amadesafio.utils.TipoCategoria;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Optional;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;


@RestController
@RequestMapping("/categorias")
@Tag(name = "Categoria Controller")
public class CategoriaController {
    
    @Autowired
    private ICategoriaRepositorio categoriaRepositorio;

    @Operation(
        security = {@SecurityRequirement(name = "bearer-key")}, 
        summary = "Buscar Categorias",
        method = "GET"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso!", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Requisição incorreta", content = @Content()),
        @ApiResponse(responseCode = "401", description = "Requisição não autorizada!", content = @Content()),       
        @ApiResponse(responseCode = "500", description = "Ocorreu um erro interno, verifique os logs!", content = @Content()),
    })
    @GetMapping
    public ResponseEntity getCategorias(HttpServletRequest request) {
        List<Categoria> categorias = this.categoriaRepositorio.findAll();
        return ResponseEntity.status(200).body(categorias);
    }
    @Operation(
        security = {@SecurityRequirement(name = "bearer-key")}, 
        summary = "Criar Categoria",
        method = "POST"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Categoria Criada com Sucesso!", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Requisição incorreta", content = @Content()),
        @ApiResponse(responseCode = "401", description = "Requisição não autorizada!", content = @Content()),
        @ApiResponse(responseCode = "500", description = "Ocorreu um erro interno, verifique os logs!", content = @Content()),
    })
    @PostMapping
    public ResponseEntity create(@RequestBody Categoria categoria, HttpServletRequest request) {
        // Verifica se o nome é nulo ou em branco
        if (categoria.getNome() == null || categoria.getNome().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O nome da categoria não pode ser nulo ou em branco");
        }

        // Verifica se o tipo é nulo
        if (categoria.getTipo() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O tipo da categoria não pode ser nulo");
        }

        var categoriaCriada = this.categoriaRepositorio.save(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaCriada);
    }   

    @Operation(
        security = {@SecurityRequirement(name = "bearer-key")}, 
        summary = "Atualizar Categoria",
        method = "POST"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Categoria Atualizada com Sucesso!", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Requisição incorreta", content = @Content()),
        @ApiResponse(responseCode = "401", description = "Requisição não autorizada!", content = @Content()),
        @ApiResponse(responseCode = "500", description = "Ocorreu um erro interno, verifique os logs!", content = @Content()),
    })
    @PutMapping("/{id}")
    public ResponseEntity atualizarCategoria(@PathVariable Integer id, @RequestBody Categoria categoria, HttpServletRequest request) {
        try {
            Optional<Categoria> categoriaExistente = this.categoriaRepositorio.findById(id);

            if(categoriaExistente.isEmpty()){
                throw new CategoriaNotFoundException("Categoria não cadastrada no sistema");
            }

            if (categoria.getTipo() != null) {
                categoriaExistente.get().setTipo(categoria.getTipo());
            }

            if(categoria.getNome() != null && categoria.getNome() != ""){
                categoriaExistente.get().setNome(categoria.getNome());
            }

            var categoriaAtualizada = this.categoriaRepositorio.save(categoriaExistente.get());

            return ResponseEntity.status(HttpStatus.CREATED).body(categoriaAtualizada);
        } catch (CategoriaNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro interno no servidor.");
        }
    }

    @Operation(
        security = {@SecurityRequirement(name = "bearer-key")}, 
        summary = "Excluir Categoria",
        method = "DELETE"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Categoria Excluída com Sucesso!", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Requisição incorreta", content = @Content()),
        @ApiResponse(responseCode = "401", description = "Requisição não autorizada!", content = @Content()),
        @ApiResponse(responseCode = "500", description = "Ocorreu um erro interno, verifique os logs!", content = @Content()),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity deletarCategoria(@PathVariable Integer id, HttpServletRequest request){
        try{
            Optional<Categoria> categoria = this.categoriaRepositorio.findById(id);

            if(categoria.isEmpty()){
                throw new CategoriaNotFoundException("Categoria não cadastrada no sistema");
            }

            this.categoriaRepositorio.delete(categoria.get());

            return ResponseEntity.status(HttpStatus.OK).body("Categoria excluída permanentemente");
        } catch (CategoriaNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getCause() + " " + e.getMessage());
            if (e.getCause() instanceof ConstraintViolationException) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ocorreu uma violação na integridade dos dados.\nA categoria possui produtos cadastrados, verifique a exclusão dos produtos primeiro.");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro interno no servidor.");
        }
    }
}
