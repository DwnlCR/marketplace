# Marketplace
 
Plataforma event-driven de marketplace de eventos desenvolvida em Java com Spring Boot 3.5.0, multi-datasource persistence e comunicação assíncrona entre bounded contexts via eventos de domínio.
 
## Funcionalidades
 
Interface REST para navegação e gerenciamento de eventos
Registro de clientes com publicação de eventos de domínio
Catálogo de eventos com metadados (setores, assentos, preços)
Caching em camadas com Redis (padrão cache-aside)
Armazenamento de eventos em MySQL e metadados em MongoDB
Validação em tempo real de dados de entrada
Enriquecimento assíncrono de eventos com metadados
Publicação de eventos entre bounded contexts
Consumo assíncrono de eventos com EventListener
Descoberta de padrões via Spring Data repositories
 
## Estrutura do Projeto
 
```
marketplace/
|
├── src/main/java/br/com/dwnl/marketplace/
|
├── catalog/
|   ├── application/
|   |   ├── BrowseShowcaseUseCase.java
|   |   ├── EventEnricher.java
|   |   └── dto/
|   |       └── EventOutput.java
|   ├── domain/
|   |   ├── Event.java
|   |   ├── EventMetadata.java
|   |   ├── EventRepository.java
|   |   └── EventMetadataRepository.java
|   └── infrastructure/
|       ├── persistence/
|       |   ├── entity/
|       |   |   ├── EventEntity.java
|       |   |   └── EventMetadataEntity.java
|       |   └── repository/
|       |       ├── JpaEventRepository.java
|       |       ├── MongoEventMetadataRepository.java
|       |       └── *EntityRepository.java
|       ├── event/
|       |   ├── EventListener.java
|       |   └── EventMetadataEventListener.java
|       ├── http/
|       |   └── ShowCaseController.java
|       └── CatalogConfiguration.java
|
├── registration/
|   ├── application/
|   ├── domain/
|   |   ├── Customer.java
|   |   ├── CustomerRepository.java
|   |   └── CustomerId.java
|   └── infrastructure/
|       ├── persistence/
|       |   ├── entity/
|       |   |   └── CustomerEntity.java
|       |   └── repository/
|       |       ├── JpaCustomerRepository.java
|       |       └── CustomerEntityRepository.java
|       └── event/
|           └── CustomerEventHandler.java
|
├── ticketing/
|   └── infrastructure/
|       └── event/
|           └── TicketingEventListener.java
|
├── common/
|   └── infrastructure/
|       └── event/
|           └── dto/
|               ├── CustomerCreated.java
|               └── EventUpdated.java
|
└── MarketplaceApplication.java
```
 
## Bounded Contexts
 
### Registration
 
Gerencia cadastro e autenticação de clientes. Responsável por validar dados de entrada, armazenar cliente em banco de dados e publicar evento CustomerCreated quando um novo cliente é criado. Utiliza MySQL como banco de dados relacional (porta 3307).
 
### Catalog
 
Gerencia eventos e seus metadados (setores, assentos, preços). Armazena eventos em MySQL (porta 3308) e metadados em MongoDB (porta 27018). Implementa cache-aside pattern com Redis (porta 6380) para otimizar leituras. Publica evento EventUpdated quando metadados são alterados.
 
### Ticketing
 
Consome eventos publicados por outros bounded contexts. Escuta CustomerCreated para criar conta de venda e EventUpdated para atualizar disponibilidade de ingressos. Processa eventos assincronamente com suporte a @Async.
 
## Padrões e Decisões de Design
 
| Conceito | Aplicacao |
|----------|-----------|
| Arquitetura Hexagonal | Model (domain/), Service (application/), Infrastructure (infrastructure/) |
| Bounded Contexts | Separação clara de domínios de negócio com linguagem ubíqua |
| Event-Driven | Comunicação assíncrona via ApplicationEventPublisher entre contextos |
| Cache-Aside | Leitura do cache ou banco de dados; invalidação em operações de escrita |
| Observer | NotifierService notifica listeners em eventos de cache |
| Multi-Datasource | Suporte a múltiplos bancos: MySQL (Registration/Catalog), MongoDB (Metadata), Redis (Cache) |
| Spring Data JPA | GamePatternRepository abstrai acesso ao banco de padrões |
| Serializable Records | Suporte a persistência em Redis de EventOutput, EventMetadataOutput, SeatOutput |
 
## Tecnologias
 
| Tecnologia | Versao | Uso |
|-----------|--------|-----|
| Java | 21 | Linguagem principal com Virtual Threads |
| Spring Boot | 3.5.0 | Framework principal e injeção de dependência |
| Spring Data JPA | 3.5.0 | Acesso a MySQL via repositório |
| Spring Data MongoDB | 3.5.0 | Persistência de metadados em MongoDB |
| Spring Data Redis | 3.5.0 | Caching distribuído em Redis |
| MySQL | 8.0 | Banco relacional para Registration e Catalog |
| MongoDB | 8.2 | Armazenamento de metadados de eventos |
| Redis | 7-alpine | Caching com TTL de 1 hora |
| Lombok | 1.18 | Redução de boilerplate em entidades |
| Docker Compose | latest | Orquestração de containers |
| Gradle | 8.x | Build tool |
 
## Como Executar
 
### Pré-requisitos
 
Java 21 ou superior
Docker e Docker Compose instalados
Git
 
### Configuração
 
Clone o repositório:
 
```bash
git clone https://github.com/DwnlCR/marketplace.git
cd marketplace
```
 
Configure as credenciais do banco em application.properties:
 
```properties
# Registration Database (MySQL)
registration.datasource.url=jdbc:mysql://localhost:3307/registration
registration.datasource.username=app
registration.datasource.password=app
 
# Catalog Database (MySQL)
catalog.datasource.url=jdbc:mysql://localhost:3308/catalog
catalog.datasource.username=app
catalog.datasource.password=app
 
# MongoDB
spring.mongodb.representation.uuid=standard
 
# Redis
spring.redis.host=localhost
spring.redis.port=6380
spring.cache.redis.time-to-live=3600000
```
 
### Executando a Aplicação
 
Inicie os containers (MySQL, MongoDB, Redis):
 
```bash
docker-compose up -d
```
 
Compile e execute a aplicação:
 
```bash
./gradlew clean build
./gradlew bootRun
```
 
A aplicação estará disponível em: http://localhost:8080
 
Verifique a saúde da aplicação:
 
```bash
curl http://localhost:8080/actuator/health
```
 
## Endpoints
 
### Catalog
 
Navegar catálogo (com cache):
 
```
GET /showcase
```
 
Resposta esperada:
 
```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "title": "Show Skank",
    "date": "2026-07-20T20:00:00Z",
    "metadata": {
      "eventDescription": "Clássico Rock",
      "seatsBySector": {
        "Pista": [
          {"id": "A1", "sectorId": "Pista", "price": 100.00}
        ],
        "VIP": [
          {"id": "V1", "sectorId": "VIP", "price": 250.00}
        ]
      }
    }
  }
]
```
 
### Health Check
 
```bash
curl http://localhost:8080/actuator/health
```
 
## Docker Compose
 
A aplicação utiliza os seguintes serviços Docker:
 
```yaml
services:
  registration-database:
    image: mysql:8.0
    ports: ["3307:3306"]
    environment:
      MYSQL_DATABASE: registration
      MYSQL_USER: app
      MYSQL_PASSWORD: app
 
  catalog-database:
    image: mysql:8.0
    ports: ["3308:3306"]
    environment:
      MYSQL_DATABASE: catalog
      MYSQL_USER: app
      MYSQL_PASSWORD: app
 
  catalog-metadata-database:
    image: mongo:8.2
    ports: ["27018:27017"]
 
  catalog-cache:
    image: redis:7-alpine
    ports: ["6380:6379"]
```
 
## Monitoramento
 
### Actuator Endpoints
 
Health check:
 
```bash
curl http://localhost:8080/actuator/health
```
 
Metrics:
 
```bash
curl http://localhost:8080/actuator/metrics
```
 
Info:
 
```bash
curl http://localhost:8080/actuator/info
```
 
### Logs
 
Ver logs em tempo real:
 
```bash
docker-compose logs -f
```
 
Ver logs de um serviço específico:
 
```bash
docker-compose logs -f catalog-database
```
 
## Troubleshooting
 
### Containers não iniciam
 
```bash
docker-compose down -v
docker-compose up -d
```
 
### Porta em uso
 
Modifique a porta em application.properties e docker-compose.yml:
 
```properties
spring.redis.port=6381
```
 
### Erro de conexão com Redis
 
Teste a conexão com Redis:
 
```bash
docker exec -it marketplace-catalog-cache-1 redis-cli ping
```
 
## Autor
 
Daniel Coelho Rodrigues
 
GitHub: DwnlCR
