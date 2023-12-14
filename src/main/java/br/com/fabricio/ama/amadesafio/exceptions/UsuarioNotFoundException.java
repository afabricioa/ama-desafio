package br.com.fabricio.ama.amadesafio.exceptions;

public class UsuarioNotFoundException extends RuntimeException{
    public UsuarioNotFoundException(String message){
        super(message);
    }
}
