package io.github.nivaldosilva.api_vendas.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoriaDTO {

    @Builder
    public record CategoriaRequest(
            @NotBlank(message = "O nome da categoria é obrigatório")
            @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
            String nome,

            @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
            String descricao
    ) {}

    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record CategoriaResponse(
            String id,
            String nome,
            String descricao
    ) {}
}