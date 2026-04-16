package io.github.nivaldosilva.api_vendas.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.nivaldosilva.api_vendas.enums.StatusVenda;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(name = "VendaRequest", description = "Dados para realização de uma nova venda")
    public record VendaRequest(

            @Schema(description = "ID do cliente que está realizando a compra", example = "69de7a13840acda5e1a56fce", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotBlank(message = "O ID do cliente é obrigatório")
            @JsonProperty("cliente_id")
            String clienteId,

            @Schema(description = "Lista de itens da venda (mínimo 1 item)", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotEmpty(message = "A venda deve possuir pelo menos um item")
            @Valid
            List<ItemVendaRequest> items

    ) {}

    @Builder
    @Schema(name = "ItemVendaRequest", description = "Item de uma venda com produto e quantidade")
    public record ItemVendaRequest(

            @Schema(description = "ID do produto", example = "69de7ce3840acda5e1a56fcf", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotBlank(message = "O ID do produto é obrigatório")
            @JsonProperty("produto_id")
            String produtoId,

            @Schema(description = "Quantidade desejada do produto (mínimo 1)", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotNull(message = "A quantidade é obrigatória")
            @Min(value = 1, message = "A quantidade mínima é 1")
            Integer quantidade

    ) {}

    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "VendaResponse", description = "Dados retornados de uma venda")
    public record VendaResponse(

            @Schema(description = "Identificador único da venda", example = "69de7d2a840acda5e1a56fd0")
            String id,

            @JsonProperty("cliente_id")
            @Schema(description = "ID do cliente vinculado à venda", example = "69de7a13840acda5e1a56fce")
            String clienteId,

            @Schema(description = "Data e hora de criação da venda", example = "2025-01-15T14:30:00")
            LocalDateTime data,

            @JsonProperty("valor_total")
            @Schema(description = "Valor total calculado da venda", example = "7999.80")
            BigDecimal valorTotal,

            @Schema(description = "Status atual da venda")
            StatusVenda status,

            @Schema(description = "Lista de itens que compõem a venda")
            List<ItemVendaResponse> items

    ) {}

    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "ItemVendaResponse", description = "Dados de um item retornado na venda")
    public record ItemVendaResponse(

            @JsonProperty("produto_id")
            @Schema(description = "ID do produto", example = "69de7ce3840acda5e1a56fcf")
            String produtoId,

            @Schema(description = "Nome do produto no momento da venda", example = "Smartphone Galaxy S24")
            String nome,

            @Schema(description = "Preço unitário do produto no momento da venda", example = "3999.90")
            BigDecimal preco,

            @Schema(description = "Quantidade comprada", example = "2")
            Integer quantidade,

            @JsonProperty("preco_total")
            @Schema(description = "Preço total do item (preço × quantidade)", example = "7999.80")
            BigDecimal precoTotal

    ) {}
}