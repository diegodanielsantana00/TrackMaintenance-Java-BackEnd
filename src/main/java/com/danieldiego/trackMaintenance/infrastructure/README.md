# Infrastructure Layer

A camada **Infrastructure** é responsável por toda a comunicação com o mundo externo: banco de dados, geração de tokens JWT e integrações de terceiros. Na Arquitetura Hexagonal, esta camada contém os **Adapters de saída (Driven/Secondary Adapters)** que implementam as portas definidas na camada Application.

Aqui ficam as **entidades JPA**, os **repositórios Spring Data**, os **adapters de persistência** e o **adapter de JWT** ,  tudo que depende de framework e tecnologia.

## Migrações Flyway

O banco de dados é versionado com **Flyway**. As migrações ficam em `src/main/resources/db/`:

### Migrations (`db/migration/`)

| Versão | Arquivo | Descrição |
|--------|---------|-----------|
| V1 | `V1__create_users_table.sql` | Cria tabela `users` com UUID, email único |
| V2 | `V2__create_veiculos_table.sql` | Cria tabela `veiculos` com CHECK tipo |
| V3 | `V3__create_viagens_table.sql` | Cria tabela `viagens` com FK para veiculos |
| V4 | `V4__create_manutencoes_table.sql` | Cria tabela `manutencoes` com FK para veiculos |
| V8 | `V8__fix_id_column_types.sql` | Altera IDs de `SERIAL` para `BIGINT` |

### Seeds (`db/seed/`)

| Versão | Arquivo | Descrição |
|--------|---------|-----------|
| V5 | `V5__seed_veiculos.sql` | 4 veículos iniciais (Fiorino, Volvo FH, Sprinter, Scania) |
| V6 | `V6__seed_viagens.sql` | 3 viagens com km percorrida |
| V7 | `V7__seed_manutencoes.sql` | 3 manutenções (2 pendentes, 1 concluída) |

---

## Diagrama de Relacionamentos (ER)

```
┌──────────┐       ┌──────────────┐       ┌──────────┐
│  users   │       │   veiculos   │       │ viagens  │
│──────────│       │──────────────│       │──────────│
│ id (UUID)│       │ id (BIGINT)  │◄──┐   │ id       │
│ name     │       │ placa        │   │   │ veiculo_id│──►│
│ email    │       │ modelo       │   ├───│ data_saida│
│ password │       │ tipo         │   │   │ km_perco. │
│ created  │       │ ano          │   │   └──────────┘
│ updated  │       └──────────────┘   │
└──────────┘              │           │   ┌────────────┐
                          │           └───│manutencoes │
                          │               │────────────│
                          └──────FK───────│ veiculo_id │
                                          │ data_inicio│
                                          │ tipo_serv. │
                                          │ custo_est. │
                                          │ status     │
                                          └────────────┘
```

---

## Tecnologias desta Camada

| Tecnologia | Uso |
|------------|-----|
| **Spring Data JPA** | Repositórios e abstração ORM |
| **Hibernate** | Implementação JPA (mapeamento objeto-relacional) |
| **PostgreSQL** | Banco de dados relacional |
| **Flyway** | Versionamento e migrações de schema |
| **JdbcTemplate** | SQL nativo para consultas analíticas |
| **JJWT 0.12.6** | Geração e validação de tokens JWT |
| **Lombok** | Boilerplate (getters, setters, builder) nas entities |
