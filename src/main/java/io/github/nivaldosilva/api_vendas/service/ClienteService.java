package io.github.nivaldosilva.api_vendas.service;

import io.github.nivaldosilva.api_vendas.exception.NotFoundException;
import io.github.nivaldosilva.api_vendas.domain.Cliente;
import io.github.nivaldosilva.api_vendas.dto.ClienteDTO;
import io.github.nivaldosilva.api_vendas.exception.ConflictException;
import io.github.nivaldosilva.api_vendas.mapper.ClienteMapper;
import io.github.nivaldosilva.api_vendas.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteDTO.ClienteResponse cadastrar(ClienteDTO.ClienteRequest request) {
        log.info("[ClienteService] Iniciando cadastro de cliente. CPF: {}", request.cpf());

        if (clienteRepository.findByCpf(request.cpf()).isPresent()) {
            log.warn("[ClienteService] Conflito: CPF '{}' já cadastrado", request.cpf());
            throw new ConflictException("CPF já cadastrado: " + request.cpf());
        }

        if (clienteRepository.findByEmail(request.email()).isPresent()) {
            log.warn("[ClienteService] Conflito: e-mail '{}' já cadastrado", request.email());
            throw new ConflictException("E-mail já cadastrado: " + request.email());
        }

        Cliente cliente = ClienteMapper.toEntity(request);
        clienteRepository.save(cliente);

        log.info("[ClienteService] Cliente cadastrado com sucesso. ID: {}", cliente.getId());
        return ClienteMapper.toResponse(cliente);
    }

    public ClienteDTO.ClienteResponse buscarPorId(String id) {
        log.info("[ClienteService] Buscando cliente por ID: {}", id);

        return clienteRepository.findById(id)
                .map(c -> {
                    log.info("[ClienteService] Cliente encontrado: {}", c.getNome());
                    return ClienteMapper.toResponse(c);
                })
                .orElseThrow(() -> {
                    log.warn("[ClienteService] Cliente não encontrado com ID: {}", id);
                    return new NotFoundException("Cliente não encontrado com id: " + id);
                });
    }

    public ClienteDTO.ClienteResponse buscarPorCpf(String cpf) {
        log.info("[ClienteService] Buscando cliente por CPF: {}", cpf);

        return clienteRepository.findByCpf(cpf)
                .map(c -> {
                    log.info("[ClienteService] Cliente encontrado. ID: {}", c.getId());
                    return ClienteMapper.toResponse(c);
                })
                .orElseThrow(() -> {
                    log.warn("[ClienteService] Cliente não encontrado com CPF: {}", cpf);
                    return new NotFoundException("Cliente não encontrado com CPF: " + cpf);
                });
    }

    public List<ClienteDTO.ClienteResponse> buscarPorNome(String nome) {
        log.info("[ClienteService] Buscando clientes pelo nome: {}", nome);

        List<Cliente> clientes = clienteRepository.findByNomeContainingIgnoreCase(nome);

        if (clientes.isEmpty()) {
            log.warn("[ClienteService] Nenhum cliente encontrado com o nome: {}", nome);
            throw new NotFoundException("Nenhum cliente encontrado com o nome: " + nome);
        }

        log.info("[ClienteService] {} cliente(s) encontrado(s) para o nome '{}'", clientes.size(), nome);
        return clientes.stream()
                .map(ClienteMapper::toResponse)
                .toList();
    }

    public List<ClienteDTO.ClienteResponse> buscarTodos() {
        log.info("[ClienteService] Buscando todos os clientes");

        List<Cliente> clientes = clienteRepository.findAll();

        if (clientes.isEmpty()) {
            log.warn("[ClienteService] Nenhum cliente cadastrado no sistema");
            throw new NotFoundException("Nenhum cliente cadastrado no sistema.");
        }

        log.info("[ClienteService] {} cliente(s) encontrado(s)", clientes.size());
        return clientes.stream()
                .map(ClienteMapper::toResponse)
                .toList();
    }

    public ClienteDTO.ClienteResponse atualizar(String id, ClienteDTO.ClienteRequest request) {
        log.info("[ClienteService] Iniciando atualização do cliente ID: {}", id);

        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("[ClienteService] Cliente não encontrado para atualização. ID: {}", id);
                    return new NotFoundException("Não é possível atualizar. Cliente não encontrado com id: " + id);
                });

        clienteRepository.findByCpf(request.cpf())
                .filter(c -> !c.getId().equals(id))
                .ifPresent(c -> {
                    log.warn("[ClienteService] Conflito: CPF '{}' já pertence a outro cliente", request.cpf());
                    throw new ConflictException("O CPF " + request.cpf() + " já pertence a outro cliente.");
                });

        clienteRepository.findByEmail(request.email())
                .filter(c -> !c.getId().equals(id))
                .ifPresent(c -> {
                    log.warn("[ClienteService] Conflito: e-mail '{}' já pertence a outro cliente", request.email());
                    throw new ConflictException("O E-mail " + request.email() + " já pertence a outro cliente.");
                });

        Cliente clienteAtualizado = ClienteMapper.toEntity(request, id, clienteExistente.isAtivo());
        clienteRepository.save(clienteAtualizado);

        log.info("[ClienteService] Cliente atualizado com sucesso. ID: {}", id);
        return ClienteMapper.toResponse(clienteAtualizado);
    }

    public void deletarPorId(String id) {
        log.info("[ClienteService] Iniciando exclusão do cliente ID: {}", id);

        if (!clienteRepository.existsById(id)) {
            log.warn("[ClienteService] Cliente não encontrado para exclusão. ID: {}", id);
            throw new NotFoundException("Falha ao deletar. Cliente não encontrado com id: " + id);
        }

        clienteRepository.deleteById(id);
        log.info("[ClienteService] Cliente deletado com sucesso. ID: {}", id);
    }
}