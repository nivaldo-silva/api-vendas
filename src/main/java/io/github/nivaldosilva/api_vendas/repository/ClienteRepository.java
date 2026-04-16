package io.github.nivaldosilva.api_vendas.repository;

import io.github.nivaldosilva.api_vendas.domain.Cliente;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends MongoRepository<Cliente, String> {

    Optional<Cliente> findByCpf(String cpf);
    Optional<Cliente> findByEmail(String email);
    List<Cliente> findByNomeContainingIgnoreCase(String nome);


}