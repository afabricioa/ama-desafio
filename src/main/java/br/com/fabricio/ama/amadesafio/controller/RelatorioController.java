package br.com.fabricio.ama.amadesafio.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fabricio.ama.amadesafio.dtos.ProdutoResponseDTO;
import br.com.fabricio.ama.amadesafio.models.Produto;
import br.com.fabricio.ama.amadesafio.services.ProdutoService;
import br.com.fabricio.ama.amadesafio.services.RelatorioService;
import br.com.fabricio.ama.amadesafio.utils.GeradorCsv;
import br.com.fabricio.ama.amadesafio.utils.GeradorXlsx;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/relatorio")
@Tag(name = "RelatorioController")
public class RelatorioController {

    @Autowired
    ProdutoService produtoService;

    @Autowired
    RelatorioService relatorioService;
    
    @Operation(
        security = {@SecurityRequirement(name = "bearer-key")}, 
        summary = "Gerar Relatório de Produtos",
        method = "GET"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso!", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Requisição incorreta", content = @Content()),
        @ApiResponse(responseCode = "401", description = "Requisição não autorizada!", content = @Content()),       
        @ApiResponse(responseCode = "500", description = "Ocorreu um erro interno, verifique os logs!", content = @Content()),
    })
    @Parameters({
        @Parameter(name = "campos", description = "Digite os campos nesse formato 'icms, valorDeCusto' separado por vírgula.", schema = @Schema(title = "campos", type = "string")),
        @Parameter(name = "tipoArquivo", description = "Selecione o tipo de arquivo.", schema = @Schema(title = "tipoArquivo", type = "string", allowableValues = {"CSV", "XLSX"})),
        @Parameter(name = "nome", description = "Filtrar por nome do produto", schema = @Schema(title = "nome", type = "string")),
        @Parameter(name = "filtroCategoria", description = "Filtrar por Categoria", schema = @Schema(title = "categoria", type = "string", allowableValues = {"NORMAL", "ESPECIAL", "PERSONALIZADO"})),
        @Parameter(name = "sku", description = "Filtrar código do produto(SKU)", schema = @Schema(title = "sku", type = "string")),
        @Parameter(name = "icms", description = "Filtrar por valor mínimo ICMS", schema = @Schema(title = "icms", type = "float")),
        @Parameter(name = "valorDeCusto", description = "Filtrar por valor mínimo de Custo", schema = @Schema(title = "valorDeCusto", type = "float")),
        @Parameter(name = "valorDeVenda", description = "Filtrar por valor mínimo de Venda", schema = @Schema(title = "valorDeVenda", type = "float")),
        @Parameter(name = "quantidadeEmEstoque", description = "Filtrar por valor mínimo em Estoque", schema = @Schema(title = "quantidadeEmEstoque", type = "float")),
        @Parameter(name = "filtroUsuario", description =  "Filtrar por login do usuário que cadastrou o produto", schema = @Schema(title = "usuario", type = "string"))
    })
    @GetMapping("/gerarRelatorio")
    public ResponseEntity gerarRelatorio(HttpServletRequest request,
                            HttpServletResponse response,
                            @RequestParam(name = "campos", required = false) String campos,
                            @RequestParam(name = "tipoArquivo", required = true, defaultValue = "CSV") String tipoArquivo,
                            @RequestParam(name = "nome", required = false) String filtroNome,
                            @RequestParam(name = "filtroCategoria", required = false) String filtroCategoria,
                            @RequestParam(name = "sku", required = false) String filtroSku,
                            @RequestParam(name = "icms", required = false) Float filtroIcms,
                            @RequestParam(name = "valorDeCusto", required = false) Float filtroCusto,
                            @RequestParam(name = "valorDeVenda", required = false) Float filtroVenda,
                            @RequestParam(name = "quantidadeEmEstoque", required = false) Integer filtroEstoque,
                            @RequestParam(name = "filtroUsuario", required = false) String filtroUsuario,
                            Pageable pageable) {

        List<ProdutoResponseDTO> produtos = produtoService.buscarProdutos(filtroUsuario, filtroCategoria, filtroNome, filtroSku, filtroIcms, filtroCusto, filtroEstoque, pageable);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        
        if(tipoArquivo.equals("XLSX")){
            response.setContentType("application/octet-stream");

            GeradorXlsx geradorXlsx = new GeradorXlsx(produtos);

            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=produtos.xlsx";
            response.setHeader(headerKey, headerValue);

            try {
                geradorXlsx.gerarXlsxFile(response);
                return ResponseEntity.status(200).body("Reatório Gerado!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(tipoArquivo.equals("CSV")) {
            headers.setContentDispositionFormData("attachment", "produtos.csv");
            String headerCsv = relatorioService.montaCabecalhoCsv(campos);
            
            String csvBytes = GeradorCsv.generateCsv(produtos, headerCsv, campos);
            return ResponseEntity.status(200).headers(headers).body(csvBytes);
        }

        return ResponseEntity.status(500).headers(headers).body("Aconteceu um erro na geração do relatório.");
    }
    
}
