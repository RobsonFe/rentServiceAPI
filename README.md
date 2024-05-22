# Teste Técnico Full Stack Spring Boot

- Teste Ténico para a empresa Flowing
- Projeto Full Stack com Spring Boot + Angular 
- Autor: Robson Ferreira

## Descrição

A RentService é uma API REST desenvolvida em Java com Spring Boot para gerenciar as locações de carros de uma locadora. A API permite cadastrar, consultar e cancelar locações, além de garantir que um cliente não possa ter mais de uma locação ativa ao mesmo tempo e que a data inicial da locação não ultrapasse a data final.

## Funcionalidades

- Cadastrar locação
- Consultar locação por ID
- Listar todas as locações
- Cancelar locação

## Tecnologias Utilizadas

- Java 21.0.03
- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL
- Docker
- Swagger
- OpenAPI
- JUnit
- RabbitMQ

# Documentação 
- [Documentação do Projeto](https://docs.google.com/document/d/1TDdKUlNRj9m_nURjmPE4De3XRjnuBY-6BG_L7rO2Of8/edit?usp=sharing)
- [Java SE](https://docs.oracle.com/en/java/)
- [Intellij IDEA](https://www.jetbrains.com/idea/)
- [Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [JDK 21](https://docs.oracle.com/en/java/javase/21/)
- [MySQL](https://dev.mysql.com/doc/)
- [Docker](https://www.docker.com/)
- [Swagger](https://swagger.io/)
- [OpenAPI](https://springdoc.org/)
- [JUnit](https://junit.org/junit5/)
- [RabbitMQ](https://www.rabbitmq.com/)

# Orientações sobre o teste

## Baixe as orientações 
- [Link para baixar as instruções](https://drive.google.com/file/d/1eauvYGpxh5Y45gToOZQT3HkDxKTgD-vG/view?usp=drive_link)

## Estrutura do Projeto

- `src/main/java/io/github/robsonfe/rentservice/model`: Contém as classes de modelo `Cliente` e `Locacao`.
- `src/main/java/io/github/robsonfe/rentservice/repository`: Contém as interfaces de repositório `ClienteRepository` e `LocacaoRepository`.
- `src/main/java/io/github/robsonfe/rentservice/service`: Contém a classe de serviço `GerenciadorLocacoes`.
- `src/main/java/io/github/robsonfe/rentservice/controller`: Contém a classe do controlador REST `LocadoraController`.
- `src/main/java/io/github/robsonfe/rentservice/config`: Contém a configuração do Swagger `SwaggerConfig`.

## Configuração do Ambiente

### Pré-requisitos

- Java 21
- Docker
- Docker Compose
- Maven

### Passos para Configuração

1. Clone o repositório:

   ```bash
   git clone https://github.com/seu-usuario/rentservice.git
   cd rentservice
   ```

2. Configure as propriedades do banco de dados no arquivo `application.properties`:

   ```properties
    spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
    spring.datasource.url=jdbc:mysql://localhost:3306/rentdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo
    spring.datasource.username=root
    spring.datasource.password=edna
   ```

3. Build o projeto:

   ```bash
   ./mvnw clean package
   ```

4. Suba os containers Docker:

   ```bash
   docker-compose up --build
   ```

5. Acesse a API via Swagger para testar os endpoints e documentação do projeto:

   ```
   http://localhost:8080/swagger-ui/index.html
   ```

## Testes

Para executar os testes, utilize o comando:

```bash
./mvnw test
```

## Autor

- [Robson Ferreira](https://github.com/RobsonFe)

---
