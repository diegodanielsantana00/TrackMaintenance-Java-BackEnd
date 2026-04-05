# Crosscutting Layer


A camada **Crosscutting** reúne as preocupações transversais da aplicação   tudo aquilo que **atravessa** as demais camadas sem pertencer a nenhuma delas. Aqui ficam as configurações do Spring, segurança (JWT + Spring Security), tratamento global de exceções e documentação OpenAPI/Swagger.

É nesta camada que ocorre a **"costura"** entre as camadas: os beans da Application são registrados, as portas são ligadas aos adapters de infraestrutura, e as regras de segurança são configuradas.

## Config

### BeanConfiguration

Responsável por registrar os **Services da camada Application** como beans do Spring. o registro é feito manualmente via `@Bean`:

```java
@Bean
public UserService userService(UserRepositoryPort repo, PasswordEncoderPort encoder, JwtTokenPort jwt) {
    return new UserServiceImpl(repo, encoder, jwt);
}

@Bean
public VeiculoService veiculoService(VeiculoRepositoryPort repo) { ... }

@Bean
public ManutencaoService manutencaoService(ManutencaoRepositoryPort manutencaoRepo, VeiculoRepositoryPort veiculoRepo) { ... }

@Bean
public DashboardService dashboardService(DashboardRepositoryPort repo) { ... }
```

---

### CrosscuttingConfiguration

Configuração central que importa todas as outras configs e define:

- **SystemInitializer** (`CommandLineRunner`)   loga os perfis ativos ao iniciar (exceto perfil `test`)
- **SystemConfigValidator**   valida na inicialização que `jwt.secret` e `spring.datasource.url` estão configurados

---

### DatabaseInitializerConfig

Cria automaticamente o banco de dados PostgreSQL se ele não existir. Executa **antes** do DataSource ser inicializado, usando `BeanFactoryPostProcessor` para garantir a ordem.

**Fluxo:**
```
1. Lê spring.datasource.url → extrai nome do banco
2. Conecta no banco "postgres" (base)
3. Verifica se o banco existe (pg_database)
4. Se não existe → CREATE DATABASE
5. DataSource é inicializado apontando para o banco criado
```

---

### JwtProperties

Record que mapeia as propriedades JWT do `application.properties`:

| Propriedade | Tipo | Descrição | Valor padrão |
|-------------|------|-----------|--------------|
| `jwt.secret` | `String` | Chave secreta para assinatura HMAC | Base64 encoded |
| `jwt.expiration` | `long` | Tempo de expiração em ms | `86400000` (24h) |

---

## Exception   Tratamento Global

## OpenAPI / Swagger

### OpenApiConfig

Configura a documentação automática da API com `springdoc-openapi`:

- **Título:** Track Maintenance API
- **Versão:** 1.0.0
- **Contato:** Diego Santana
- **Autenticação:** Bearer JWT configurado como SecurityScheme global

**URLs de acesso:**
| Recurso | URL |
|---------|-----|
| Swagger UI | `http://localhost:3440/swagger/index.html` |
| API Docs (JSON) | `http://localhost:3440/v3/api-docs` |
| Swagger UI (Produção) | [https://api-logitrack.danieldiegosantana.me/swagger/index.html](https://api-logitrack.danieldiegosantana.me/swagger/index.html) |

---

## Security

### SecurityConfig

Configura o Spring Security com as seguintes regras:

| Configuração | Valor |
|--------------|-------|
| **CORS** | Permite todas as origens, métodos e headers (com credentials) |
| **CSRF** | Desabilitado (API stateless) |
| **Sessão** | `STATELESS`   sem sessão no servidor |
| **Filtro JWT** | Adicionado antes do `UsernamePasswordAuthenticationFilter` |

**Rotas públicas (sem autenticação):**
| Método | Rota |
|--------|------|
| `POST` | `/v1/auth/login` |
| `POST` | `/v1/auth/register` |
| `*` | `/swagger/**` |
| `*` | `/v3/api-docs/**` |
| `*` | `/swagger-ui/**` |

**Todas as demais rotas** requerem autenticação JWT.

---

## Fluxo de Autenticação Completo

```
┌─────────────┐     ┌────────────────────┐     ┌──────────────┐
│   Cliente    │────►│ JwtAuthentication  │────►│  Controller  │
│  (Request)   │     │     Filter         │     │              │
└─────────────┘     └────────┬───────────┘     └──────────────┘
                             │
                    Token válido?
                    ┌────────┴────────┐
                    │                 │
                   SIM               NÃO
                    │                 │
            Seta userId no     Passa sem auth
            SecurityContext    (rota pública OK,
                               rota protegida → 401)
```

---

## Padrões Aplicados

| Padrão | Onde |
|--------|------|
| **Manual Bean Registration** | `BeanConfiguration`   Services registrados sem `@Service` |
| **Adapter Pattern** | `PasswordEncoderAdapter` implementa `PasswordEncoderPort` |
| **Filter Chain** | `JwtAuthenticationFilter` como `OncePerRequestFilter` |
| **Global Exception Handling** | `@RestControllerAdvice` centraliza tratamento de erros |
| **Configuration as Code** | `JwtProperties` como record + `@ConfigurationProperties` |
| **Database Auto-Setup** | `DatabaseInitializerConfig` cria banco automaticamente |
| **Separation of Concerns** | Cada subpacote (config, security, exception, openapi) tem responsabilidade isolada |
