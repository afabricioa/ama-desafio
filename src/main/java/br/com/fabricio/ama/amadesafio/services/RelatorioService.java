package br.com.fabricio.ama.amadesafio.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class RelatorioService {
    
    public String montaCabecalhoCsv(String campos){
        String[] fields = campos.split(", ");
        List<String> campoList = List.of(campos.split(", "));
        List<String> listaCampos = new ArrayList<>();
        for(String campo : campoList){
            switch (campo) {
                case "ID":
                    listaCampos.add("ID");
                    break;
                case "NOME":
                    listaCampos.add("Nome");
                    break;
                case "SKU":
                    listaCampos.add("Sku");
                    break;
                case "CATEGORIA":
                    listaCampos.add("ID Categoria");
                    break;
                case "ICMS":
                    listaCampos.add("Icms");
                    break;
                case "VALORDECUSTO":
                    listaCampos.add("Valor de Custo");
                    break;
                case "VALORDEVENDA":
                    listaCampos.add("Valor de Venda");
                    break;
                case "ESTOQUE":
                    listaCampos.add("Quantidade em Estoque");
                    break;
                default:
                    break;
            }
        }
        System.out.println(listaCampos);
        return String.join(",", listaCampos);
    }
}
