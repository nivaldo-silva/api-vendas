package io.github.nivaldosilva.api_vendas.controller;

import io.github.nivaldosilva.api_vendas.docs.CategoriaDocs;
import io.github.nivaldosilva.api_vendas.dto.CategoriaDTO;
import io.github.nivaldosilva.api_vendas.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
public class CategoriaController implements CategoriaDocs {

    private final CategoriaService categoriaService;

    @Override
    @PostMapping
    public ResponseEntity<CategoriaDTO.CategoriaResponse> cadastrar(
            @Valid @RequestBody CategoriaDTO.CategoriaRequest request) {
        log.info("[CategoriaController] POST /api/v1/categorias - Cadastrar categoria: {}", request.nome());
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.cadastrar(request));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO.CategoriaResponse> buscarPorId(@PathVariable String id) {
        log.info("[CategoriaController] GET /api/v1/categorias/{}", id);
        return ResponseEntity.ok(categoriaService.buscarPorId(id));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<CategoriaDTO.CategoriaResponse>> buscarTodas() {
        log.info("[CategoriaController] GET /api/v1/categorias");
        return ResponseEntity.ok(categoriaService.buscarTodas());
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO.CategoriaResponse> atualizar(
            @PathVariable String id,
            @Valid @RequestBody CategoriaDTO.CategoriaRequest request) {
        log.info("[CategoriaController] PUT /api/v1/categorias/{}", id);
        return ResponseEntity.ok(categoriaService.atualizar(id, request));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        log.info("[CategoriaController] DELETE /api/v1/categorias/{}", id);
        categoriaService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }
}