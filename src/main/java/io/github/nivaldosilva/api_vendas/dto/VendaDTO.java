package io.github.nivaldosilva.api_vendas.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.nivaldosilva.api_vendas.enums.StatusVenda;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VendaDTO {

    @Builder
    public record VendaRequest(
            @NotBlank(message = "O ID do cliente é obrigatório")
            @JsonProperty("cliente_id")
            String clienteId,

            @NotEmpty(message = "A venda deve possuir pelo menos um item")
            @Valid
            List<ItemVendaRequest> items
    ) {}

    @Builder
    public record ItemVendaRequest(
            @NotBlank(message = "O ID do produto é obrigatório")
            @JsonProperty("produto_id")
            String produtoId,

            @NotNull(message = "A quantidade é obrigatória")
            @Min(value = 1, message = "A quantidade mínima é 1")
            Integer quantidade
    ) {}

    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record VendaResponse(
            String id,
            @JsonProperty("cliente_id")
            String clienteId,
            LocalDateTime data,
            @JsonProperty("valor_total")
            BigDecimal valorTotal,
            StatusVenda status,
            List<ItemVendaResponse> items
    ) {}

    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ItemVendaResponse(
            @JsonProperty("produto_id")
            String produtoId,
            String nome,
            BigDecimal preco,
            Integer quantidade,
            @JsonProperty("preco_total")
            BigDecimal precoTotal
    ) {}
}