package br.com.fabricio.ama.amadesafio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExcecaoController {
    
    public ResponseEntity handleSessaoExpieradaException(){
        System.out.println("eox");
        return ResponseEntity.status(403).body("O usuário não está autenticado ou a sessão expirou");
    }
}
