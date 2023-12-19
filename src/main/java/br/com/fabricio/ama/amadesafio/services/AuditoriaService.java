package br.com.fabricio.ama.amadesafio.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fabricio.ama.amadesafio.dtos.AuditoriaHistoricoDTO;
import br.com.fabricio.ama.amadesafio.models.AuditEntity;
import br.com.fabricio.ama.amadesafio.models.Categoria;
import br.com.fabricio.ama.amadesafio.models.Produto;
import jakarta.persistence.EntityManagerFactory;

@Service
public class AuditoriaService {
    
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public List<AuditoriaHistoricoDTO> getHistorico(){
        List<AuditoriaHistoricoDTO> historicoDTOs = new ArrayList<>();
        AuditReader reader = AuditReaderFactory.get(entityManagerFactory.createEntityManager());
        AuditQuery queryHistoryCategoria = reader.createQuery()
                                .forRevisionsOfEntityWithChanges(Categoria.class, 
                                true);

        List<?> historicoCategoria = queryHistoryCategoria.getResultList();

        AuditQuery queryHistoryProduto = reader.createQuery()
                                .forRevisionsOfEntityWithChanges(Produto.class, 
                                true);

        List<?> historicoProduto = queryHistoryProduto.getResultList();
        
        for(Object item : historicoCategoria){
            historicoDTOs.add(extraiHistorico(item, "Categoria"));
        }   

        for(Object item : historicoProduto){
            historicoDTOs.add(extraiHistorico(item, "Produto"));
        }   
        historicoDTOs.sort((a,b) -> a.getId() - b.getId());
        return historicoDTOs;
    }

    public AuditoriaHistoricoDTO extraiHistorico(Object item, String objetoAuditado){
        SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        AuditEntity auditRevisionEntity = (AuditEntity) ((Object[]) item)[1];
        RevisionType revisionType = (RevisionType) ((Object[]) item)[2];
        
        String tipo = "Inclusão";
        if(revisionType.name() == "MOD"){
            tipo = "Alteração";
        } else if(revisionType.name() == "DEL"){
            tipo = "Exclusão";
        }
        String dataHoraFormatada = formatador.format(auditRevisionEntity.getRevisionDate());

        return new AuditoriaHistoricoDTO(auditRevisionEntity.getId(), 
                objetoAuditado, 
                tipo, 
                dataHoraFormatada, 
                auditRevisionEntity.getUsuario());
    }
}
