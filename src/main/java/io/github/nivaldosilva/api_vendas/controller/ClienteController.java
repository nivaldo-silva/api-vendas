package io.github.nivaldosilva.api_vendas.controller;

import io.github.nivaldosilva.api_vendas.docs.ClienteDocs;
import io.github.nivaldosilva.api_vendas.dto.ClienteDTO;
import io.github.nivaldosilva.api_vendas.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController implements ClienteDocs {

    private final ClienteService clienteService;

    @Override
    @PostMapping
    public ResponseEntity<ClienteDTO.ClienteResponse> cadastrar(
            @Valid @RequestBody ClienteDTO.ClienteRequest request) {
        log.info("[ClienteController] POST /api/v1/clientes - Cadastrar cliente. CPF: {}", request.cpf());
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.cadastrar(request));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO.ClienteResponse> buscarPorId(@PathVariable String id) {
        log.info("[ClienteController] GET /api/v1/clientes/{}", id);
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @Override
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<ClienteDTO.ClienteResponse> buscarPorCpf(@PathVariable String cpf) {
        log.info("[ClienteController] GET /api/v1/clientes/cpf/{}", cpf);
        return ResponseEntity.ok(clienteService.buscarPorCpf(cpf));
    }

    @Override
    @GetMapping("/buscar")
    public ResponseEntity<List<ClienteDTO.ClienteResponse>> buscarPorNome(@RequestParam String nome) {
        log.info("[ClienteController] GET /api/v1/clientes/buscar?nome={}", nome);
        return ResponseEntity.ok(clienteService.buscarPorNome(nome));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<ClienteDTO.ClienteResponse>> buscarTodos() {
        log.info("[ClienteController] GET /api/v1/clientes");
        return ResponseEntity.ok(clienteService.buscarTodos());
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO.ClienteResponse> atualizar(
            @PathVariable String id,
            @Valid @RequestBody ClienteDTO.ClienteRequest request) {
        log.info("[ClienteController] PUT /api/v1/clientes/{}", id);
        return ResponseEntity.ok(clienteService.atualizar(id, request));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        log.info("[ClienteController] DELETE /api/v1/clientes/{}", id);
        clienteService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }
}