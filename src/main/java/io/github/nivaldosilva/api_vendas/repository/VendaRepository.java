package io.github.nivaldosilva.api_vendas.repository;

import io.github.nivaldosilva.api_vendas.domain.Venda;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface VendaRepository extends MongoRepository<Venda, String> {

    List<Venda> findByClienteId(String clienteId);
}
