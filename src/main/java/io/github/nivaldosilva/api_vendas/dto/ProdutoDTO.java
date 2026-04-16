package io.github.nivaldosilva.api_vendas.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProdutoDTO {

    @Builder
    @Schema(name = "ProdutoRequest", description = "Dados para cadastro ou atualização de um produto")
    public record ProdutoRequest(

            @Schema(description = "Nome do produto", example = "Smartphone Galaxy S24", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotBlank(message = "O nome do produto é obrigatório")
            @Size(min = 2, max = 200, message = "Nome deve ter entre 2 e 200 caracteres")
            String nome,

            @Schema(description = "Descrição detalhada do produto", example = "Smartphone com 256GB, câmera 200MP", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotBlank(message = "A descrição do produto é obrigatória")
            @Size(max = 1000, message = "A descrição deve ter no máximo 1000 caracteres")
            String descricao,

            @Schema(description = "Lista de IDs das categorias associadas ao produto", example = "[\"69de79e0840acda5e1a56fcd\"]", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotEmpty(message = "O produto deve ter pelo menos uma categoria")
            List<String> categorias,

            @Schema(description = "Código de barras único do produto (EAN-8 a EAN-14)", example = "7891234567890", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotBlank(message = "O código de barras é obrigatório")
            @Pattern(regexp = "\\d{8,14}", message = "Código de barras deve ter entre 8 e 14 dígitos numéricos")
            @JsonProperty("codigo_de_barras")
            String codigoDeBarras,

            @Schema(description = "Preço unitário do produto", example = "3999.90", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotNull(message = "O preço é obrigatório")
            @Positive(message = "O preço deve ser maior que zero")
            BigDecimal preco,

            @Schema(description = "Quantidade inicial em estoque (zero ou positivo)", example = "50")
            @PositiveOrZero(message = "O estoque não pode ser negativo")
            Integer estoque

    ) {}

    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "ProdutoResponse", description = "Dados retornados de um produto")
    public record ProdutoResponse(

            @Schema(description = "Identificador único do produto", example = "69de7ce3840acda5e1a56fcf")
            String id,

            @Schema(example = "Smartphone Galaxy S24")
            String nome,

            @Schema(example = "Smartphone com 256GB, câmera 200MP")
            String descricao,

            @Schema(description = "IDs das categorias do produto")
            List<String> categorias,

            @JsonProperty("codigo_de_barras")
            @Schema(example = "7891234567890")
            String codigoDeBarras,

            @Schema(example = "3999.90")
            BigDecimal preco,

            @Schema(description = "Quantidade disponível em estoque", example = "50")
            Integer estoque

    ) {}
}