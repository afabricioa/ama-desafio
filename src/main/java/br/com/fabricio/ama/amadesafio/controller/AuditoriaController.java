package br.com.fabricio.ama.amadesafio.controller;

import org.springframework.web.bind.annotation.RestController;

import br.com.fabricio.ama.amadesafio.dtos.AuditoriaHistoricoDTO;
import br.com.fabricio.ama.amadesafio.services.AuditoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/auditoria")
public class AuditoriaController {
    
    @Autowired
    AuditoriaService auditoriaService;

    @Operation(
        security = {@SecurityRequirement(name = "bearer-key")}, 
        summary = "Buscar Histório Auditoria",
        method = "GET"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso!", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Requisição incorreta", content = @Content()),
        @ApiResponse(responseCode = "401", description = "Requisição não autorizada!", content = @Content()),       
        @ApiResponse(responseCode = "500", description = "Ocorreu um erro interno, verifique os logs!", content = @Content()),
    })
    @GetMapping("/getHistorico")
    public ResponseEntity getHistorico(HttpServletRequest request) {
        List<AuditoriaHistoricoDTO> historico = auditoriaService.getHistorico();
        return ResponseEntity.status(200).body(historico);
    }
}
