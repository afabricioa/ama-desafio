package br.com.fabricio.ama.amadesafio.utils;

import java.util.Arrays;

public enum CampoRelatorio {
    ID("Id", 1),
    NOME("Nome", 2),
    SKU("SKU", 3),
    CATEGORIA("Categoria Id", 4),
    VALORDECUSTO("Valor de Custo", 5),
    VALORDEVENDA("Valor de Venda", 6),
    ICMS("ICMS", 7),
    ESTOQUE("Quantidade em Estoque", 8);

    private String nomeDoCampo;
    private Integer order;

    CampoRelatorio(String nomeDoCampo, Integer order) {
        this.nomeDoCampo = nomeDoCampo;
        this.order = order;
    }

    public String getNomeDoCampo() {
        return nomeDoCampo;
    }

    public Integer getOrder(){
        return order;
    }

    private static CampoRelatorio getCampoRelatorio(String nomeCampo) {
        return Arrays.stream(CampoRelatorio.values())
                .filter(campo -> campo.name().equalsIgnoreCase(nomeCampo))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Campo não encontrado: " + nomeCampo));
    }
}
