package io.github.nivaldosilva.api_vendas.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusVenda {

    PENDENTE(1, "Aguardando Pagamento"),

    PROCESSANDO(2, "Em Processamento"),

    CONCLUIDA(3, "Concluída"),

    CANCELADA(4, "Cancelada"),

    REEMBOLSADA(5, "Reembolsada");

    private final Integer codigo;
    private final String descricao;

    @JsonValue
    public Integer getCodigo() {
        return codigo; }
}
