package io.github.nivaldosilva.api_vendas.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String BASE_TYPE_URI = "https://api-vendas.io/errors/";

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFoundException(
            NotFoundException ex, HttpServletRequest request) {

        log.warn("[GlobalExceptionHandler] Recurso não encontrado: {}", ex.getMessage());

        ProblemDetail problem = buildProblem(
                HttpStatus.NOT_FOUND,
                "not-found",
                "Recurso não encontrado",
                ex.getMessage(),
                request
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ProblemDetail> handleConflictException(
            ConflictException ex, HttpServletRequest request) {

        log.warn("[GlobalExceptionHandler] Conflito de recurso: {}", ex.getMessage());

        ProblemDetail problem = buildProblem(
                HttpStatus.CONFLICT,
                "conflict",
                "Conflito com recurso existente",
                ex.getMessage(),
                request
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ProblemDetail> handleBusinessException(
            BusinessException ex, HttpServletRequest request) {

        log.warn("[GlobalExceptionHandler] Violação de regra de negócio: {}", ex.getMessage());

        ProblemDetail problem = buildProblem(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "business-rule-violation",
                "Violação de regra de negócio",
                ex.getMessage(),
                request
        );

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(problem);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        int errorCount = ex.getBindingResult().getErrorCount();
        log.warn("[GlobalExceptionHandler] Erro de validação: {} campo(s) com erro", errorCount);

        List<Map<String, String>> fieldErrors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> Map.of(
                        "field", error.getField(),
                        "message", String.valueOf(error.getDefaultMessage())
                ))
                .collect(Collectors.toList());

        ProblemDetail problem = buildProblem(
                HttpStatus.BAD_REQUEST,
                "validation-failed",
                "Erro de validação",
                String.format("A requisição contém %d erro(s) de validação. Corrija os campos indicados e tente novamente.", errorCount),
                request
        );
        // Propriedade extra — serializada automaticamente pelo Jackson no ProblemDetail nativo
        problem.setProperty("errors", fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ProblemDetail> handleDuplicateKeyException(
            DuplicateKeyException ex, HttpServletRequest request) {

        log.warn("[GlobalExceptionHandler] Documento duplicado: {}", ex.getMessage());

        ProblemDetail problem = buildProblem(
                HttpStatus.CONFLICT,
                "duplicate",
                "Recurso duplicado",
                "Já existe um recurso com este identificador único. Verifique os dados e tente novamente.",
                request
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ProblemDetail> handleMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {

        log.warn("[GlobalExceptionHandler] Método não suportado: {}", ex.getMethod());

        String supportedMethods = ex.getSupportedHttpMethods() != null
                ? String.join(", ", ex.getSupportedHttpMethods().stream().map(HttpMethod::name).toList())
                : "Nenhum";

        ProblemDetail problem = buildProblem(
                HttpStatus.METHOD_NOT_ALLOWED,
                "method-not-allowed",
                "Método HTTP não suportado",
                String.format("O método '%s' não é suportado para este endpoint. Métodos suportados: [%s].", ex.getMethod(), supportedMethods),
                request
        );

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(problem);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex, HttpServletRequest request) {

        log.warn("[GlobalExceptionHandler] Corpo da requisição inválido: {}", ex.getMessage());

        ProblemDetail problem = buildProblem(
                HttpStatus.BAD_REQUEST,
                "malformed-json",
                "Corpo da requisição inválido",
                "O corpo da requisição está malformado ou contém dados incompatíveis. Verifique o JSON enviado e tente novamente.",
                request
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> handleTypeMismatchException(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {

        log.warn("[GlobalExceptionHandler] Tipo incompatível: parâmetro '{}'", ex.getName());

        ProblemDetail problem = buildProblem(
                HttpStatus.BAD_REQUEST,
                "type-mismatch",
                "Tipo de parâmetro incompatível",
                String.format("O parâmetro '%s' recebeu o valor '%s', que é incompatível com o tipo esperado '%s'.",
                        ex.getName(), ex.getValue(),
                        ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconhecido"),
                request
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ProblemDetail> handleMissingParameterException(
            MissingServletRequestParameterException ex, HttpServletRequest request) {

        log.warn("[GlobalExceptionHandler] Parâmetro ausente: {}", ex.getParameterName());

        ProblemDetail problem = buildProblem(
                HttpStatus.BAD_REQUEST,
                "missing-parameter",
                "Parâmetro obrigatório ausente",
                String.format("O parâmetro obrigatório '%s' (%s) não foi informado.", ex.getParameterName(), ex.getParameterType()),
                request
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ProblemDetail> handleNoResourceFoundException(
            NoResourceFoundException ex, HttpServletRequest request) {

        log.warn("[GlobalExceptionHandler] Rota não encontrada: {}", request.getRequestURI());

        ProblemDetail problem = buildProblem(
                HttpStatus.NOT_FOUND,
                "not-found",
                "Rota não encontrada",
                String.format("O caminho '%s' não foi encontrado nesta API. Verifique a URL e tente novamente.", request.getRequestURI()),
                request
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericException(
            Exception ex, HttpServletRequest request) {

        log.error("[GlobalExceptionHandler] Erro interno não tratado", ex);

        ProblemDetail problem = buildProblem(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "internal-server-error",
                "Erro interno do servidor",
                "Ocorreu um erro inesperado. Tente novamente mais tarde. Se o problema persistir, entre em contato com o suporte.",
                request
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
    }

    private ProblemDetail buildProblem(HttpStatus status, String errorSlug, String title,
                                       String detail, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setType(URI.create(BASE_TYPE_URI + errorSlug));
        problem.setTitle(title);
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
}