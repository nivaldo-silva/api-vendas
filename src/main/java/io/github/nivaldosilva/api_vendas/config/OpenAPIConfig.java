package io.github.nivaldosilva.api_vendas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(buildInfo())
                .servers(buildServers())
                .tags(buildTags());
    }

    private Info buildInfo() {
        return new Info()
                .title("API de Vendas")
                .version("1.0.0")
                .description("""
                        API RESTful para gerenciamento de vendas, clientes, produtos e categorias.

                        ## Funcionalidades

                        - **Categorias**: Organização de produtos por categorias
                        - **Clientes**: Cadastro e gerenciamento de clientes com validação de CPF
                        - **Produtos**: Gerenciamento de produtos com controle de estoque e código de barras
                        - **Vendas**: Realização de vendas com cálculo automático de valores e baixa de estoque

                        ## Padrão de Erros

                        Todos os erros seguem o formato [RFC 7807 - Problem Details](https://tools.ietf.org/html/rfc7807):

                        | Status | Tipo                    | Descrição                                |
                        |--------|-------------------------|------------------------------------------|
                        | 400    | validation-failed       | Erro de validação nos campos             |
                        | 404    | not-found               | Recurso não encontrado                   |
                        | 409    | conflict                | Conflito com recurso existente           |
                        | 422    | business-rule-violation | Violação de regra de negócio             |
                        | 500    | internal-server-error   | Erro interno do servidor                 |
                        """)
                .contact(new Contact()
                        .name("Nivaldo da Silva")
                        .url("https://github.com/nivaldo-silva/api-vendas")
                        .email("nivaldosilva.contato@gmail.com"))
                .license(new License()
                        .name("MIT License")
                        .url("https://opensource.org/licenses/MIT"));
    }

    private List<Server> buildServers() {
        return List.of(
                new Server()
                        .url("http://localhost:" + serverPort)
                        .description("Ambiente de desenvolvimento local"),
                new Server()
                        .url("https://api.nivaldosilva.io")
                        .description("Ambiente de produção")
        );
    }


    private List<Tag> buildTags() {
        return List.of(
                new Tag().name("Categorias").description("Gerenciamento de categorias de produtos"),
                new Tag().name("Produtos").description("Gerenciamento de produtos e estoque"),
                new Tag().name("Clientes").description("Gerenciamento de clientes"),
                new Tag().name("Vendas").description("Ciclo de vida das vendas")
        );
    }
}