package io.github.nivaldosilva.api_vendas.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteDTO {

    @Builder
    @Schema(name = "ClienteRequest", description = "Dados para cadastro ou atualização de um cliente")
    public record ClienteRequest(

            @Schema(description = "Nome completo do cliente", example = "Maria da Silva", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotBlank(message = "Nome é obrigatório")
            @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
            String nome,

            @Schema(description = "CPF do cliente (somente dígitos, sem formatação)", example = "12345678901", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotBlank(message = "CPF é obrigatório")
            @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos numéricos")
            String cpf,

            @Schema(description = "E-mail do cliente", example = "maria@email.com", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotBlank(message = "E-mail é obrigatório")
            @Email(message = "E-mail inválido")
            String email,

            @Schema(description = "Telefone do cliente (somente dígitos, com DDD)", example = "81987654321", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotBlank(message = "Telefone é obrigatório")
            @Pattern(regexp = "\\d{10,11}", message = "Telefone deve conter 10 ou 11 dígitos")
            String telefone,

            @Schema(description = "Lista de endereços do cliente (mínimo 1)", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotEmpty(message = "Pelo menos um endereço é obrigatório")
            @Valid
            List<EnderecoRequest> enderecos

    ) {}

    @Builder
    @Schema(name = "EnderecoRequest", description = "Dados de endereço do cliente")
    public record EnderecoRequest(

            @Schema(description = "CEP (somente dígitos, sem hífen)", example = "50000000", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotBlank(message = "CEP é obrigatório")
            @Pattern(regexp = "\\d{8}", message = "CEP deve conter 8 dígitos numéricos")
            String cep,

            @Schema(description = "Nome da rua", example = "Rua das Flores", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotBlank(message = "O nome da rua é obrigatório")
            String rua,

            @Schema(description = "Número do endereço", example = "123", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotBlank(message = "Número é obrigatório")
            String numero,

            @Schema(description = "Complemento do endereço", example = "Apto 42")
            String complemento,

            @Schema(description = "Bairro", example = "Boa Viagem", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotBlank(message = "Bairro é obrigatório")
            String bairro,

            @Schema(description = "Cidade", example = "Recife", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotBlank(message = "Cidade é obrigatória")
            String cidade,

            @Schema(description = "UF (sigla com 2 letras)", example = "PE", requiredMode = Schema.RequiredMode.REQUIRED)
            @NotBlank(message = "UF é obrigatória")
            @Size(min = 2, max = 2, message = "UF deve ter exatamente 2 caracteres")
            String uf

    ) {}

    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "EnderecoResponse", description = "Dados de endereço retornados")
    public record EnderecoResponse(
            @Schema(example = "50000000") String cep,
            @Schema(example = "Rua das Flores") String rua,
            @Schema(example = "123") String numero,
            @Schema(example = "Apto 42") String complemento,
            @Schema(example = "Boa Viagem") String bairro,
            @Schema(example = "Recife") String cidade,
            @Schema(example = "PE") String uf
    ) {}

    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "ClienteResponse", description = "Dados retornados de um cliente")
    public record ClienteResponse(
            @Schema(description = "Identificador único do cliente", example = "69de7a13840acda5e1a56fce")
            String id,
            @Schema(example = "Maria da Silva") String nome,
            @Schema(example = "12345678901") String cpf,
            @Schema(example = "maria@email.com") String email,
            @Schema(example = "81987654321") String telefone,
            List<EnderecoResponse> enderecos,
            @Schema(description = "Indica se o cliente está ativo no sistema")
            boolean ativo
    ) {}
}