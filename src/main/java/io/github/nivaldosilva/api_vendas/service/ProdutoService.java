package io.github.nivaldosilva.api_vendas.service;

import io.github.nivaldosilva.api_vendas.domain.Produto;
import io.github.nivaldosilva.api_vendas.dto.ProdutoDTO;
import io.github.nivaldosilva.api_vendas.exception.ConflictException;
import io.github.nivaldosilva.api_vendas.exception.NotFoundException;
import io.github.nivaldosilva.api_vendas.mapper.ProdutoMapper;
import io.github.nivaldosilva.api_vendas.repository.CategoriaRepository;
import io.github.nivaldosilva.api_vendas.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProdutoDTO.ProdutoResponse cadastrar(ProdutoDTO.ProdutoRequest request) {
        log.info("[ProdutoService] Iniciando cadastro do produto: {}", request.nome());

        if (produtoRepository.findByCodigoDeBarras(request.codigoDeBarras()).isPresent()) {
            log.warn("[ProdutoService] Conflito: código de barras '{}' já cadastrado", request.codigoDeBarras());
            throw new ConflictException("Código de barras já cadastrado: " + request.codigoDeBarras());
        }

        validarCategorias(request.categorias());
        Produto produto = ProdutoMapper.toEntity(request);
        produtoRepository.save(produto);

        log.info("[ProdutoService] Produto cadastrado com sucesso. ID: {}", produto.getId());
        return ProdutoMapper.toResponse(produto);
    }

    public ProdutoDTO.ProdutoResponse buscarPorId(String id) {
        log.info("[ProdutoService] Buscando produto por ID: {}", id);

        return produtoRepository.findById(id)
                .map(p -> {
                    log.info("[ProdutoService] Produto encontrado: {}", p.getNome());
                    return ProdutoMapper.toResponse(p);
                })
                .orElseThrow(() -> {
                    log.warn("[ProdutoService] Produto não encontrado com ID: {}", id);
                    return new NotFoundException("Produto não encontrado com id: " + id);
                });
    }

    public List<ProdutoDTO.ProdutoResponse> buscarTodos() {
        log.info("[ProdutoService] Buscando todos os produtos");

        List<Produto> produtos = produtoRepository.findAll();

        if (produtos.isEmpty()) {
            log.warn("[ProdutoService] Nenhum produto cadastrado no sistema");
            throw new NotFoundException("Nenhum produto cadastrado no sistema.");
        }

        log.info("[ProdutoService] {} produto(s) encontrado(s)", produtos.size());
        return produtos.stream()
                .map(ProdutoMapper::toResponse)
                .toList();
    }

    public List<ProdutoDTO.ProdutoResponse> buscarPorCategoria(String categoriaId) {
        log.info("[ProdutoService] Buscando produtos da categoria ID: {}", categoriaId);

        if (!categoriaRepository.existsById(categoriaId)) {
            log.warn("[ProdutoService] Categoria não encontrada com ID: {}", categoriaId);
            throw new NotFoundException("Categoria não encontrada com id: " + categoriaId);
        }

        List<Produto> produtos = produtoRepository.findByCategoriasContaining(categoriaId);

        log.info("[ProdutoService] {} produto(s) encontrado(s) para a categoria ID: {}", produtos.size(), categoriaId);
        return produtos.stream()
                .map(ProdutoMapper::toResponse)
                .toList();
    }

    public ProdutoDTO.ProdutoResponse atualizar(String id, ProdutoDTO.ProdutoRequest request) {
        log.info("[ProdutoService] Iniciando atualização do produto ID: {}", id);

        Produto produtoExistente = produtoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("[ProdutoService] Produto não encontrado para atualização. ID: {}", id);
                    return new NotFoundException("Não é possível atualizar. Produto não encontrado com id: " + id);
                });

        produtoRepository.findByCodigoDeBarras(request.codigoDeBarras())
                .filter(p -> !p.getId().equals(id))
                .ifPresent(p -> {
                    log.warn("[ProdutoService] Conflito: código de barras '{}' já pertence a outro produto", request.codigoDeBarras());
                    throw new ConflictException("O código de barras " + request.codigoDeBarras() + " já pertence a outro produto.");
                });

        validarCategorias(request.categorias());
        Integer estoqueAtualizado = request.estoque() != null ? request.estoque() : produtoExistente.getEstoque();

        Produto produtoAtualizado = Produto.builder()
                .id(id)
                .nome(request.nome())
                .descricao(request.descricao())
                .categorias(request.categorias())
                .codigoDeBarras(request.codigoDeBarras())
                .preco(request.preco())
                .estoque(estoqueAtualizado)
                .build();

        produtoRepository.save(produtoAtualizado);

        log.info("[ProdutoService] Produto atualizado com sucesso. ID: {}", id);
        return ProdutoMapper.toResponse(produtoAtualizado);
    }

    public void deletarPorId(String id) {
        log.info("[ProdutoService] Iniciando exclusão do produto ID: {}", id);

        if (!produtoRepository.existsById(id)) {
            log.warn("[ProdutoService] Produto não encontrado para exclusão. ID: {}", id);
            throw new NotFoundException("Falha ao deletar. Produto não encontrado com id: " + id);
        }
        produtoRepository.deleteById(id);
        log.info("[ProdutoService] Produto deletado com sucesso. ID: {}", id);
    }

    private void validarCategorias(List<String> categoriaIds) {
        log.info("[ProdutoService] Validando {} categoria(s) informada(s)", categoriaIds.size());

        List<String> inexistentes = categoriaIds.stream()
                .filter(catId -> !categoriaRepository.existsById(catId))
                .toList();

        if (!inexistentes.isEmpty()) {
            log.warn("[ProdutoService] Categoria(s) não encontrada(s): {}", inexistentes);
            throw new NotFoundException("Categoria(s) não encontrada(s): " + inexistentes);
        }
    }
}