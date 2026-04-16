package io.github.nivaldosilva.api_vendas.repository;

import io.github.nivaldosilva.api_vendas.domain.Categoria;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface CategoriaRepository extends MongoRepository<Categoria, String> {

    Optional<Object> findByNome(String nome);
}