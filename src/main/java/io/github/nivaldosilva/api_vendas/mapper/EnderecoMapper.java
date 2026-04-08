package io.github.nivaldosilva.api_vendas.mapper;

import io.github.nivaldosilva.api_vendas.domain.vo.Endereco;
import io.github.nivaldosilva.api_vendas.dto.ClienteDTO;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EnderecoMapper {

    public static Endereco toEntity(ClienteDTO.EnderecoRequest request) {
        if (request == null) return null;

        return Endereco.builder()
                .cep(request.cep())
                .rua(request.rua())
                .numero(request.numero())
                .complemento(request.complemento())
                .bairro(request.bairro())
                .cidade(request.cidade())
                .uf(request.uf())
                .build();
    }

    public static ClienteDTO.EnderecoResponse toResponse(Endereco entity) {
        if (entity == null) return null;

        return ClienteDTO.EnderecoResponse.builder()
                .cep(entity.getCep())
                .rua(entity.getRua())
                .numero(entity.getNumero())
                .complemento(entity.getComplemento())
                .bairro(entity.getBairro())
                .cidade(entity.getCidade())
                .uf(entity.getUf())
                .build();
    }
}