package br.com.fabricio.ama.amadesafio.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import br.com.fabricio.ama.amadesafio.exceptions.SessaoExpiradaException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ExcecaoController {
    
    @ExceptionHandler(SessaoExpiradaException.class)
    public ResponseEntity handleSessaoExpieradaException(SessaoExpiradaException e){
        return ResponseEntity.status(403).body("O usuário não está autenticado ou a sessão expirou");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException excecao, HttpServletRequest request) {
        var servletPath = request.getServletPath();
        String mensagemErro = "";
        if(excecao.getCause() instanceof InvalidFormatException && servletPath.startsWith("/categorias")){
            mensagemErro = "O valor do campo tipo deve ser ESPECIAL, NORMAL ou PERSONALIZADO.";
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensagemErro);
    }
}
