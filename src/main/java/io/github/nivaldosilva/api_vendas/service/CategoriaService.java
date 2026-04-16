package io.github.nivaldosilva.api_vendas.service;

import io.github.nivaldosilva.api_vendas.domain.Categoria;
import io.github.nivaldosilva.api_vendas.dto.CategoriaDTO;
import io.github.nivaldosilva.api_vendas.exception.ConflictException;
import io.github.nivaldosilva.api_vendas.exception.NotFoundException;
import io.github.nivaldosilva.api_vendas.mapper.CategoriaMapper;
import io.github.nivaldosilva.api_vendas.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaDTO.CategoriaResponse cadastrar(CategoriaDTO.CategoriaRequest request) {
        log.info("[CategoriaService] Iniciando cadastro de categoria com nome: {}", request.nome());

        if (categoriaRepository.findByNome(request.nome()).isPresent()) {
            log.warn("[CategoriaService] Conflito: já existe uma categoria com o nome '{}'", request.nome());
            throw new ConflictException("Já existe uma categoria com o nome: " + request.nome());
        }

        Categoria categoria = CategoriaMapper.toEntity(request);
        categoriaRepository.save(categoria);

        log.info("[CategoriaService] Categoria cadastrada com sucesso. ID: {}", categoria.getId());
        return CategoriaMapper.toResponse(categoria);
    }

    public CategoriaDTO.CategoriaResponse buscarPorId(String id) {
        log.info("[CategoriaService] Buscando categoria por ID: {}", id);

        return categoriaRepository.findById(id)
                .map(c -> {
                    log.info("[CategoriaService] Categoria encontrada: {}", c.getNome());
                    return CategoriaMapper.toResponse(c);
                })
                .orElseThrow(() -> {
                    log.warn("[CategoriaService] Categoria não encontrada com ID: {}", id);
                    return new NotFoundException("Categoria não encontrada com id: " + id);
                });
    }

    public List<CategoriaDTO.CategoriaResponse> buscarTodas() {
        log.info("[CategoriaService] Buscando todas as categorias");

        List<Categoria> categorias = categoriaRepository.findAll();

        if (categorias.isEmpty()) {
            log.warn("[CategoriaService] Nenhuma categoria cadastrada no sistema");
            throw new NotFoundException("Nenhuma categoria cadastrada no sistema.");
        }

        log.info("[CategoriaService] {} categoria(s) encontrada(s)", categorias.size());
        return categorias.stream()
                .map(CategoriaMapper::toResponse)
                .toList();
    }

    public CategoriaDTO.CategoriaResponse atualizar(String id, CategoriaDTO.CategoriaRequest request) {
        log.info("[CategoriaService] Iniciando atualização da categoria ID: {}", id);

        categoriaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("[CategoriaService] Categoria não encontrada para atualização. ID: {}", id);
                    return new NotFoundException("Não é possível atualizar. Categoria não encontrada com id: " + id);
                });

        categoriaRepository.findByNome(request.nome())
                .filter(c -> !c.getClass().equals(id))
                .ifPresent(c -> {
                    log.warn("[CategoriaService] Conflito de nome ao atualizar: '{}' já pertence a outra categoria", request.nome());
                    throw new ConflictException("O nome '" + request.nome() + "' já pertence a outra categoria.");
                });

        Categoria categoriaAtualizada = Categoria.builder()
                .id(id)
                .nome(request.nome())
                .descricao(request.descricao())
                .build();

        categoriaRepository.save(categoriaAtualizada);

        log.info("[CategoriaService] Categoria atualizada com sucesso. ID: {}", id);
        return CategoriaMapper.toResponse(categoriaAtualizada);
    }

    public void deletarPorId(String id) {
        log.info("[CategoriaService] Iniciando exclusão da categoria ID: {}", id);

        if (!categoriaRepository.existsById(id)) {
            log.warn("[CategoriaService] Categoria não encontrada para exclusão. ID: {}", id);
            throw new NotFoundException("Falha ao deletar. Categoria não encontrada com id: " + id);
        }

        categoriaRepository.deleteById(id);
        log.info("[CategoriaService] Categoria deletada com sucesso. ID: {}", id);
    }
}