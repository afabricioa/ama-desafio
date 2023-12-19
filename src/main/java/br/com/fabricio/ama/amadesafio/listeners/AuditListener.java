package br.com.fabricio.ama.amadesafio.listeners;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.fabricio.ama.amadesafio.models.AuditEntity;
import br.com.fabricio.ama.amadesafio.models.Usuario;

public class AuditListener implements RevisionListener{

    @Override
    public void newRevision(Object revisionEntity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();
        
        AuditEntity auditEntity = (AuditEntity) revisionEntity;
        auditEntity.setUsuario(usuario.getNome());
    }
    
}
