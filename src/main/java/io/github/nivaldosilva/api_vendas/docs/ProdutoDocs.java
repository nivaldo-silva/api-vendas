package io.github.nivaldosilva.api_vendas.docs;

import io.github.nivaldosilva.api_vendas.dto.ProdutoDTO;
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

@Tag(name = "Produtos", description = "Gerenciamento de produtos e estoque")
public interface ProdutoDocs {

    @Operation(summary = "Cadastrar novo produto",
            description = "Cadastra um produto com código de barras único. O estoque inicial pode ser zero.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produto cadastrado com sucesso",
                    content = @Content(schema = @Schema(implementation = ProdutoDTO.ProdutoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "Código de barras já cadastrado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<ProdutoDTO.ProdutoResponse> cadastrar(
            @Valid @RequestBody ProdutoDTO.ProdutoRequest request);

    @Operation(summary = "Buscar produto por ID",
            description = "Retorna os dados de um produto pelo seu ID.")
    @Parameter(name = "id", description = "ID do produto", required = true, example = "69de7ce3840acda5e1a56fcf")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto encontrado",
                    content = @Content(schema = @Schema(implementation = ProdutoDTO.ProdutoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<ProdutoDTO.ProdutoResponse> buscarPorId(@PathVariable String id);

    @Operation(summary = "Listar todos os produtos",
            description = "Retorna todos os produtos cadastrados no sistema.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
            content = @Content(schema = @Schema(implementation = ProdutoDTO.ProdutoResponse.class)))
    ResponseEntity<List<ProdutoDTO.ProdutoResponse>> buscarTodos();

    @Operation(summary = "Buscar produtos por categoria",
            description = "Retorna todos os produtos associados a uma categoria específica.")
    @Parameter(name = "categoriaId", description = "ID da categoria", required = true, example = "69de79e0840acda5e1a56fcd")
    @ApiResponse(responseCode = "200", description = "Produtos encontrados",
            content = @Content(schema = @Schema(implementation = ProdutoDTO.ProdutoResponse.class)))
    ResponseEntity<List<ProdutoDTO.ProdutoResponse>> buscarPorCategoria(@PathVariable String categoriaId);

    @Operation(summary = "Atualizar produto",
            description = "Atualiza os dados de um produto existente.")
    @Parameter(name = "id", description = "ID do produto a ser atualizado", required = true, example = "69de7ce3840acda5e1a56fcf")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = ProdutoDTO.ProdutoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "Código de barras já cadastrado para outro produto",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<ProdutoDTO.ProdutoResponse> atualizar(
            @PathVariable String id,
            @Valid @RequestBody ProdutoDTO.ProdutoRequest request);

    @Operation(summary = "Excluir produto",
            description = "Remove um produto pelo ID. Ação irreversível.")
    @Parameter(name = "id", description = "ID do produto a ser excluído", required = true, example = "69de7ce3840acda5e1a56fcf")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Produto excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<Void> deletar(@PathVariable String id);
}