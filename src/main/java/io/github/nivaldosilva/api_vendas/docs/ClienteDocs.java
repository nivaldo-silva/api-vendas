package io.github.nivaldosilva.api_vendas.docs;

import io.github.nivaldosilva.api_vendas.dto.ClienteDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Tag(name = "Clientes", description = "Gerenciamento de clientes")
public interface ClienteDocs {

    @Operation(summary = "Cadastrar novo cliente",
            description = "Cadastra um cliente com validação de CPF único. O e-mail também deve ser único.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente cadastrado com sucesso",
                    content = @Content(schema = @Schema(implementation = ClienteDTO.ClienteResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "CPF já cadastrado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<ClienteDTO.ClienteResponse> cadastrar(
            @Valid @RequestBody ClienteDTO.ClienteRequest request);

    @Operation(summary = "Buscar cliente por ID",
            description = "Retorna os dados de um cliente pelo seu ID.")
    @Parameter(name = "id", description = "ID do cliente", required = true, example = "69de7a13840acda5e1a56fce")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado",
                    content = @Content(schema = @Schema(implementation = ClienteDTO.ClienteResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<ClienteDTO.ClienteResponse> buscarPorId(@PathVariable String id);

    @Operation(summary = "Buscar cliente por CPF",
            description = "Retorna os dados de um cliente pelo seu CPF (somente dígitos).")
    @Parameter(name = "cpf", description = "CPF do cliente (somente dígitos)", required = true, example = "12345678901")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado",
                    content = @Content(schema = @Schema(implementation = ClienteDTO.ClienteResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<ClienteDTO.ClienteResponse> buscarPorCpf(@PathVariable String cpf);

    @Operation(summary = "Buscar clientes por nome",
            description = "Busca parcial e case-insensitive por nome de clientes.")
    @Parameter(name = "nome", description = "Nome ou parte do nome", required = true, example = "Maria")
    @ApiResponse(responseCode = "200", description = "Clientes encontrados",
            content = @Content(schema = @Schema(implementation = ClienteDTO.ClienteResponse.class)))
    ResponseEntity<List<ClienteDTO.ClienteResponse>> buscarPorNome(@RequestParam String nome);

    @Operation(summary = "Listar todos os clientes",
            description = "Retorna todos os clientes cadastrados no sistema.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
            content = @Content(schema = @Schema(implementation = ClienteDTO.ClienteResponse.class)))
    ResponseEntity<List<ClienteDTO.ClienteResponse>> buscarTodos();

    @Operation(summary = "Atualizar cliente",
            description = "Atualiza os dados de um cliente existente.")
    @Parameter(name = "id", description = "ID do cliente a ser atualizado", required = true, example = "69de7a13840acda5e1a56fce")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = ClienteDTO.ClienteResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "CPF já cadastrado para outro cliente",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<ClienteDTO.ClienteResponse> atualizar(
            @PathVariable String id,
            @Valid @RequestBody ClienteDTO.ClienteRequest request);

    @Operation(summary = "Excluir cliente",
            description = "Remove um cliente pelo ID. Ação irreversível.")
    @Parameter(name = "id", description = "ID do cliente a ser excluído", required = true, example = "69de7a13840acda5e1a56fce")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cliente excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<Void> deletar(@PathVariable String id);
}