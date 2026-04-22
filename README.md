# API Vendas

API REST para gerenciamento de `categorias`, `produtos`, `clientes` e `vendas`, construida com Spring Boot e MongoDB.

## Visao Geral

O projeto implementa um fluxo completo de vendas:

- Cadastro e manutencao de categorias.
- Cadastro e manutencao de produtos com codigo de barras e estoque.
- Cadastro e manutencao de clientes com validacao de CPF, e-mail e endereco.
- Realizacao de vendas com validacao de cliente/produto, calculo de valor total e baixa de estoque.
- Alteracao de status da venda (finalizacao e cancelamento).
- Padrao de erros baseado em RFC 7807 (`ProblemDetail`).
- Documentacao interativa via Swagger/OpenAPI.

## Stack Tecnológica

- Java 21
- Spring Boot 4.0.5
- Spring Web MVC
- Spring Data MongoDB
- Spring Validation (Jakarta Validation)
- Springdoc OpenAPI UI 2.8.5
- Lombok
- Gradle Wrapper 9.4.1

## Arquitetura e Organização

Estrutura principal:

```text
src/main/java/io/github/nivaldosilva/api_vendas
|- config/       # configuracoes da aplicacao e OpenAPI
|- controller/   # endpoints REST
|- docs/         # contratos/anotacoes de documentacao OpenAPI
|- domain/       # entidades de dominio (MongoDB)
|- dto/          # request/response da API
|- enums/        # enumeracoes de dominio
|- exception/    # excecoes customizadas e handler global
|- mapper/       # mapeamentos DTO <-> entidade
|- repository/   # interfaces Spring Data MongoDB
|- service/      # regras de negocio
```

## Requisitos

- Java 21 instalado e configurado no `PATH`
- MongoDB em execucao local
- Porta `8080` disponivel (ou ajuste no `application.yaml`)

## Configuracao

Arquivo: `src/main/resources/application.yaml`

```yaml
server:
  port: 8080

spring:
  application:
    name: api-vendas
  mongodb:
    auto-index-creation: true
    uri: mongodb://localhost:27017/vendas

springdoc:
  swagger-ui:
    default-models-expand-depth: 1
    tags-sorter: alpha
```

### Banco de Dados

- Banco utilizado: MongoDB
- URI padrao: `mongodb://localhost:27017/vendas`
- Collections:
  - `categorias`
  - `produtos`
  - `clientes`
  - `vendas`
- Indices unicos:
  - Categoria: `nome`
  - Produto: `codigo_de_barras`
  - Cliente: `cpf` e `email`

## Como Executar

### 1) Clonar o repositorio

```bash
git clone https://github.com/nivaldo-silva/api-vendas.git
cd api-vendas
```

### 2) Subir o MongoDB

Garanta que o MongoDB esteja ativo na URI configurada.

### 3) Rodar a aplicação

No Windows:

```bash
gradlew.bat bootRun
```

No Linux/macOS:

```bash
./gradlew bootRun
```

Aplicacao disponivel em: `http://localhost:8080`

## Build e Testes

Executar testes:

```bash
gradlew.bat test
```

Gerar build:

```bash
gradlew.bat build
```

Observacao: atualmente existe apenas teste de contexto da aplicação (`ApiVendasApplicationTests`).

## Documentacao da API (Swagger)

Com a API em execução:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

Servidores definidos no OpenAPI:

- `http://localhost:{port}` (desenvolvimento)
- `https://api.nivaldosilva.io` (producao, metadado de documentacao)

## Endpoints

Base path: `/api/v1`

### Categorias

- `POST /categorias` - cadastrar categoria
- `GET /categorias/{id}` - buscar categoria por ID
- `GET /categorias` - listar categorias
- `PUT /categorias/{id}` - atualizar categoria
- `DELETE /categorias/{id}` - remover categoria

Exemplo de request (`POST/PUT`):

```json
{
  "nome": "Eletronicos",
  "descricao": "Produtos eletronicos em geral"
}
```

### Produtos

- `POST /produtos` - cadastrar produto
- `GET /produtos/{id}` - buscar produto por ID
- `GET /produtos` - listar produtos
- `GET /produtos/categoria/{categoriaId}` - listar produtos por categoria
- `PUT /produtos/{id}` - atualizar produto
- `DELETE /produtos/{id}` - remover produto

Exemplo de request (`POST/PUT`):

```json
{
  "nome": "Smartphone Galaxy S24",
  "descricao": "Smartphone com 256GB, camera 200MP",
  "categorias": ["<categoria_id>"],
  "codigo_de_barras": "7891234567890",
  "preco": 3999.90,
  "estoque": 50
}
```

### Clientes

- `POST /clientes` - cadastrar cliente
- `GET /clientes/{id}` - buscar cliente por ID
- `GET /clientes/cpf/{cpf}` - buscar cliente por CPF
- `GET /clientes/buscar?nome=...` - buscar clientes por nome (parcial, case-insensitive)
- `GET /clientes` - listar clientes
- `PUT /clientes/{id}` - atualizar cliente
- `DELETE /clientes/{id}` - remover cliente

Exemplo de request (`POST/PUT`):

```json
{
  "nome": "Maria da Silva",
  "cpf": "12345678901",
  "email": "maria@email.com",
  "telefone": "81987654321",
  "enderecos": [
    {
      "cep": "50000000",
      "rua": "Rua das Flores",
      "numero": "123",
      "complemento": "Apto 42",
      "bairro": "Boa Viagem",
      "cidade": "Recife",
      "uf": "PE"
    }
  ]
}
```

### Vendas

- `POST /vendas` - realizar nova venda
- `GET /vendas/{id}` - buscar venda por ID
- `GET /vendas/cliente/{clienteId}` - listar vendas de um cliente
- `GET /vendas` - listar vendas
- `PATCH /vendas/{id}/finalizar` - finalizar venda pendente
- `PATCH /vendas/{id}/cancelar` - cancelar venda

Exemplo de request (`POST`):

```json
{
  "cliente_id": "<cliente_id>",
  "items": [
    {
      "produto_id": "<produto_id>",
      "quantidade": 2
    }
  ]
}
```

## Regras de Negócio Principais

- Nao permite cadastrar:
  - Categoria com nome duplicado.
  - Produto com codigo de barras duplicado.
  - Cliente com CPF ou e-mail duplicado.
- Produto precisa ter pelo menos uma categoria valida.
- Venda exige cliente existente e produtos existentes.
- Ao realizar venda:
  - O sistema calcula `valor_total`.
  - O sistema baixa estoque dos produtos vendidos.
- Finalizacao de venda:
  - Apenas vendas com status `PENDENTE` podem ser finalizadas.
- Cancelamento de venda:
  - Vendas `CONCLUIDA` nao podem ser canceladas.

## Status de Venda

Enum `StatusVenda`:

- `PENDENTE` (codigo `1`)
- `PROCESSANDO` (codigo `2`)
- `CONCLUIDA` (codigo `3`)
- `CANCELADA` (codigo `4`)
- `REEMBOLSADA` (codigo `5`)

## Tratamento de Erros (RFC 7807)

A API retorna erros no formato `ProblemDetail`.

Tipos principais:

- `not-found` (`404`)
- `conflict` (`409`)
- `business-rule-violation` (`422`)
- `validation-failed` (`400`)
- `duplicate` (`409`)
- `method-not-allowed` (`405`)
- `malformed-json` (`400`)
- `type-mismatch` (`400`)
- `missing-parameter` (`400`)
- `internal-server-error` (`500`)

Exemplo de resposta de erro:

```json
{
  "type": "https://api-vendas.io/errors/validation-failed",
  "title": "Erro de validacao",
  "status": 400,
  "detail": "A requisicao contem 1 erro(s) de validacao. Corrija os campos indicados e tente novamente.",
  "instance": "/api/v1/clientes",
  "timestamp": "2026-04-16T12:00:00Z",
  "errors": [
    {
      "field": "cpf",
      "message": "CPF deve conter 11 digitos numericos"
    }
  ]
}
```




