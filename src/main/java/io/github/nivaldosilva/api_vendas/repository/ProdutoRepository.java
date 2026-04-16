package io.github.nivaldosilva.api_vendas.repository;

import io.github.nivaldosilva.api_vendas.domain.Produto;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends MongoRepository<Produto, String> {

    Optional<Produto> findByCodigoDeBarras(String codigoDeBarras);
    List<Produto> findByCategoriasContaining(String categoriaId);
}
