package br.com.fabricio.ama.amadesafio.controller;

import br.com.fabricio.ama.amadesafio.dtos.ProdutoRequestDTO;
import br.com.fabricio.ama.amadesafio.exceptions.CategoriaNotFoundException;
import br.com.fabricio.ama.amadesafio.exceptions.ProdutoValidationException;
import br.com.fabricio.ama.amadesafio.exceptions.UsuarioNotFoundException;
import br.com.fabricio.ama.amadesafio.models.Categoria;
import br.com.fabricio.ama.amadesafio.models.Produto;
import br.com.fabricio.ama.amadesafio.models.Usuario;
import br.com.fabricio.ama.amadesafio.repositories.ICategoriaRepositorio;
import br.com.fabricio.ama.amadesafio.repositories.IProdutoRepositorio;
import br.com.fabricio.ama.amadesafio.repositories.IUsuarioRepositorio;
import br.com.fabricio.ama.amadesafio.utils.TipoCategoria;
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
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
        @Parameter(name = "filtroNome", description = "Filtrar por nome do produto", schema = @Schema(title = "nome", type = "string")),
        @Parameter(name = "filtroCategoria", description = "Filtrar por Categoria", schema = @Schema(title = "categoria", type = "string", allowableValues = {"NORMAL", "ESPECIAL", "PERSONALIZADO"})),
        @Parameter(name = "filtroSku", description = "Filtrar código do produto(SKU)", schema = @Schema(title = "sku", type = "string")),
        @Parameter(name = "filtroIcms", description = "Filtrar por valor mínimo ICMS", schema = @Schema(title = "icms", type = "float")),
        @Parameter(name = "filtroCusto", description = "Filtrar por valor mínimo de Custo", schema = @Schema(title = "sku", type = "float")),
        @Parameter(name = "filtroEstoque", description = "Filtrar por valor mínimo em Estoque", schema = @Schema(title = "sku", type = "float")),
        @Parameter(name = "filtroUsuario", description =  "Filtrar por login do usuário que cadastrou o produto", schema = @Schema(title = "usuario", type = "string"))
    })
    @GetMapping
    public ResponseEntity getProdutos(
                            HttpServletRequest request,
                            @RequestParam(name = "filtroNome", required = false) String filtroNome,
                            @RequestParam(name = "filtroCategoria", required = false) String filtroCategoria,
                            @RequestParam(name = "filtroSku", required = false) String filtroSku,
                            @RequestParam(name = "filtroIcms", required = false) Float filtroIcms,
                            @RequestParam(name = "filtroCusto", required = false) Float filtroCusto,
                            @RequestParam(name = "filtroEstoque", required = false) Integer filtroEstoque,
                            @RequestParam(name = "filtroUsuario", required = false) String filtroUsuario
                        ) {
       
        try {
            Optional<Categoria> categoria = null;

            UserDetails usuario = this.usuarioRepositorio.findByUsername(filtroUsuario);

            if(filtroUsuario != null && usuario == null){
                throw new UsuarioNotFoundException("Usuário não cadastrado no sistema");
            } 

            if(filtroCategoria != null){
                TipoCategoria tipoCategoria = TipoCategoria.valueOf(filtroCategoria.toUpperCase());
                categoria = this.categoriaRepositorio.findByTipo(tipoCategoria);
                
                if(categoria.isEmpty()){
                    throw new CategoriaNotFoundException("Categoria não cadastrada no sistema");
                }
            }
            
            List<Produto> produtos = this.produtoRepositorio.findProdutoByCriteria(filtroNome, categoria != null ? categoria.get() : null, filtroSku, filtroIcms, filtroCusto, filtroEstoque, (Usuario) usuario);
            return ResponseEntity.status(200).body(produtos);
        } catch (UsuarioNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (CategoriaNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
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
    @PostMapping
    public ResponseEntity create(@RequestBody ProdutoRequestDTO produto, HttpServletRequest request) {
        try {
            validateProdutoRequest(produto);

            String username = (String) request.getAttribute("username");
            UserDetails usuarioLogado = this.usuarioRepositorio.findByUsername(username);
            Optional<Categoria> categoria = this.categoriaRepositorio.findById(produto.getCategoriaId());

            if(categoria.isEmpty()){
                throw new CategoriaNotFoundException("Categoria não cadastrada no sistema");
            }
            Optional<Produto> produtoEncontrado = this.produtoRepositorio.findBySku(produto.getSku());
            if(produtoEncontrado.isPresent()){
                throw new ProdutoValidationException("O código SKU já foi cadastrado em outro produto");
            }

            Produto novoProduto = new Produto();

            novoProduto.setNome(produto.getNome());
            novoProduto.setSku(produto.getSku());
            novoProduto.setCategoria(categoria.get());
            novoProduto.setValorDeCusto(produto.getValorDeCusto());
            novoProduto.setIcms(produto.getIcms());
            novoProduto.setQuantidadeEmEstoque(produto.getQuantidadeEmEstoque());
            novoProduto.setUsuario((Usuario) usuarioLogado);
            
            var produtoCriado = this.produtoRepositorio.save(novoProduto);

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
    public ResponseEntity update(@PathVariable Integer id, @RequestBody ProdutoRequestDTO produto, HttpServletRequest request){
        try {
            Optional<Produto> produtoExistente = this.produtoRepositorio.findById(id);
            if (produtoExistente.isEmpty()) {
                throw new ProdutoValidationException("Produto não encontrado");
            }

            Produto produtoAtualizado = produtoExistente.get();

            Optional<Produto> produtoEncontrado = this.produtoRepositorio.findBySku(produto.getSku());
            if(produtoEncontrado.isPresent() && produtoEncontrado.get().getId() != produtoAtualizado.getId()){
                throw new ProdutoValidationException("O código SKU já foi cadastrado em outro produto");
            }

            // String username = (String) request.getAttribute("username");
            // UserDetails usuarioLogado = this.usuarioRepositorio.findByUsername(username);
            Optional<Categoria> categoria = null;
            
            if(produto.getCategoriaId() != null){
                categoria = this.categoriaRepositorio.findById(produto.getCategoriaId());

                if(categoria.isPresent()){
                    produtoAtualizado.setCategoria(categoria.get());
                }
            }

            if(produto.getNome() != null && produto.getNome() != ""){
                produtoAtualizado.setNome(produto.getNome());
            }

            if(produto.getSku() != null && produto.getSku() != ""){
                produtoAtualizado.setSku(produto.getSku());
            }

            if(produto.getValorDeCusto() != null){
                produtoAtualizado.setValorDeCusto(produto.getValorDeCusto());
            }

            if(produto.getQuantidadeEmEstoque() != null){
                produtoAtualizado.setQuantidadeEmEstoque(produto.getQuantidadeEmEstoque());
            }

            if(produto.getIcms() != null){
                produtoAtualizado.setIcms(produto.getIcms());
            }
                        
            var updatedProduct = this.produtoRepositorio.save(produtoAtualizado);

            return ResponseEntity.status(HttpStatus.CREATED).body(updatedProduct);
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
            Optional<Produto> produtoExistente = this.produtoRepositorio.findById(id);
            if (produtoExistente.isEmpty()) {
                throw new ProdutoValidationException("Produto não encontrado");
            }

            Produto produtoInativado = produtoExistente.get();

            produtoInativado.setAtivo(false);

            var updatedProduct = this.produtoRepositorio.save(produtoInativado);

            return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
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
            Optional<Produto> produtoExistente = this.produtoRepositorio.findById(id);
            if (produtoExistente.isEmpty()) {
                throw new ProdutoValidationException("Produto não encontrado");
            }

            this.produtoRepositorio.delete(produtoExistente.get());

            return ResponseEntity.status(HttpStatus.OK).body("Produto excluído permanentemente");
        } catch (ProdutoValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro interno no servidor.");
        }
    }
    
    private void validateProdutoRequest(ProdutoRequestDTO produto) {
        if (produto.getIcms() < 0 || produto.getIcms() == null) {
            throw new ProdutoValidationException("ICMS deve ser um valor positivo.");
        }

        if (produto.getValorDeCusto() < 0 || produto.getValorDeCusto() == null) {
            throw new ProdutoValidationException("Valor de custo deve ser um valor positivo.");
        }
        
        if (produto.getQuantidadeEmEstoque() < 0 || produto.getQuantidadeEmEstoque() == null) {
            throw new ProdutoValidationException("Valor de quantidade em estoque deve ser um valor positivo.");
        }

        if (produto.getNome().trim() == "" || produto.getNome() == null) {
            throw new ProdutoValidationException("O campo 'nome' não pode ser nulo ou em branco.");
        }

    }
    
}
