package br.com.fabricio.ama.amadesafio.exceptions;

public class ProdutoValidationException extends RuntimeException{
    public ProdutoValidationException(String message){
        super(message);
    }
}
