package br.com.fabricio.ama.amadesafio.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import br.com.fabricio.ama.amadesafio.dtos.ProdutoRequestDTO;
import br.com.fabricio.ama.amadesafio.dtos.ProdutoResponseDTO;
import br.com.fabricio.ama.amadesafio.dtos.ProdutoUpdateRequestDTO;
import br.com.fabricio.ama.amadesafio.dtos.UsuarioResponseDTO;
import br.com.fabricio.ama.amadesafio.dtos.ValorAgregadoDTO;
import br.com.fabricio.ama.amadesafio.exceptions.CategoriaNotFoundException;
import br.com.fabricio.ama.amadesafio.exceptions.ProdutoValidationException;
import br.com.fabricio.ama.amadesafio.exceptions.UsuarioNotFoundException;
import br.com.fabricio.ama.amadesafio.models.Categoria;
import br.com.fabricio.ama.amadesafio.models.Produto;
import br.com.fabricio.ama.amadesafio.models.Usuario;
import br.com.fabricio.ama.amadesafio.repositories.*;
import br.com.fabricio.ama.amadesafio.utils.TipoCategoria;

@Service
public class ProdutoService {
    
    @Autowired
    private IProdutoRepositorio produtoRepositorio;

    @Autowired
    private IUsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ICategoriaRepositorio categoriaRepositorio;

    Set<String> camposBloqueados = Set.of();

    public List<ProdutoResponseDTO> buscarProdutos(String filtroUsuario, String filtroCategoria, String filtroNome, String filtroSku, Float filtroIcms, Float filtroCusto, Integer filtroEstoque, Pageable pageable){
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
        
        List<Produto> produtos = this.produtoRepositorio.findProdutoByCriteria(filtroNome, categoria != null ? categoria.get() : null, filtroSku, filtroIcms, filtroCusto, filtroEstoque, (Usuario) usuario, pageable);
        List<ProdutoResponseDTO> produtosDTO = new ArrayList<>();
        produtos.forEach(produto -> {
            produtosDTO.add(produto.toDto());
        });
        System.out.println("produtos " + produtosDTO);
        return produtosDTO;
    }

    public Produto cadastrarProduto(ProdutoRequestDTO produto, String username) throws IOException{
        this.validateProdutoRequest(produto);
        System.out.println(produto);
        UserDetails usuarioLogado = this.usuarioRepositorio.findByUsername(username);
        Optional<Categoria> categoria = this.categoriaRepositorio.findById(produto.getCategoriaId());

        if(categoria.isEmpty()){
            throw new CategoriaNotFoundException("Categoria não cadastrada no sistema");
        }
        Optional<Produto> produtoEncontrado = this.produtoRepositorio.findBySku(produto.getSku());
        if(produtoEncontrado.isPresent()){
            throw new ProdutoValidationException("O código SKU já foi cadastrado em outro produto");
        }

        if(produto.getValorDeVenda() < produto.getValorDeCusto()){
            throw new ProdutoValidationException("O valor de Custo não pode ser maior que o valor de Venda.");
        }

        Produto novoProduto = new Produto();

        novoProduto.setNome(produto.getNome());
        novoProduto.setSku(produto.getSku());
        novoProduto.setCategoria(categoria.get());
        novoProduto.setValorDeCusto(produto.getValorDeCusto());
        novoProduto.setValorDeVenda(produto.getValorDeVenda());
        novoProduto.setIcms(produto.getIcms());
        novoProduto.setQuantidadeEmEstoque(produto.getQuantidadeEmEstoque());
        novoProduto.setUsuario((Usuario) usuarioLogado);
        if(produto.getImagemDoProduto() != null){
            novoProduto.setImagemDoProduto(produto.getImagemDoProduto().getBytes());
        }
        
        return this.produtoRepositorio.save(novoProduto);
    }

    public Produto atualizarProduto(Integer id, ProdutoUpdateRequestDTO produto, String username){
        Optional<Produto> produtoExistente = this.produtoRepositorio.findById(id);
        if (produtoExistente.isEmpty()) {
            throw new ProdutoValidationException("Produto não encontrado");
        }

        Produto produtoAtualizado = produtoExistente.get();
        Optional<Produto> produtoEncontrado = this.produtoRepositorio.findBySku(produto.getSku());
        if(produtoEncontrado.isPresent() && produtoEncontrado.get().getId() != produtoAtualizado.getId()){
            throw new ProdutoValidationException("O código SKU já foi cadastrado em outro produto");
        }

        Usuario usuarioLogado = (Usuario) this.usuarioRepositorio.findByUsername(username);
        if(!usuarioLogado.getIsAdmin() && (produto.getValorDeCusto() != 0.0 || produto.getIcms() != 0.0)){
            throw new ProdutoValidationException("O usuário autenticado não possui permissão para alterar o Valor do Custo ou ICMS.");
        }

        Optional<Categoria> categoria = null;
        
        if(produto.getCategoriaId() != null){
            categoria = this.categoriaRepositorio.findById(produto.getCategoriaId());

            if(categoria.isPresent()){
                produtoAtualizado.setCategoria(categoria.get());
            }
        }

        if(StringUtils.isNotBlank(produto.getNome())){
            produtoAtualizado.setNome(produto.getNome());
        }

        if(StringUtils.isNotBlank(produto.getSku())){
            produtoAtualizado.setSku(produto.getSku());
        }

        if(produto.getValorDeCusto() != null){
            produtoAtualizado.setValorDeCusto(produto.getValorDeCusto());
        }

        if(produto.getValorDeVenda() != null){
            produtoAtualizado.setValorDeVenda(produto.getValorDeVenda());
        }

        if(produto.getQuantidadeEmEstoque() != null){
            produtoAtualizado.setQuantidadeEmEstoque(produto.getQuantidadeEmEstoque());
        }

        if(produto.getIcms() != null){
            produtoAtualizado.setIcms(produto.getIcms());
        }

        return this.produtoRepositorio.save(produtoAtualizado);
    }

    public Produto inativarProduto(Integer id){
        Optional<Produto> produtoExistente = this.produtoRepositorio.findById(id);
        if (produtoExistente.isEmpty()) {
            throw new ProdutoValidationException("Produto não encontrado");
        }

        Produto produtoInativado = produtoExistente.get();

        produtoInativado.setAtivo(false);

        return this.produtoRepositorio.save(produtoInativado);
    }

    public void deletarProduto(Integer id){
        Optional<Produto> produtoExistente = this.produtoRepositorio.findById(id);
        if (produtoExistente.isEmpty()) {
            throw new ProdutoValidationException("Produto não encontrado");
        }

        this.produtoRepositorio.delete(produtoExistente.get());
    }

    public List<ValorAgregadoDTO> buscaValoresAgregados(String filtroUsuario, String filtroCategoria, String filtroNome, String filtroSku, Float filtroIcms, Float filtroCusto, Integer filtroEstoque, Pageable pageable){
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
        
        List<Produto> produtos = this.produtoRepositorio.findProdutoByCriteria(filtroNome, categoria != null ? categoria.get() : null, filtroSku, filtroIcms, filtroCusto, filtroEstoque, (Usuario) usuario, pageable);
        List<ValorAgregadoDTO> produtoValorAgregadoDTO = new ArrayList<>();
        produtos.forEach(produto -> {
            UsuarioResponseDTO usuarioResponseDTO = new UsuarioResponseDTO(produto.getUsuario());
            produtoValorAgregadoDTO.add(new ValorAgregadoDTO(
                produto.getId(),
                produto.getNome(),
                produto.getValorDeCusto(),
                produto.getValorDeVenda(),
                produto.getValorDeCusto() * produto.getQuantidadeEmEstoque(),
                produto.getValorDeVenda() * produto.getQuantidadeEmEstoque(),
                produto.getQuantidadeEmEstoque(),
                usuarioResponseDTO
            ));
        });

        return produtoValorAgregadoDTO;
    }

    public void gerenciarCamposBloqueados(String campos){
        if(campos != null){
            String[] fields = campos.split(", ");
            camposBloqueados = new HashSet<>(List.of(fields));
        } else {
            camposBloqueados = Set.of();
        }
        
    }

    public Set<String> getCamposBloqueados(){
        if(camposBloqueados.size() > 0){
            return camposBloqueados;
        } else {
            return Set.of();
        }
    }

    public void validateProdutoRequest(ProdutoRequestDTO produto) {
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
