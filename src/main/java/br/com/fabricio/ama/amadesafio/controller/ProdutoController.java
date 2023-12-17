package br.com.fabricio.ama.amadesafio.controller;

import br.com.fabricio.ama.amadesafio.dtos.*;
import br.com.fabricio.ama.amadesafio.exceptions.*;
import br.com.fabricio.ama.amadesafio.models.Usuario;
import br.com.fabricio.ama.amadesafio.repositories.*;
import br.com.fabricio.ama.amadesafio.services.FilterCamposService;
import br.com.fabricio.ama.amadesafio.services.ProdutoService;

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

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/produtos")
@Tag(name = "ProdutoController")
public class ProdutoController {
    
    @Autowired
    private IProdutoRepositorio produtoRepositorio;

    @Autowired
    private IUsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ICategoriaRepositorio categoriaRepositorio;

    @Autowired
    ProdutoService produtoService;

    @Operation(
        security = {@SecurityRequirement(name = "bearer-key")}, 
        summary = "Buscar Produtos",
        method = "GET"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso!", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Requisição incorreta", content = @Content()),
        @ApiResponse(responseCode = "401", description = "Requisição não autorizada!", content = @Content()),       
        @ApiResponse(responseCode = "500", description = "Ocorreu um erro interno, verifique os logs!", content = @Content()),
    })
    @Parameters({
        @Parameter(name = "nome", description = "Filtrar por nome do produto", schema = @Schema(title = "nome", type = "string")),
        @Parameter(name = "filtroCategoria", description = "Filtrar por Categoria", schema = @Schema(title = "categoria", type = "string", allowableValues = {"NORMAL", "ESPECIAL", "PERSONALIZADO"})),
        @Parameter(name = "sku", description = "Filtrar código do produto(SKU)", schema = @Schema(title = "sku", type = "string")),
        @Parameter(name = "icms", description = "Filtrar por valor mínimo ICMS", schema = @Schema(title = "icms", type = "float")),
        @Parameter(name = "valorDeCusto", description = "Filtrar por valor mínimo de Custo", schema = @Schema(title = "valorDeCusto", type = "float")),
        @Parameter(name = "valorDeVenda", description = "Filtrar por valor mínimo de Venda", schema = @Schema(title = "valorDeVenda", type = "float")),
        @Parameter(name = "quantidadeEmEstoque", description = "Filtrar por valor mínimo em Estoque", schema = @Schema(title = "quantidadeEmEstoque", type = "float")),
        @Parameter(name = "filtroUsuario", description =  "Filtrar por login do usuário que cadastrou o produto", schema = @Schema(title = "usuario", type = "string"))
    })
    @GetMapping
    public ResponseEntity getProdutos(
                            HttpServletRequest request,
                            @RequestParam(name = "nome", required = false) String filtroNome,
                            @RequestParam(name = "filtroCategoria", required = false) String filtroCategoria,
                            @RequestParam(name = "sku", required = false) String filtroSku,
                            @RequestParam(name = "icms", required = false) Float filtroIcms,
                            @RequestParam(name = "valorDeCusto", required = false) Float filtroCusto,
                            @RequestParam(name = "valorDeVenda", required = false) Float filtroVenda,
                            @RequestParam(name = "quantidadeEmEstoque", required = false) Integer filtroEstoque,
                            @RequestParam(name = "filtroUsuario", required = false) String filtroUsuario,
                            Pageable pageable
                        ) {
       
        try {
            String username = (String) request.getAttribute("username");
            Usuario usuarioLogado = (Usuario) this.usuarioRepositorio.findByUsername(username);

            List<ProdutoResponseDTO> produtosDTO = produtoService.buscarProdutos(
                                                    filtroUsuario, 
                                                    filtroCategoria, 
                                                    filtroNome, 
                                                    filtroSku, 
                                                    filtroIcms, 
                                                    filtroCusto, 
                                                    filtroEstoque, 
                                                    pageable);
            
            Set<String> campos = Set.of();
            
            if(!usuarioLogado.getIsAdmin()){
                campos = produtoService.getCamposBloqueados();
            }

            MappingJacksonValue jacksonValue = FilterCamposService.filtroCampos(
                                                campos, 
                                                "produtoFilter", 
                                                produtosDTO);
            
            return ResponseEntity.status(200).body(jacksonValue);

        } catch (UsuarioNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (CategoriaNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getCause() + " " + e.getMessage());
            if(e.getCause() instanceof IllegalArgumentException){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Escolha um atributo de ordenação correto (nome, sku, valorDeCusto, icms, quantidadeEmEstoque).");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro interno no servidor.");
        }
    }

    @Operation(
        security = {@SecurityRequirement(name = "bearer-key")}, 
        summary = "Criar Produto",
        method = "POST"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Produto Criado com Sucesso!", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Requisição incorreta", content = @Content()),
        @ApiResponse(responseCode = "401", description = "Requisição não autorizada!", content = @Content()),
        @ApiResponse(responseCode = "500", description = "Ocorreu um erro interno, verifique os logs!", content = @Content()),
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity create(@ModelAttribute ProdutoRequestDTO produto, HttpServletRequest request) {
        try {
            String username = (String) request.getAttribute("username");

            var produtoCriado = produtoService.cadastrarProduto(produto, username);

            return ResponseEntity.status(HttpStatus.CREATED).body(produtoCriado);
        } catch (ProdutoValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (CategoriaNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro interno no servidor.");
        }
    }

    @Operation(
        security = {@SecurityRequirement(name = "bearer-key")}, 
        summary = "Atualizar Produto",
        description = "Preencha apenas os campos que deseja atualizar e apague o resto",
        method = "PUT"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Produto Atualizado com Sucesso!", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Requisição incorreta", content = @Content()),
        @ApiResponse(responseCode = "401", description = "Requisição não autorizada!", content = @Content()),
        @ApiResponse(responseCode = "500", description = "Ocorreu um erro interno, verifique os logs!", content = @Content()),
    })
    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Integer id, @RequestBody ProdutoUpdateRequestDTO produto, HttpServletRequest request){
        try {
            String username = (String) request.getAttribute("username");

            var produtoAtualizado = produtoService.atualizarProduto(id, produto, username);

            return ResponseEntity.status(HttpStatus.CREATED).body(produtoAtualizado);
        } catch (ProdutoValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (CategoriaNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro interno no servidor.");
        }
    }
    @Operation(
        security = {@SecurityRequirement(name = "bearer-key")}, 
        summary = "Inativar Produto",
        method = "PATCH"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Produto Desativado com Sucesso!", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Requisição incorreta", content = @Content()),
        @ApiResponse(responseCode = "401", description = "Requisição não autorizada!", content = @Content()),
        @ApiResponse(responseCode = "500", description = "Ocorreu um erro interno, verifique os logs!", content = @Content()),
    })
    @PatchMapping("/{id}")
    public ResponseEntity inativarProduto(@PathVariable Integer id, HttpServletRequest request){
        try{
            var produtoAtualizado = produtoService.inativarProduto(id);

            return ResponseEntity.status(HttpStatus.OK).body(produtoAtualizado);
        } catch (ProdutoValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro interno no servidor.");
        }
    }
    @Operation(
        security = {@SecurityRequirement(name = "bearer-key")}, 
        summary = "Excluir Produto",
        method = "DELETE"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Produto Excluí com Sucesso!", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Requisição incorreta", content = @Content()),
        @ApiResponse(responseCode = "401", description = "Requisição não autorizada!", content = @Content()),
        @ApiResponse(responseCode = "500", description = "Ocorreu um erro interno, verifique os logs!", content = @Content()),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity deletarProduto(@PathVariable Integer id, HttpServletRequest request){
        try{
            produtoService.deletarProduto(id);

            return ResponseEntity.status(HttpStatus.OK).body("Produto excluído permanentemente");
        } catch (ProdutoValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro interno no servidor.");
        }
    }

    @Operation(
        security = {@SecurityRequirement(name = "bearer-key")}, 
        summary = "Buscar Valor Agregado",
        method = "GET"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso!", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Requisição incorreta", content = @Content()),
        @ApiResponse(responseCode = "401", description = "Requisição não autorizada!", content = @Content()),       
        @ApiResponse(responseCode = "500", description = "Ocorreu um erro interno, verifique os logs!", content = @Content()),
    })
    @Parameters({
        @Parameter(name = "nome", description = "Filtrar por nome do produto", schema = @Schema(title = "nome", type = "string")),
        @Parameter(name = "filtroCategoria", description = "Filtrar por Categoria", schema = @Schema(title = "categoria", type = "string", allowableValues = {"NORMAL", "ESPECIAL", "PERSONALIZADO"})),
        @Parameter(name = "sku", description = "Filtrar código do produto(SKU)", schema = @Schema(title = "sku", type = "string")),
        @Parameter(name = "icms", description = "Filtrar por valor mínimo ICMS", schema = @Schema(title = "icms", type = "float")),
        @Parameter(name = "valorDeCusto", description = "Filtrar por valor mínimo de Custo", schema = @Schema(title = "valorDeCusto", type = "float")),
        @Parameter(name = "valorDeVenda", description = "Filtrar por valor mínimo de Venda", schema = @Schema(title = "valorDeVenda", type = "float")),
        @Parameter(name = "quantidadeEmEstoque", description = "Filtrar por valor mínimo em Estoque", schema = @Schema(title = "quantidadeEmEstoque", type = "float")),
        @Parameter(name = "filtroUsuario", description =  "Filtrar por login do usuário que cadastrou o produto", schema = @Schema(title = "usuario", type = "string"))
    })
    @GetMapping("/getValorAgregado")
    public ResponseEntity getValorAgregado(
                            HttpServletRequest request,
                            @RequestParam(name = "nome", required = false) String filtroNome,
                            @RequestParam(name = "filtroCategoria", required = false) String filtroCategoria,
                            @RequestParam(name = "sku", required = false) String filtroSku,
                            @RequestParam(name = "icms", required = false) Float filtroIcms,
                            @RequestParam(name = "valorDeCusto", required = false) Float filtroCusto,
                            @RequestParam(name = "valorDeVenda", required = false) Float filtroVenda,
                            @RequestParam(name = "quantidadeEmEstoque", required = false) Integer filtroEstoque,
                            @RequestParam(name = "filtroUsuario", required = false) String filtroUsuario,
                            Pageable pageable
                        ) {
       
        try {
            List<ValorAgregadoDTO> produtoValorAgregadoDTO = produtoService.buscaValoresAgregados(
                                                    filtroUsuario, 
                                                    filtroCategoria, 
                                                    filtroNome, 
                                                    filtroSku, 
                                                    filtroIcms, 
                                                    filtroCusto, 
                                                    filtroEstoque, 
                                                    pageable);

            return ResponseEntity.status(200).body(produtoValorAgregadoDTO);

        } catch (UsuarioNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (CategoriaNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getCause() + " " + e.getMessage());
            if(e.getCause() instanceof IllegalArgumentException){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Escolha um atributo de ordenação correto (nome, sku, valorDeCusto, icms, quantidadeEmEstoque).");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro interno no servidor.");
        }
    }

    @Operation(
        security = {@SecurityRequirement(name = "bearer-key")}, 
        summary = "Gerencia Visualização de campos do Produto",
        method = "POST"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesso!", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Requisição incorreta", content = @Content()),
        @ApiResponse(responseCode = "401", description = "Requisição não autorizada!", content = @Content()),       
        @ApiResponse(responseCode = "500", description = "Ocorreu um erro interno, verifique os logs!", content = @Content()),
    })
    @Parameters({
        @Parameter(name = "campos", description = "Digite os campos nesse formato 'icms, valorDeCusto' separado por vírgula.", schema = @Schema(title = "campos", type = "string")),
    })
    @PostMapping("/gerenciaVisualizacaoProduto")
    public ResponseEntity gerenciaVisualizacaoProduto(HttpServletRequest request, @RequestParam(name = "campos", required = false) String campos) {
        try {
            String username = (String) request.getAttribute("username");
            Usuario usuarioLogado = (Usuario) this.usuarioRepositorio.findByUsername(username);

            if(!usuarioLogado.getIsAdmin()){
                throw new ProdutoValidationException("O usuário não tem permissão para realizar essa requisição");
            }

            produtoService.gerenciarCamposBloqueados(campos);

            return ResponseEntity.status(200).body("Visualização alterada com sucesso");
        } catch (ProdutoValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (UsuarioNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getCause() + " " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro interno no servidor.");
        }
    }
}
