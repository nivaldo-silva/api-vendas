package io.github.nivaldosilva.api_vendas.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteDTO {

    @Builder
    public record ClienteRequest(

            @NotBlank(message = "Nome é obrigatório")
            @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
            String nome,

            @NotBlank(message = "CPF é obrigatório")
            @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos numéricos")
            String cpf,

            @NotBlank(message = "E-mail é obrigatório")
            @Email(message = "E-mail inválido")
            String email,

            @NotBlank(message = "Telefone é obrigatório")
            @Pattern(regexp = "\\d{10,11}", message = "Telefone deve conter 10 ou 11 dígitos")
            String telefone,

            @NotEmpty(message = "Pelo menos um endereço é obrigatório")
            @Valid
            List<EnderecoRequest> enderecos

    ) {}

    @Builder
    public record EnderecoRequest(

            @NotBlank(message = "CEP é obrigatório")
            @Pattern(regexp = "\\d{8}", message = "CEP deve conter 8 dígitos numéricos")
            String cep,

            @NotBlank(message = "O nome da rua é obrigatório")
            String rua,

            @NotBlank(message = "Número é obrigatório")
            String numero,

            String complemento,

            @NotBlank(message = "Bairro é obrigatório")
            String bairro,

            @NotBlank(message = "Cidade é obrigatória")
            String cidade,

            @NotBlank(message = "UF é obrigatória")
            @Size(min = 2, max = 2, message = "UF deve ter exatamente 2 caracteres")
            String uf
    ) {}

    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record EnderecoResponse(
            String cep,
            String rua,
            String numero,
            String complemento,
            String bairro,
            String cidade,
            String uf
    ) {}

    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ClienteResponse(
            String id,
            String nome,
            String cpf,
            String email,
            String telefone,
            List<EnderecoResponse> enderecos,
            boolean ativo
    ) {}
}