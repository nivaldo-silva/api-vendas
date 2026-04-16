package io.github.nivaldosilva.api_vendas.controller;

import io.github.nivaldosilva.api_vendas.docs.VendaDocs; // Importação da nova interface
import io.github.nivaldosilva.api_vendas.dto.VendaDTO;
import io.github.nivaldosilva.api_vendas.service.VendaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/vendas")
@RequiredArgsConstructor
public class VendaController implements VendaDocs {

    private final VendaService vendaService;

    @Override
    @PostMapping
    public ResponseEntity<VendaDTO.VendaResponse> realizarVenda(
            @Valid @RequestBody VendaDTO.VendaRequest request) {
        log.info("[VendaController] POST /api/v1/vendas - Realizar venda para o cliente ID: {}", request.clienteId());
        VendaDTO.VendaResponse response = vendaService.realizarVenda(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<VendaDTO.VendaResponse> buscarPorId(@PathVariable String id) {
        log.info("[VendaController] GET /api/v1/vendas/{}", id);
        return ResponseEntity.ok(vendaService.buscarPorId(id));
    }

    @Override
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<VendaDTO.VendaResponse>> buscarPorCliente(
            @PathVariable String clienteId) {
        log.info("[VendaController] GET /api/v1/vendas/cliente/{}", clienteId);
        return ResponseEntity.ok(vendaService.buscarPorCliente(clienteId));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<VendaDTO.VendaResponse>> buscarTodas() {
        log.info("[VendaController] GET /api/v1/vendas");
        return ResponseEntity.ok(vendaService.buscarTodas());
    }

    @Override
    @PatchMapping("/{id}/finalizar")
    public ResponseEntity<VendaDTO.VendaResponse> finalizarVenda(@PathVariable String id) {
        log.info("[VendaController] PATCH /api/v1/vendas/{}/finalizar", id);
        return ResponseEntity.ok(vendaService.finalizarVenda(id));
    }

    @Override
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarVenda(@PathVariable String id) {
        log.info("[VendaController] PATCH /api/v1/vendas/{}/cancelar", id);
        vendaService.cancelarVenda(id);
        return ResponseEntity.noContent().build();
    }
}