package io.github.nivaldosilva.api_vendas.exception;

/**
 * Exceção lançada quando uma regra de negócio é violada.
 *
 * <p>Utilizada para validar operações que não podem ser realizadas devido
 * a restrições de negócio. Exemplo: tentar finalizar uma venda que não
 * está pendente, ou cancelar uma venda já concluída. O GlobalExceptionHandler
 * converte esta exceção em uma resposta HTTP 422 com Problem Details.</p>
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
