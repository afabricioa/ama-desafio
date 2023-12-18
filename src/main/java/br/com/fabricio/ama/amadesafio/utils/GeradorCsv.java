package br.com.fabricio.ama.amadesafio.utils;

import java.util.List;

import br.com.fabricio.ama.amadesafio.dtos.ProdutoResponseDTO;

public class GeradorCsv {

    public static String generateCsv(List<ProdutoResponseDTO> produtos, String headerCSV, String campos) {
        StringBuilder csvContent = new StringBuilder();
        csvContent.append(headerCSV.concat("\n"));
        List<String> campoList = List.of(campos.split(", "));
        
        for (ProdutoResponseDTO produto : produtos) {
            if(campoList.contains("ID")){
                csvContent.append(produto.getId()).append(",");
            }
            if(campoList.contains("NOME")) {
                csvContent.append(produto.getNome()).append(",");
            }
            if(campoList.contains("SKU")) {
                csvContent.append(produto.getSku()).append(",");
            }
            if(campoList.contains("CATEGORIA")) {
                csvContent.append(produto.getCategoria().getId()).append(",");
            }
            if(campoList.contains("ICMS")) {
                csvContent.append(produto.getIcms()).append(",");
            }
            if(campoList.contains("VALORDECUSTO")) {
                csvContent.append(produto.getValorDeCusto()).append(",");
            }
            if(campoList.contains("VALORDEVENDA")) {
                csvContent.append(produto.getValorDeVenda()).append(",");
            }
            if(campoList.contains("ESTOQUE")) {
                csvContent.append(produto.getQuantidadeEmEstoque()).append(",");
            }
            csvContent.append("\n");
        }

        return csvContent.toString();
    }
}
