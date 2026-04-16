package io.github.nivaldosilva.api_vendas.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.nivaldosilva.api_vendas.domain.vo.ItemVenda;
import io.github.nivaldosilva.api_vendas.enums.StatusVenda;
import io.github.nivaldosilva.api_vendas.exception.BusinessException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
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
    @Field("valor_total")
    private BigDecimal valorTotal;

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

    public void finalizarVenda() {
        if (this.status != StatusVenda.PENDENTE) {
            throw new BusinessException("Apenas vendas pendentes podem ser finalizadas.");
        }
        this.status = StatusVenda.CONCLUIDA;
    }

    public void cancelar() {
        if (this.status == StatusVenda.CONCLUIDA) {
            throw new BusinessException("Uma venda concluída não pode ser cancelada.");
        }
        this.status = StatusVenda.CANCELADA;
    }
}

