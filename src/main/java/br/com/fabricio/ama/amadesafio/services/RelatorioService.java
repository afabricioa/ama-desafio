package br.com.fabricio.ama.amadesafio.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.fabricio.ama.amadesafio.dtos.CampoRelatorioDTO;

@Service
public class RelatorioService {
    
    public String montaCabecalhoCsv(String campos){
        List<String> listaCampos = this.extraiCampos(campos);
                
        return String.join(",", listaCampos);
    }

    public List<String> extraiCampos(String campos){
        List<String> campoList = List.of(campos.split(", "));
        
        List<CampoRelatorioDTO> camposOrdenados = new ArrayList<>();

        for(String campo : campoList){
            switch (campo) {
                case "ID":
                    camposOrdenados.add(new CampoRelatorioDTO("ID", "ID", 1));
                    break;
                case "NOME":
                    camposOrdenados.add(new CampoRelatorioDTO("Nome", "NOME", 2));
                    break;
                case "SKU":
                    camposOrdenados.add(new CampoRelatorioDTO("Sku", "SKU", 3));
                    break;
                case "CATEGORIA":
                    camposOrdenados.add(new CampoRelatorioDTO("ID Categoria", "CATEGORIA", 4));
                    break;
                case "ICMS":
                    camposOrdenados.add(new CampoRelatorioDTO("Icms", "ICMS", 5));
                    break;
                case "VALORDECUSTO":
                    camposOrdenados.add(new CampoRelatorioDTO("Valor de Custo", "VALORDECUSTO", 6));
                    break;
                case "VALORDEVENDA":
                    camposOrdenados.add(new CampoRelatorioDTO("Valor de Venda", "VALORDEVENDA", 7));
                    break;
                case "ESTOQUE":
                    camposOrdenados.add(new CampoRelatorioDTO("Quantidade em Estoque", "ESTOQUE", 8));
                    break;
                default:
                    break;
            }
        }
        camposOrdenados.sort((a,b) -> a.getOrder() - b.getOrder());
        List<String> lista = camposOrdenados.stream()  
                            .map(CampoRelatorioDTO::getNome)
                            .collect(Collectors.toList());
        return lista;
    }
}
