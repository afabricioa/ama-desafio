package br.com.fabricio.ama.amadesafio.exceptions;

public class SessaoExpiradaException extends RuntimeException{
    public SessaoExpiradaException(String mensagem) {
        super(mensagem);
    }
}
