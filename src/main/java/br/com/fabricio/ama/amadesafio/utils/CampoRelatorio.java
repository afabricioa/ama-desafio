package br.com.fabricio.ama.amadesafio.utils;

public enum CampoRelatorio {
    ID("Id"),
    NOME("Nome"),
    SKU("SKU"),
    CATEGORIA("Categoria Id"),
    VALORDECUSTO("Valor de Custo"),
    VALORDEVENDA("Valor de Venda"),
    ICMS("ICMS"),
    ESTOQUE("Quantidade em Estoque");

    private final String nomeDoCampo;

    CampoRelatorio(String nomeDoCampo) {
        this.nomeDoCampo = nomeDoCampo;
    }

    public String getNomeDoCampo() {
        return nomeDoCampo;
    }
}
