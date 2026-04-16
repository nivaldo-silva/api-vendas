package io.github.nivaldosilva.api_vendas.service;

import io.github.nivaldosilva.api_vendas.domain.Produto;
import io.github.nivaldosilva.api_vendas.domain.Venda;
import io.github.nivaldosilva.api_vendas.dto.VendaDTO;
import io.github.nivaldosilva.api_vendas.exception.NotFoundException;
import io.github.nivaldosilva.api_vendas.mapper.VendaMapper;
import io.github.nivaldosilva.api_vendas.repository.ClienteRepository;
import io.github.nivaldosilva.api_vendas.repository.ProdutoRepository;
import io.github.nivaldosilva.api_vendas.repository.VendaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    public VendaDTO.VendaResponse realizarVenda(VendaDTO.VendaRequest request) {
        log.info("[VendaService] Iniciando venda para o cliente ID: {}", request.clienteId());


        if (!clienteRepository.existsById(request.clienteId())) {
            throw new NotFoundException("Cliente não encontrado com id: " + request.clienteId());
        }


        Set<String> produtoIds = request.items().stream()
                .map(VendaDTO.ItemVendaRequest::produtoId)
                .collect(Collectors.toSet());

        Map<String, Produto> produtosNoBanco = produtoRepository.findAllById(produtoIds)
                .stream()
                .collect(Collectors.toMap(Produto::getId, p -> p));


        produtoIds.forEach(id -> {
            if (!produtosNoBanco.containsKey(id)) {
                throw new NotFoundException("Produto não encontrado com id: " + id);
            }
        });


        Venda venda = VendaMapper.toEntity(request, produtosNoBanco);


        venda.calcularValorTotal();


        baixarEstoque(venda, produtosNoBanco);

        vendaRepository.save(venda);

        log.info("[VendaService] Venda realizada com sucesso. ID: {}", venda.getId());
        return VendaMapper.toResponse(venda);
    }

    private void baixarEstoque(Venda venda, Map<String, Produto> produtosNoBanco) {
        log.info("[VendaService] Baixando estoque para {} itens", venda.getItems().size());
        venda.getItems().forEach(item -> {
            Produto produto = produtosNoBanco.get(item.getProdutoId());
            produto.baixarEstoque(item.getQuantidade());
            produtoRepository.save(produto);
            log.debug("[VendaService] Estoque baixado para produto {}: {}", produto.getNome(), produto.getEstoque());
        });
    }

    public VendaDTO.VendaResponse buscarPorId(String id) {
        log.info("[VendaService] Buscando venda por ID: {}", id);
        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("[VendaService] Venda não encontrada. ID: {}", id);
                    return new NotFoundException("Venda não encontrada com id: " + id);
                });
        return VendaMapper.toResponse(venda);
    }

    public List<VendaDTO.VendaResponse> buscarPorCliente(String clienteId) {
        log.info("[VendaService] Buscando vendas do cliente ID: {}", clienteId);

        if (!clienteRepository.existsById(clienteId)) {
            throw new NotFoundException("Cliente não encontrado com id: " + clienteId);
        }

        List<Venda> vendas = vendaRepository.findByClienteId(clienteId);
        log.info("[VendaService] {} venda(s) encontrada(s) para o cliente ID: {}", vendas.size(), clienteId);

        return vendas.stream()
                .map(VendaMapper::toResponse)
                .toList();
    }

    public List<VendaDTO.VendaResponse> buscarTodas() {
        log.info("[VendaService] Buscando todas as vendas");

        List<Venda> vendas = vendaRepository.findAll();

        log.info("[VendaService] {} venda(s) encontrada(s)", vendas.size());
        return vendas.stream()
                .map(VendaMapper::toResponse)
                .toList();
    }

    public VendaDTO.VendaResponse finalizarVenda(String id) {
        log.info("[VendaService] Finalizando venda ID: {}", id);

        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Venda não encontrada com id: " + id));

        venda.calcularValorTotal();
        venda.finalizarVenda();
        vendaRepository.save(venda);

        log.info("[VendaService] Venda finalizada com sucesso. ID: {}. Valor total: {}", id, venda.getValorTotal());
        return VendaMapper.toResponse(venda);
    }

    public void cancelarVenda(String id) {
        log.info("[VendaService] Cancelando venda ID: {}", id);

        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Venda não encontrada com id: " + id));

        venda.cancelar();
        vendaRepository.save(venda);

        log.info("[VendaService] Venda cancelada com sucesso. ID: {}", id);
    }
}