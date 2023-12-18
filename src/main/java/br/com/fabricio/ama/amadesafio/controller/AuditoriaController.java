package br.com.fabricio.ama.amadesafio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fabricio.ama.amadesafio.services.AuditoriaService;

@RestController
@RequestMapping("/auditoria")
public class AuditoriaController {
    
    @Autowired
    AuditoriaService auditoriaService;
}
