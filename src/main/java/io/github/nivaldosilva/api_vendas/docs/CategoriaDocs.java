package io.github.nivaldosilva.api_vendas.docs;

import io.github.nivaldosilva.api_vendas.dto.CategoriaDTO;
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

@Tag(name = "Categorias", description = "Gerenciamento de categorias de produtos")
public interface CategoriaDocs {

    @Operation(summary = "Cadastrar nova categoria",
            description = "Cadastra uma nova categoria no sistema. O nome deve ser único.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categoria cadastrada com sucesso",
                    content = @Content(schema = @Schema(implementation = CategoriaDTO.CategoriaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "Nome de categoria já cadastrado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<CategoriaDTO.CategoriaResponse> cadastrar(
            @Valid @RequestBody CategoriaDTO.CategoriaRequest request);

    @Operation(summary = "Buscar categoria por ID",
            description = "Retorna os dados de uma categoria pelo seu ID.")
    @Parameter(name = "id", description = "ID da categoria", required = true, example = "69de79e0840acda5e1a56fcd")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria encontrada",
                    content = @Content(schema = @Schema(implementation = CategoriaDTO.CategoriaResponse.class))),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<CategoriaDTO.CategoriaResponse> buscarPorId(@PathVariable String id);

    @Operation(summary = "Listar todas as categorias",
            description = "Retorna todas as categorias cadastradas no sistema.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
            content = @Content(schema = @Schema(implementation = CategoriaDTO.CategoriaResponse.class)))
    ResponseEntity<List<CategoriaDTO.CategoriaResponse>> buscarTodas();

    @Operation(summary = "Atualizar categoria",
            description = "Atualiza os dados de uma categoria existente.")
    @Parameter(name = "id", description = "ID da categoria a ser atualizada", required = true, example = "69de79e0840acda5e1a56fcd")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso",
                    content = @Content(schema = @Schema(implementation = CategoriaDTO.CategoriaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "Nome já cadastrado para outra categoria",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<CategoriaDTO.CategoriaResponse> atualizar(
            @PathVariable String id,
            @Valid @RequestBody CategoriaDTO.CategoriaRequest request);

    @Operation(summary = "Excluir categoria",
            description = "Remove uma categoria pelo ID. Ação irreversível.")
    @Parameter(name = "id", description = "ID da categoria a ser excluída", required = true, example = "69de79e0840acda5e1a56fcd")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Categoria excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<Void> deletar(@PathVariable String id);
}