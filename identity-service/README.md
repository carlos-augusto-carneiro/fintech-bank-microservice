# Fintech Identity Service

Este microsserviço é responsável pelo **gerenciamento de identidades, autenticação e perfil de usuários** da plataforma **Fintech**. Ele foi construído utilizando **Jakarta EE 8** rodando sobre **Open Liberty**, implementando padrões arquiteturais avançados como **CQRS (Command Query Responsibility Segregation)** e **Event Sourcing simplificado** para sincronização de dados.

---

## Arquitetura e Design

O projeto segue rigorosamente os princípios de **Clean Architecture** e **Domain-Driven Design (DDD)**, garantindo baixo acoplamento, alta coesão e facilidade de evolução.

As camadas são separadas em:

* **Domínio**: regras de negócio puras
* **Aplicação**: casos de uso e orquestração
* **Infraestrutura**: detalhes técnicos e integrações

---

## Fluxo de Dados (CQRS)

O sistema separa explicitamente as operações de **Escrita (Command)** das operações de **Leitura (Query)**, visando **performance, escalabilidade e isolamento de responsabilidades**.

### Command Side (Escrita)

* Recebe requisições de criação e alteração de usuários
* Persiste dados no **PostgreSQL** (Source of Truth)
* Publica **eventos de domínio** (`UserCreated`, `UserUpdated`) no **RabbitMQ**

### Event Processor

* Consome eventos de forma **assíncrona** via RabbitMQ
* Projeta dados **desnormalizados** no banco de leitura

### Query Side (Leitura)

* API de consulta lê diretamente do **MongoDB**
* Estrutura otimizada para **reads rápidos** e **buscas complexas**

---

## Tech Stack

* **Linguagem**: Java 8
* **Framework**: Jakarta EE 8

  * CDI 2.0
  * JAX-RS 2.1
  * JPA 2.2
  * JSON-B
* **Server**: Open Liberty

### Bancos de Dados

* **Write Model**: PostgreSQL (Relacional)
* **Read Model**: MongoDB (Document)

### Mensageria

* RabbitMQ

### Segurança

* Autenticação via **JWT (JJWT)**
* Criptografia de senhas no domínio

### Infraestrutura

* **Migração de banco**: Flyway (PostgreSQL)
* **Build**: Maven

---

## Estrutura do Projeto

A organização de pacotes reflete a arquitetura **Hexagonal / Clean Architecture**:

```
src/main/java/com/fintech/jakarta
├── api             # Controllers REST (JAX-RS)
├── application     # Casos de Uso e Orquestração
│   ├── dto         # Data Transfer Objects
│   ├── service     # Regras de Aplicação (Command / Query / Auth)
│   └── irepository # Interfaces de Repositórios (Inversão de Dependência)
├── domain          # Núcleo do Negócio
│   ├── entities    # Entidade User (rica e auto-validável)
│   └── valueobject # CPF, Email, Address, PasswordHash
└── infra           # Detalhes Técnicos
    ├── config      # Configurações (RabbitMQ, Flyway)
    ├── messaging   # Producers e Consumers (RabbitMQ)
    ├── persistence # Implementações JPA / Mongo
    └── security    # JWT e Criptografia
```

---

## Como Executar

### Pré-requisitos

* Java JDK 8
* Maven 3.6+
* Docker (para infraestrutura de apoio)

---

### 1. Subir Infraestrutura (Docker)

Certifique-se de que **PostgreSQL**, **MongoDB** e **RabbitMQ** estejam rodando.

Caso possua um `docker-compose.yml`:

```bash
docker-compose up -d
```

Caso rode manualmente, verifique as portas padrão:

* PostgreSQL: `5432`
* MongoDB: `27017`
* RabbitMQ: `5672`

---

### 2. Configuração

As configurações estão em:

```
src/main/resources/META-INF/microprofile-config.properties
```

Principais propriedades:

```properties
db.url
db.username
db.password

mongodb.connection.string

rabbitmq.host
```

As variáveis podem ser sobrescritas via **environment variables**.

---

### 3. Build e Run

O projeto utiliza o plugin do **Open Liberty** para desenvolvimento com **hot reload**.

```bash
# Build do projeto
mvn clean package

# Subir o servidor Open Liberty
mvn liberty:run
```

A aplicação estará disponível em:

```
http://localhost:9080/
```

---

## API Endpoints

### Autenticação

* `POST /auth/login`
  Retorna **Access Token** e **Refresh Token**

---

### Usuários (Command)

* `POST /users`
  Cria um novo usuário *(Postgres → Evento → Mongo)*

* `PUT /users/{id}`
  Atualiza dados cadastrais

* `DELETE /users/{id}`
  Realiza **Soft Delete** (inativação)

---

### Usuários (Query – MongoDB)

* `GET /users/{id}`
  Busca perfil detalhado

* `GET /users/search?cpf={cpf}`
  Busca por CPF

* `GET /users/search?email={email}`
  Busca por Email

* `GET /users/active`
  Lista usuários ativos

---

## Testes

O projeto segue a **Pirâmide de Testes**:

* **Testes Unitários**
  Foco em Domínio e Application Services
  *(JUnit 5 + Mockito)*

* **Testes de Integração** *(Planejado)*
  Validação de Repositórios e Mensageria
  *(Testcontainers)*

### Executar testes unitários

```bash
mvn test
```

---

## Observações

* Arquitetura preparada para **alta escalabilidade**
* Forte isolamento entre domínio e infraestrutura
* Pronto para evolução com **Event-Driven Architecture** e **Microservices**

---

