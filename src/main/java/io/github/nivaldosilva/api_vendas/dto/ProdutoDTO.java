package io.github.nivaldosilva.api_vendas.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProdutoDTO {

    @Builder
    public record ProdutoRequest(
            @NotBlank(message = "O nome do produto é obrigatório")
            @Size(min = 2, max = 200, message = "Nome deve ter entre 2 e 200 caracteres")
            String nome,

            @NotBlank(message = "A descrição do produto é obrigatória")
            @Size(max = 1000, message = "A descrição deve ter no máximo 1000 caracteres")
            String descricao,

            @NotEmpty(message = "O produto deve ter pelo menos uma categoria")
            List<String> categorias,

            @NotBlank(message = "O código de barras é obrigatório")
            @Pattern(regexp = "\\d{8,14}", message = "Código de barras deve ter entre 8 e 14 dígitos numéricos")
            @JsonProperty("codigo_de_barras")
            String codigoDeBarras,

            @PositiveOrZero(message = "O estoque não pode ser negativo")
            Integer estoque
    ) {}

    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ProdutoResponse(
            String id,
            String nome,
            String descricao,
            List<String> categorias,
            @JsonProperty("codigo_de_barras")
            String codigoDeBarras,
            Integer estoque
    ) {}
}