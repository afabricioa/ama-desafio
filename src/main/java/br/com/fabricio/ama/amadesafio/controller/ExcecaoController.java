package br.com.fabricio.ama.amadesafio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.fabricio.ama.amadesafio.exceptions.SessaoExpiradaException;

@ControllerAdvice
public class ExcecaoController {
    
    @ExceptionHandler(SessaoExpiradaException.class)
    public ResponseEntity handleSessaoExpieradaException(SessaoExpiradaException e){
        System.out.println("eox");
        return ResponseEntity.status(403).body("O usuário não está autenticado ou a sessão expirou");
    }
}
