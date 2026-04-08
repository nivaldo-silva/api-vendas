package io.github.nivaldosilva.api_vendas.mapper;

import io.github.nivaldosilva.api_vendas.domain.Categoria;
import io.github.nivaldosilva.api_vendas.dto.CategoriaDTO;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CategoriaMapper {

    public static Categoria toEntity(CategoriaDTO.CategoriaRequest request) {
        if (request == null) return null;

        return Categoria.builder()
                .nome(request.nome())
                .descricao(request.descricao())
                .build();
    }

    public static CategoriaDTO.CategoriaResponse toResponse(Categoria entity) {
        if (entity == null) return null;

        return CategoriaDTO.CategoriaResponse.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .descricao(entity.getDescricao())
                .build();
    }
}
