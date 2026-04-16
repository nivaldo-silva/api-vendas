package io.github.nivaldosilva.api_vendas.mapper;

import io.github.nivaldosilva.api_vendas.domain.Produto;
import io.github.nivaldosilva.api_vendas.domain.Venda;
import io.github.nivaldosilva.api_vendas.domain.vo.ItemVenda;
import io.github.nivaldosilva.api_vendas.dto.VendaDTO;
import io.github.nivaldosilva.api_vendas.enums.StatusVenda;
import lombok.experimental.UtilityClass;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@UtilityClass
public class VendaMapper {

    public static Venda toEntity(VendaDTO.VendaRequest request, Map<String, Produto> produtosNoBanco) {
        if (request == null) return null;

        List<ItemVenda> items = request.items().stream()
                .map(itemReq -> {
                    Produto produto = produtosNoBanco.get(itemReq.produtoId());
                    BigDecimal precoTotal = ItemVenda.calcularPrecoTotal(produto.getPreco(), itemReq.quantidade());

                    return ItemVenda.builder()
                            .produtoId(itemReq.produtoId())
                            .nome(produto.getNome())
                            .preco(produto.getPreco())
                            .quantidade(itemReq.quantidade())
                            .precoTotal(precoTotal)
                            .build();
                })
                .toList();

        return Venda.builder()
                .clienteId(request.clienteId())
                .data(LocalDateTime.now())
                .items(items)
                .status(StatusVenda.PENDENTE)
                .build();
    }

    public static VendaDTO.VendaResponse toResponse(Venda entity) {
        if (entity == null) return null;

        List<VendaDTO.ItemVendaResponse> items = entity.getItems() == null
                ? Collections.emptyList()
                : entity.getItems().stream()
                .map(VendaMapper::toItemResponse)
                .toList();

        return VendaDTO.VendaResponse.builder()
                .id(entity.getId())
                .clienteId(entity.getClienteId())
                .data(entity.getData())
                .valorTotal(entity.getValorTotal())
                .status(entity.getStatus())
                .items(items)
                .build();
    }

    private static VendaDTO.ItemVendaResponse toItemResponse(ItemVenda entity) {
        return VendaDTO.ItemVendaResponse.builder()
                .produtoId(entity.getProdutoId())
                .nome(entity.getNome())
                .preco(entity.getPreco())
                .quantidade(entity.getQuantidade())
                .precoTotal(entity.getPrecoTotal())
                .build();
    }
}