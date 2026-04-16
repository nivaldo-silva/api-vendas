package io.github.nivaldosilva.api_vendas.docs;

import io.github.nivaldosilva.api_vendas.dto.VendaDTO;
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
import java.util.List;

@Tag(name = "Vendas", description = "Ciclo de vida das vendas")
public interface VendaDocs {

    @Operation(summary = "Realizar nova venda",
            description = "Registra uma nova venda validando a existência do cliente e a disponibilidade de estoque.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Venda registrada com sucesso",
                    content = @Content(schema = @Schema(implementation = VendaDTO.VendaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou estoque insuficiente",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Cliente ou produto não encontrado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<VendaDTO.VendaResponse> realizarVenda(
            @Valid @RequestBody VendaDTO.VendaRequest request);

    @Operation(summary = "Buscar venda por ID",
            description = "Retorna os detalhes completos de uma venda específica.")
    @Parameter(name = "id", description = "ID da venda", required = true, example = "69de7d2a840acda5e1a56fd0")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Venda encontrada",
                    content = @Content(schema = @Schema(implementation = VendaDTO.VendaResponse.class))),
            @ApiResponse(responseCode = "404", description = "Venda não encontrada",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<VendaDTO.VendaResponse> buscarPorId(@PathVariable String id);

    @Operation(summary = "Listar vendas por cliente",
            description = "Retorna o histórico completo de vendas de um cliente.")
    @Parameter(name = "clienteId", description = "ID do cliente", required = true, example = "69de7a13840acda5e1a56fce")
    @ApiResponse(responseCode = "200", description = "Histórico retornado com sucesso",
            content = @Content(schema = @Schema(implementation = VendaDTO.VendaResponse.class)))
    ResponseEntity<List<VendaDTO.VendaResponse>> buscarPorCliente(@PathVariable String clienteId);

    @Operation(summary = "Listar todas as vendas",
            description = "Retorna todas as vendas registradas no sistema.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
            content = @Content(schema = @Schema(implementation = VendaDTO.VendaResponse.class)))
    ResponseEntity<List<VendaDTO.VendaResponse>> buscarTodas();

    @Operation(summary = "Finalizar venda",
            description = "Altera o status da venda para FINALIZADA. Uma venda finalizada não pode mais ser editada.")
    @Parameter(name = "id", description = "ID da venda a ser finalizada", required = true, example = "69de7d2a840acda5e1a56fd0")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Venda finalizada com sucesso",
                    content = @Content(schema = @Schema(implementation = VendaDTO.VendaResponse.class))),
            @ApiResponse(responseCode = "404", description = "Venda não encontrada",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "422", description = "Venda não pode ser finalizada no status atual",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<VendaDTO.VendaResponse> finalizarVenda(@PathVariable String id);

    @Operation(summary = "Cancelar venda",
            description = "Cancela uma venda e realiza o estorno dos produtos ao estoque.")
    @Parameter(name = "id", description = "ID da venda a ser cancelada", required = true, example = "69de7d2a840acda5e1a56fd0")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Venda cancelada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Venda não encontrada",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "422", description = "Venda não pode ser cancelada no status atual",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<Void> cancelarVenda(@PathVariable String id);
}