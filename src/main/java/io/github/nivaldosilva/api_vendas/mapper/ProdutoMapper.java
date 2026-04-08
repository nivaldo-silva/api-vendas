package io.github.nivaldosilva.api_vendas.mapper;

import io.github.nivaldosilva.api_vendas.domain.Produto;
import io.github.nivaldosilva.api_vendas.dto.ProdutoDTO;
import lombok.experimental.UtilityClass;
import java.util.ArrayList;
import java.util.Optional;

@UtilityClass
public class ProdutoMapper {

    public static Produto toEntity(ProdutoDTO.ProdutoRequest request) {
        if (request == null) return null;

        return Produto.builder()
                .nome(request.nome())
                .descricao(request.descricao())
                .categorias(Optional.ofNullable(request.categorias()).orElse(new ArrayList<>()))
                .codigoDeBarras(request.codigoDeBarras())
                .estoque(request.estoque())
                .build();
    }

    public static ProdutoDTO.ProdutoResponse toResponse(Produto entity) {
        if (entity == null) return null;

        return ProdutoDTO.ProdutoResponse.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .descricao(entity.getDescricao())
                .categorias(entity.getCategorias())
                .codigoDeBarras(entity.getCodigoDeBarras())
                .estoque(entity.getEstoque())
                .build();
    }
}