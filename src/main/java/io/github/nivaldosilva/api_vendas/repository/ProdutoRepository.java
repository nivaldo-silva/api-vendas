package io.github.nivaldosilva.api_vendas.repository;

import io.github.nivaldosilva.api_vendas.domain.Produto;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProdutoRepository extends MongoRepository<Produto, String> {
}
