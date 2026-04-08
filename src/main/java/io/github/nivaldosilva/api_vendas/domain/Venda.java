package io.github.nivaldosilva.api_vendas.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.nivaldosilva.api_vendas.domain.vo.ItemVenda;
import io.github.nivaldosilva.api_vendas.enums.StatusVenda;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "vendas")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Venda {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    @NotNull(message = "O clienteId não pode ser nulo")
    @Field("cliente_id")
    private String clienteId;

    @Builder.Default
    @PastOrPresent(message = "A data da venda não pode estar no futuro")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime data = LocalDateTime.now();

    @ReadOnlyProperty
    private BigDecimal valorTotal;

    @NotNull(message = "O valor da venda não pode ser nulo")
    @PositiveOrZero(message = "O valor da venda deve ser zero ou positivo")
    private BigDecimal valor;

    @NotNull(message = "O status da venda é obrigatório")
    private StatusVenda status;

    @NotEmpty(message = "A venda deve possuir pelo menos um item")
    @Valid
    private List<ItemVenda> items;

    public void calcularValorTotal() {
        this.valorTotal = items.stream()
                .map(ItemVenda::getPrecoTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
