package io.github.nivaldosilva.api_vendas.controller;

import io.github.nivaldosilva.api_vendas.docs.ProdutoDocs;
import io.github.nivaldosilva.api_vendas.dto.ProdutoDTO;
import io.github.nivaldosilva.api_vendas.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/produtos")
@RequiredArgsConstructor
public class ProdutoController implements ProdutoDocs {

    private final ProdutoService produtoService;

    @Override
    @PostMapping
    public ResponseEntity<ProdutoDTO.ProdutoResponse> cadastrar(
            @Valid @RequestBody ProdutoDTO.ProdutoRequest request) {
        log.info("[ProdutoController] POST /api/v1/produtos - Cadastrar produto: {}", request.nome());
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.cadastrar(request));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO.ProdutoResponse> buscarPorId(@PathVariable String id) {
        log.info("[ProdutoController] GET /api/v1/produtos/{}", id);
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<ProdutoDTO.ProdutoResponse>> buscarTodos() {
        log.info("[ProdutoController] GET /api/v1/produtos");
        return ResponseEntity.ok(produtoService.buscarTodos());
    }

    @Override
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ProdutoDTO.ProdutoResponse>> buscarPorCategoria(
            @PathVariable String categoriaId) {
        log.info("[ProdutoController] GET /api/v1/produtos/categoria/{}", categoriaId);
        return ResponseEntity.ok(produtoService.buscarPorCategoria(categoriaId));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDTO.ProdutoResponse> atualizar(
            @PathVariable String id,
            @Valid @RequestBody ProdutoDTO.ProdutoRequest request) {
        log.info("[ProdutoController] PUT /api/v1/produtos/{}", id);
        return ResponseEntity.ok(produtoService.atualizar(id, request));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        log.info("[ProdutoController] DELETE /api/v1/produtos/{}", id);
        produtoService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }
}