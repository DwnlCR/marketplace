# Marketplace
 
Plataforma event-driven de marketplace de eventos desenvolvida em Java com Spring Boot 3.5.0, multi-datasource persistence, caching distribuído e sistema de seat locking com Redis.
 
## Funcionalidades
 
Interface REST para navegação e gerenciamento de eventos
Registro de clientes com publicação de eventos de domínio
Catálogo de eventos com metadados (setores, assentos, preços)
Seleção de assentos com locking otimista distribuído
Validação de assentos disponíveis em tempo real
Caching em camadas com Redis (padrão cache-aside)
Armazenamento de eventos em múltiplos bancos de dados
Enriquecimento assíncrono de eventos com metadados
Publicação de eventos entre bounded contexts
Consumo assíncrono de eventos com EventListener
 
## Arquitetura
 
Três bounded contexts independentes com comunicação event-driven:
 
Registration Context
Gerencia cadastro e autenticação de clientes. Publica evento CustomerCreated quando um novo cliente é criado. Utiliza MySQL como banco de dados (porta 3307).
 
Catalog Context
Gerencia eventos e seus metadados (setores, assentos, preços). Armazena eventos em MySQL (porta 3308) e metadados em MongoDB (porta 27018). Implementa cache-aside pattern com Redis (porta 6380). Publica evento EventUpdated quando metadados são alterados.
 
Ticketing Context
Consome eventos de Registration e Catalog. Implementa seleção de assentos com locking otimista via Redis (porta 6381). Persiste eventos em PostgreSQL (porta 5433). Valida disponibilidade de assentos e controla reservas concorrentes.
 
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
|   └── infrastructure/
|       ├── persistence/
|       ├── event/
|       ├── http/
|       └── CatalogConfiguration.java
|
├── registration/
|   ├── application/
|   ├── domain/
|   └── infrastructure/
|       ├── persistence/
|       ├── event/
|       └── RegistrationConfiguration.java
|
├── ticketing/
|   ├── application/
|   |   ├── CreateCustomerUseCase.java
|   |   ├── CreateEventUseCase.java
|   |   └── SelectSeatUseCase.java
|   ├── domain/
|   |   ├── Customer.java
|   |   ├── Event.java
|   |   ├── Seat.java
|   |   ├── Sector.java
|   |   ├── EventRepository.java
|   |   └── *Exception.java
|   ├── infrastructure/
|   |   ├── persistence/
|   |   |   ├── entity/
|   |   |   |   ├── EventEntity.java
|   |   |   |   ├── SeatEntity.java
|   |   |   |   └── SeatLockEntity.java
|   |   |   └── repository/
|   |   |       ├── WorkOfUnitEventRepository.java
|   |   |       ├── RedisSeatLockRepository.java
|   |   |       └── *CrudRepository.java
|   |   ├── event/
|   |   ├── http/
|   |   |   ├── SeatSelectionController.java
|   |   |   └── request/
|   |   └── TicketingConfiguration.java
|
├── common/
|   └── infrastructure/
|       └── event/
|           └── dto/
|
└── MarketplaceApplication.java
```
 
## Padrões e Decisões de Design
 
Conceito | Aplicação
---------|----------
Arquitetura Hexagonal | Model (domain/), Service (application/), Infrastructure (infrastructure/)
Bounded Contexts | Separação clara de domínios: Registration, Catalog, Ticketing
Event-Driven | Comunicação assíncrona via ApplicationEventPublisher entre contextos
Cache-Aside | Leitura do cache ou banco; invalidação em operações de escrita
Optimistic Locking | Uso de Redis para lock transiente de assentos (30s TTL)
Work of Unit Pattern | Transação distribuída entre PostgreSQL e Redis para seat selection
Multi-Datasource | MySQL (Registration/Catalog), MongoDB (Metadata), PostgreSQL (Ticketing), Redis (Cache/Locks)
 
## Tecnologias
 
Tecnologia | Versão | Uso
-----------|--------|----
Java | 21 | Linguagem principal com Virtual Threads
Spring Boot | 3.5.0 | Framework principal
Spring Data JPA | 3.5.0 | Acesso a MySQL e PostgreSQL
Spring Data MongoDB | 3.5.0 | Persistência de metadados
Spring Data Redis | 3.5.0 | Caching e locking distribuído
MySQL | 8.0 | Banco relacional Registration e Catalog
MongoDB | 8.2 | Armazenamento de metadados de eventos
PostgreSQL | 18.3 | Banco relacional para Ticketing
Redis | 7-alpine | Caching e seat locking (2 instâncias)
Lombok | 1.18 | Redução de boilerplate
Docker Compose | latest | Orquestração de containers
Gradle | 8.x | Build tool
 
## Como Executar
 
Clone o repositório:
 
```bash
git clone https://github.com/DwnlCR/marketplace.git
cd marketplace
```
 
Configure as credenciais dos bancos em application.properties:
 
```properties
# Registration Database (MySQL)
registration.datasource.url=jdbc:mysql://localhost:3307/registration
registration.datasource.username=app
registration.datasource.password=app
 
# Catalog Database (MySQL)
catalog.datasource.url=jdbc:mysql://localhost:3308/catalog
catalog.datasource.username=app
catalog.datasource.password=app
 
# Ticketing Database (PostgreSQL)
ticketing.datasource.url=jdbc:postgresql://localhost:5433/ticketing
ticketing.datasource.username=app
ticketing.datasource.password=app
 
# MongoDB
spring.mongodb.representation.uuid=standard
 
# Redis Catalog Cache
catalog.redis.host=localhost
catalog.redis.port=6380
spring.cache.redis.time-to-live=3600000
 
# Redis Ticketing Locking
ticketing.redis.host=localhost
ticketing.redis.port=6381
```
 
Inicie os containers (MySQL, PostgreSQL, MongoDB, Redis):
 
```bash
docker-compose up -d
```
 
Compile e execute a aplicação:
 
```bash
./gradlew clean build
./gradlew bootRun
```
 
A aplicação estará disponível em: http://localhost:8080
 
## Endpoints
 
### Catalog
 
Navegar catálogo (com cache):
 
```
GET /showcase
```
 
### Ticketing
 
Selecionar assento com locking:
 
```
POST /ticketing/events/{eventId}/seats/select
Headers: X-CUSTOMER-ID: {customerId}
Body: {"id": "{seatId}"}
```
 
Resposta esperada (201 Created ou erro):
 
```
SeatNotFoundException - Assento não encontrado
SeatAlreadyReservedException - Assento já reservado
```
 
### Health Check
 
```bash
curl http://localhost:8080/actuator/health
```
 
## Docker Compose
 
A aplicação utiliza os seguintes serviços:
 
```yaml
services:
  registration-database:
    image: mysql:8.0
    ports: ["3307:3306"]
 
  catalog-database:
    image: mysql:8.0
    ports: ["3308:3306"]
 
  catalog-metadata-database:
    image: mongo:8.2
    ports: ["27018:27017"]
 
  catalog-cache:
    image: redis:7-alpine
    ports: ["6380:6379"]
 
  ticketing-database:
    image: postgres:18.3
    ports: ["5433:5432"]
 
  ticketing-locking:
    image: redis:7-alpine
    ports: ["6381:6379"]
```
 
## Seat Locking
 
O sistema implementa locking otimista para prevenir double-booking de assentos:
 
Fluxo de seleção de assento:
 
1. Cliente faz POST em /ticketing/events/{eventId}/seats/select
2. SelectSeatUseCase valida se assento existe
3. WorkOfUnitEventRepository tenta criar lock no Redis (30s TTL)
4. Se lock foi criado, assento é reservado
5. Se lock já existe, SeatAlreadyReservedException é lançada
O lock é armazenado em Redis (porta 6381) de forma independente do cache de catálogo (porta 6380) para evitar contenção.
 
## Monitoramento
 
Health check:
 
```bash
curl http://localhost:8080/actuator/health
```
 
Metrics:
 
```bash
curl http://localhost:8080/actuator/metrics
```
 
Logs em tempo real:
 
```bash
docker-compose logs -f
```
 
## Troubleshooting
 
Containers não iniciam:
 
```bash
docker-compose down -v
docker-compose up -d
```
 
Erro de conexão com PostgreSQL:
 
```bash
docker exec -it marketplace-ticketing-database-1 psql -U app -d ticketing -c "SELECT 1"
```
 
Erro de conexão com Redis:
 
```bash
docker exec -it marketplace-ticketing-locking-1 redis-cli ping
```
 
