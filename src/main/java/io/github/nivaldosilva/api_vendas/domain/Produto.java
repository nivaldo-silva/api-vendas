package io.github.nivaldosilva.api_vendas.domain;

import io.github.nivaldosilva.api_vendas.exception.BusinessException;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "produtos")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Produto {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    @NotBlank(message = "Nome do produto é obrigatório")
    @Size(min = 2, max = 200, message = "Nome deve ter entre 2 e 200 caracteres")
    private String nome;

    @Size(max = 1000, message = "Descrição deve ter no máximo 1000 caracteres")
    private String descricao;

    @NotEmpty(message = "O produto deve ter pelo menos uma categoria")
    @Builder.Default
    private List<String> categorias = new ArrayList<>();

    @NotBlank(message = "Código de barras é obrigatório")
    @Pattern(regexp = "\\d{8,14}", message = "Código de barras deve ter entre 8 e 14 dígitos numéricos")
    @Indexed(unique = true)
    @Field("codigo_de_barras")
    private String codigoDeBarras;

    @PositiveOrZero(message = "O estoque não pode ser negativo")
    private Integer estoque;

    public void baixarEstoque(Integer quantidade) {
        if (quantidade > this.estoque) {
            throw new BusinessException("Estoque insuficiente para o produto: " + this.nome);
        }
        this.estoque -= quantidade;
    }
}
