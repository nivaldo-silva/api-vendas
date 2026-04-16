package io.github.nivaldosilva.api_vendas.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoriaDTO {

    @Builder
    @Schema(name = "CategoriaRequest", description = "Dados para cadastro ou atualização de uma categoria")
    public record CategoriaRequest(

            @Schema(description = "Nome da categoria", example = "Eletrônicos", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotBlank(message = "O nome da categoria é obrigatório")
            @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
            String nome,

            @Schema(description = "Descrição opcional da categoria", example = "Produtos eletrônicos em geral")
            @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
            String descricao

    ) {}

    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "CategoriaResponse", description = "Dados retornados de uma categoria")
    public record CategoriaResponse(

            @Schema(description = "Identificador único da categoria", example = "69de79e0840acda5e1a56fcd")
            String id,

            @Schema(description = "Nome da categoria", example = "Eletrônicos")
            String nome,

            @Schema(description = "Descrição da categoria", example = "Produtos eletrônicos em geral")
            String descricao

    ) {}
}