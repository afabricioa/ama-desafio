package br.com.fabricio.ama.amadesafio.listeners;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import br.com.fabricio.ama.amadesafio.models.Auditoria;
import br.com.fabricio.ama.amadesafio.models.Usuario;
import br.com.fabricio.ama.amadesafio.services.AuditoriaService;
import br.com.fabricio.ama.amadesafio.services.BeanService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

@Component
public class AuditoriaEntityListener {
    
    @Autowired
    AuditoriaService auditoriaService;

    @PrePersist
    public void auditarInclusao(Object objeto) {
        auditar(objeto, "Inclusão");
    }

    @PreUpdate
    public void auditarAlteracao(Object objeto){
        auditar(objeto, "Alteração");
    }

    @PreRemove
    public void auditarRemocao(Object objeto){
        auditar(objeto, "Remoção");
    }

    public String getUsuarioLogado(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Usuario usuario = (Usuario) authentication.getPrincipal();

        return usuario.getNome();
    }

    private void auditar(Object objeto, String acao) {
        String usuarioLogado = getUsuarioLogado();
        // Implemente a lógica para obter informações relevantes do objeto, como campos alterados, valores antigos e atuais.
        Auditoria auditoria = new Auditoria();
        System.out.println(objeto.getClass());
        System.out.println(objeto);
        auditoria.setObjetoAlteado(objeto.getClass().getSimpleName());
        auditoria.setAcaoRealizada(acao);
        auditoria.setDataHora(LocalDateTime.now());
        auditoria.setUsuario(usuarioLogado);

        EntityManager entityManager = BeanService.getBean(EntityManager.class);
        entityManager.persist(auditoria);
    }
}
