package br.com.fabricio.ama.amadesafio.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fabricio.ama.amadesafio.models.Auditoria;
import br.com.fabricio.ama.amadesafio.repositories.IAuditoriaRepositorio;

@Service
public class AuditoriaService {
    
    @Autowired
    private IAuditoriaRepositorio auditoriaRepositorio;

    public void salvarAuditoria(Auditoria auditoria){
        this.auditoriaRepositorio.save(auditoria);
    }
}
