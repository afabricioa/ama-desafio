package br.com.fabricio.ama.amadesafio.models;

import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import br.com.fabricio.ama.amadesafio.listeners.AuditListener;
import jakarta.persistence.Entity;

@Entity
@RevisionEntity(AuditListener.class)
public class AuditEntity extends DefaultRevisionEntity{
    private String usuario;

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
}
