package io.github.nivaldosilva.api_vendas.domain.vo;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;
import java.math.BigDecimal;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ItemVenda {

    @NotBlank(message = "O ID do produto é obrigatório")
    @Field("produto_id")
    private String produtoId;

    @NotBlank(message = "O nome do produto é obrigatório")
    private String nome;

    @NotNull
    @Positive(message = "O preço unitário deve ser maior que zero")
    private BigDecimal preco;

    @NotNull
    @Min(value = 1, message = "A quantidade mínima é 1")
    private Integer quantidade;

    @NotNull
    @PositiveOrZero
    @Field("preco_total")
    private BigDecimal precoTotal;

    public void atualizarInformaces(String nome, BigDecimal preco, Integer quantidade) {
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
        this.precoTotal = calcularPrecoTotal(preco, quantidade);
    }

    public static BigDecimal calcularPrecoTotal(BigDecimal precoUnitario, Integer quantidade) {
        if (precoUnitario == null || quantidade == null) {
            return BigDecimal.ZERO;
        }
        return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }
}
