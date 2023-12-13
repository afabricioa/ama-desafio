package br.com.fabricio.ama.amadesafio.exceptions;

public class CategoriaNotFoundException extends RuntimeException{
    public CategoriaNotFoundException(String message){
        super(message);
    }
}
