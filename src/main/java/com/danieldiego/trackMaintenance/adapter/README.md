# Adapter Layer


A camada **Adapter** é a porta de entrada da aplicação, responsável por receber as requisições HTTP/HTTPs, validar os dados de entrada e delegar para os casos de uso na camada **Application**. Ela contém os **Controllers REST**, os **DTOs de request/response** e os **wrappers de resposta padronizada**.
Na Arquitetura Hexagonal, esta camada representa os **Adapters de entrada (Driving/Primary Adapters)**.

---

## Estrutura

```
adapter/
├── controller/
├── dto/
```

---

## Resposta Padrão da API

Todas as respostas seguem um formato unificado usando `ApiResponse<T>`:

```json
{
  "status": 200,
  "message": "Descrição do resultado",
  "data": { ... },
  "timestamp": "2026-04-05T12:00:00"
}
```

Para listagens paginadas, usa-se `PagedApiResponse<T>`:

```json
{
  "status": 200,
  "message": "Descrição do resultado",
  "data": [ ... ],
  "page": 0,
  "size": 10,
  "totalElements": 25,
  "totalPages": 3,
  "timestamp": "2026-04-05T12:00:00"
}
```

Para erros:

```json
{
  "status": 404,
  "message": "Veiculo with id '99' not found",
  "timestamp": "2026-04-05T12:00:00"
}
```

---

## Mapa Completo de Endpoints

> **Base URL Local:** `http://localhost:3440`
>
> **Base URL Produção:** `https://api-logitrack.danieldiegosantana.me`
>
> **Swagger UI:** [https://api-logitrack.danieldiegosantana.me/swagger/index.html](https://api-logitrack.danieldiegosantana.me/swagger/index.html)

| Método | Endpoint | Auth | Descrição |
|--------|----------|------|-----------|
| `POST` | `/v1/auth/register` | Não | Registrar novo usuário |
| `POST` | `/v1/auth/login` | Não | Autenticar usuário |
| `GET` | `/v1/users/me` | JWT | Obter perfil do usuário autenticado |
| `POST` | `/v1/veiculos` | JWT | Criar veículo |
| `GET` | `/v1/veiculos` | JWT | Listar veículos (paginado) |
| `GET` | `/v1/veiculos/{id}` | JWT | Buscar veículo por ID |
| `PUT` | `/v1/veiculos/{id}` | JWT | Atualizar veículo |
| `DELETE` | `/v1/veiculos/{id}` | JWT | Remover veículo |
| `POST` | `/v1/manutencoes` | JWT | Agendar manutenção |
| `GET` | `/v1/manutencoes` | JWT | Listar manutenções (paginado) |
| `GET` | `/v1/manutencoes/{id}` | JWT | Buscar manutenção por ID |
| `GET` | `/v1/manutencoes/veiculo/{veiculoId}` | JWT | Listar manutenções de um veículo |
| `GET` | `/v1/manutencoes/status/{status}` | JWT | Filtrar manutenções por status |
| `PUT` | `/v1/manutencoes/{id}` | JWT | Atualizar manutenção |
| `PATCH` | `/v1/manutencoes/{id}/status` | JWT | Atualizar status da manutenção |
| `DELETE` | `/v1/manutencoes/{id}` | JWT | Remover manutenção |
| `GET` | `/v1/dashboard/total-km` | JWT | Total de km percorrido |
| `GET` | `/v1/dashboard/volume-por-categoria` | JWT | Volume por categoria de veículo |
| `GET` | `/v1/dashboard/cronograma-manutencao` | JWT | Cronograma de manutenções |
| `GET` | `/v1/dashboard/ranking-utilizacao` | JWT | Ranking de utilização dos veículos |
| `GET` | `/v1/dashboard/projecao-financeira` | JWT | Projeção financeira do mês |

---

## Controllers

### AuthController — `/v1/auth`

Endpoints públicos (não requerem JWT).

---

#### `POST /v1/auth/register` — Registrar Usuário

**Request Body:**
```json
{
  "name": "Diego Santana",
  "email": "contato@danieldiegosantana.me",
  "password": "P@ssw0rd123"
}
```

**Validações:**
| Campo | Regra |
|-------|-------|
| `name` | Obrigatório, 3–150 caracteres |
| `email` | Obrigatório, formato de email válido |
| `password` | Obrigatório, 8–100 caracteres |

**Response `201 Created`:**
```json
{
  "status": 201,
  "message": "User registered successfully",
  "data": {
    "userId": "550e8400-e29b-41d4-a716-446655440000",
    "name": "Diego Santana",
    "email": "contato@danieldiegosantana.me",
    "token": "eyJhbGciOiJIUzI1NiJ9..."
  },
  "timestamp": "2026-04-05T12:00:00"
}
```

**Erros possíveis:**
| Status | Descrição |
|--------|-----------|
| `400` | Dados inválidos (validação) |
| `409` | Email já cadastrado |

**cURL:**
```bash
curl -X POST http://localhost:3440/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Diego Santana",
    "email": "contato@danieldiegosantana.me",
    "password": "P@ssw0rd123"
  }'
```

---

#### `POST /v1/auth/login` — Autenticar Usuário

**Request Body:**
```json
{
  "email": "contato@danieldiegosantana.me",
  "password": "P@ssw0rd123"
}
```

**Validações:**
| Campo | Regra |
|-------|-------|
| `email` | Obrigatório, formato de email válido |
| `password` | Obrigatório |

**Response `200 OK`:**
```json
{
  "status": 200,
  "message": "Login successful",
  "data": {
    "userId": "550e8400-e29b-41d4-a716-446655440000",
    "name": "Diego Santana",
    "email": "contato@danieldiegosantana.me",
    "token": "eyJhbGciOiJIUzI1NiJ9..."
  },
  "timestamp": "2026-04-05T12:00:00"
}
```

**Erros possíveis:**
| Status | Descrição |
|--------|-----------|
| `400` | Dados inválidos |
| `401` | Credenciais inválidas |

**cURL:**
```bash
curl -X POST http://localhost:3440/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "contato@danieldiegosantana.me",
    "password": "P@ssw0rd123"
  }'
```

---

### UserController — `/v1/users`

Requer autenticação JWT via header `Authorization: Bearer <TOKEN>`.

---

#### `GET /v1/users/me` — Obter Perfil

**Response `200 OK`:**
```json
{
  "status": 200,
  "message": "User profile retrieved successfully",
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "Diego Santana",
    "email": "contato@danieldiegosantana.me",
    "createdAt": "2026-04-05T12:00:00"
  },
  "timestamp": "2026-04-05T12:00:00"
}
```

**Erros possíveis:**
| Status | Descrição |
|--------|-----------|
| `401` | Token ausente ou inválido |
| `404` | Usuário não encontrado |

**cURL:**
```bash
curl -X GET http://localhost:3440/v1/users/me \
  -H "Authorization: Bearer <TOKEN>"
```

---

### VeiculoController — `/v1/veiculos`

Todos os endpoints requerem autenticação JWT.

---

#### `POST /v1/veiculos` — Criar Veículo

**Request Body:**
```json
{
  "placa": "ABC1D23",
  "modelo": "Volvo FH 540",
  "tipo": "PESADO",
  "ano": 2023
}
```

**Validações:**
| Campo | Regra |
|-------|-------|
| `placa` | Obrigatório, máx 8 chars, formato antigo (`ABC-1234`) ou Mercosul (`ABC1D23`) |
| `modelo` | Obrigatório, máx 50 caracteres |
| `tipo` | Obrigatório, valores: `LEVE` ou `PESADO` |
| `ano` | Opcional |

**Response `201 Created`:**
```json
{
  "status": 201,
  "message": "Veículo criado com sucesso",
  "data": {
    "id": 1,
    "placa": "ABC1D23",
    "modelo": "Volvo FH 540",
    "tipo": "PESADO",
    "ano": 2023
  },
  "timestamp": "2026-04-05T12:00:00"
}
```

**Erros possíveis:**
| Status | Descrição |
|--------|-----------|
| `400` | Dados inválidos / placa formato incorreto |
| `409` | Placa já cadastrada |

**cURL:**
```bash
curl -X POST http://localhost:3440/v1/veiculos \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "placa": "ABC1D23",
    "modelo": "Volvo FH 540",
    "tipo": "PESADO",
    "ano": 2023
  }'
```

---

#### `GET /v1/veiculos` — Listar Veículos (Paginado)

**Query Parameters:**
| Param | Default | Descrição |
|-------|---------|-----------|
| `page` | `0` | Número da página (0-based) |
| `size` | `10` | Itens por página |

**Response `200 OK`:**
```json
{
  "status": 200,
  "message": "Veículos listados com sucesso",
  "data": [
    {
      "id": 1,
      "placa": "ABC1D23",
      "modelo": "Volvo FH 540",
      "tipo": "PESADO",
      "ano": 2023
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 1,
  "totalPages": 1,
  "timestamp": "2026-04-05T12:00:00"
}
```

**cURL:**
```bash
curl -X GET "http://localhost:3440/v1/veiculos?page=0&size=10" \
  -H "Authorization: Bearer <TOKEN>"
```

---

#### `GET /v1/veiculos/{id}` — Buscar por ID

**Response `200 OK`:**
```json
{
  "status": 200,
  "message": "Veículo encontrado",
  "data": {
    "id": 1,
    "placa": "ABC1D23",
    "modelo": "Volvo FH 540",
    "tipo": "PESADO",
    "ano": 2023
  },
  "timestamp": "2026-04-05T12:00:00"
}
```

**Erros:** `404` Veículo não encontrado

**cURL:**
```bash
curl -X GET http://localhost:3440/v1/veiculos/1 \
  -H "Authorization: Bearer <TOKEN>"
```

---

#### `PUT /v1/veiculos/{id}` — Atualizar Veículo

**Request Body:** (mesma estrutura do `POST`)
```json
{
  "placa": "ABC1D23",
  "modelo": "Volvo FH 560",
  "tipo": "PESADO",
  "ano": 2024
}
```

**Erros possíveis:**
| Status | Descrição |
|--------|-----------|
| `404` | Veículo não encontrado |
| `409` | Placa já em uso por outro veículo |

**cURL:**
```bash
curl -X PUT http://localhost:3440/v1/veiculos/1 \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "placa": "ABC1D23",
    "modelo": "Volvo FH 560",
    "tipo": "PESADO",
    "ano": 2024
  }'
```

---

#### `DELETE /v1/veiculos/{id}` — Remover Veículo

**Response:** `204 No Content`

**Erros:** `404` Veículo não encontrado

**cURL:**
```bash
curl -X DELETE http://localhost:3440/v1/veiculos/1 \
  -H "Authorization: Bearer <TOKEN>"
```

---

### ManutencaoController — `/v1/manutencoes`

Todos os endpoints requerem autenticação JWT.

---

#### `POST /v1/manutencoes` — Agendar Manutenção

**Request Body:**
```json
{
  "veiculoId": 1,
  "dataInicio": "2026-04-10",
  "dataFinalizacao": "2026-04-15",
  "tipoServico": "Troca de óleo e filtros",
  "custoEstimado": 1500.00
}
```

**Validações:**
| Campo | Regra |
|-------|-------|
| `veiculoId` | Obrigatório |
| `dataInicio` | Obrigatório |
| `dataFinalizacao` | Opcional (não pode ser anterior à `dataInicio`) |
| `tipoServico` | Obrigatório |
| `custoEstimado` | Deve ser maior que zero, máx R$ 1.000.000,00 |

**Response `201 Created`:**
```json
{
  "status": 201,
  "message": "Manutenção agendada com sucesso",
  "data": {
    "id": 1,
    "veiculoId": 1,
    "veiculoPlaca": "ABC1D23",
    "veiculoModelo": "Volvo FH 540",
    "dataInicio": "2026-04-10",
    "dataFinalizacao": "2026-04-15",
    "tipoServico": "Troca de óleo e filtros",
    "custoEstimado": 1500.00,
    "status": "PENDENTE"
  },
  "timestamp": "2026-04-05T12:00:00"
}
```

**Erros possíveis:**
| Status | Descrição |
|--------|-----------|
| `400` | Dados inválidos |
| `404` | Veículo não encontrado |

**cURL:**
```bash
curl -X POST http://localhost:3440/v1/manutencoes \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "veiculoId": 1,
    "dataInicio": "2026-04-10",
    "dataFinalizacao": "2026-04-15",
    "tipoServico": "Troca de óleo e filtros",
    "custoEstimado": 1500.00
  }'
```

---

#### `GET /v1/manutencoes` — Listar Manutenções (Paginado)

**Query Parameters:**
| Param | Default | Descrição |
|-------|---------|-----------|
| `page` | `0` | Número da página |
| `size` | `10` | Itens por página |

**Response `200 OK`:**
```json
{
  "status": 200,
  "message": "Manutenções listadas com sucesso",
  "data": [
    {
      "id": 1,
      "veiculoId": 1,
      "veiculoPlaca": "ABC1D23",
      "veiculoModelo": "Volvo FH 540",
      "dataInicio": "2026-04-10",
      "dataFinalizacao": "2026-04-15",
      "tipoServico": "Troca de óleo e filtros",
      "custoEstimado": 1500.00,
      "status": "PENDENTE"
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 1,
  "totalPages": 1,
  "timestamp": "2026-04-05T12:00:00"
}
```

**cURL:**
```bash
curl -X GET "http://localhost:3440/v1/manutencoes?page=0&size=10" \
  -H "Authorization: Bearer <TOKEN>"
```

---

#### `GET /v1/manutencoes/{id}` — Buscar por ID

**Response `200 OK`:**
```json
{
  "status": 200,
  "message": "Manutenção encontrada",
  "data": {
    "id": 1,
    "veiculoId": 1,
    "veiculoPlaca": "ABC1D23",
    "veiculoModelo": "Volvo FH 540",
    "dataInicio": "2026-04-10",
    "dataFinalizacao": "2026-04-15",
    "tipoServico": "Troca de óleo e filtros",
    "custoEstimado": 1500.00,
    "status": "PENDENTE"
  },
  "timestamp": "2026-04-05T12:00:00"
}
```

**Erros:** `404` Manutenção não encontrada

**cURL:**
```bash
curl -X GET http://localhost:3440/v1/manutencoes/1 \
  -H "Authorization: Bearer <TOKEN>"
```

---

#### `GET /v1/manutencoes/veiculo/{veiculoId}` — Por Veículo

Retorna todas as manutenções de um veículo específico.

**Response `200 OK`:**
```json
{
  "status": 200,
  "message": "Manutenções do veículo listadas",
  "data": [ ... ],
  "timestamp": "2026-04-05T12:00:00"
}
```

**cURL:**
```bash
curl -X GET http://localhost:3440/v1/manutencoes/veiculo/1 \
  -H "Authorization: Bearer <TOKEN>"
```

---

#### `GET /v1/manutencoes/status/{status}` — Por Status

Filtra manutenções pelo status informado.

**Valores válidos:** `PENDENTE`, `EM_REALIZACAO`, `CONCLUIDA`

**Response `200 OK`:**
```json
{
  "status": 200,
  "message": "Manutenções filtradas por status",
  "data": [ ... ],
  "timestamp": "2026-04-05T12:00:00"
}
```

**cURL:**
```bash
curl -X GET http://localhost:3440/v1/manutencoes/status/PENDENTE \
  -H "Authorization: Bearer <TOKEN>"
```

---

#### `PUT /v1/manutencoes/{id}` — Atualizar Manutenção

Atualiza todos os campos de uma manutenção, incluindo o status.

**Request Body:**
```json
{
  "dataInicio": "2026-04-10",
  "dataFinalizacao": "2026-04-18",
  "tipoServico": "Troca de óleo, filtros e correias",
  "custoEstimado": 2300.00,
  "status": "EM_REALIZACAO"
}
```

**Validações:**
| Campo | Regra |
|-------|-------|
| `dataInicio` | Obrigatório |
| `dataFinalizacao` | Opcional |
| `tipoServico` | Obrigatório |
| `custoEstimado` | Maior que zero |
| `status` | Obrigatório, deve respeitar a máquina de estados |

> **Máquina de estados:** `PENDENTE → EM_REALIZACAO → CONCLUIDA` (não permite retrocesso)

**Erros possíveis:**
| Status | Descrição |
|--------|-----------|
| `400` | Dados inválidos ou transição de status inválida |
| `404` | Manutenção não encontrada |

**cURL:**
```bash
curl -X PUT http://localhost:3440/v1/manutencoes/1 \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "dataInicio": "2026-04-10",
    "dataFinalizacao": "2026-04-18",
    "tipoServico": "Troca de óleo, filtros e correias",
    "custoEstimado": 2300.00,
    "status": "EM_REALIZACAO"
  }'
```

---

#### `PATCH /v1/manutencoes/{id}/status` — Atualizar Status

Atualiza apenas o status da manutenção, respeitando a máquina de estados.

**Request Body:**
```json
{
  "status": "CONCLUIDA"
}
```

**Erros possíveis:**
| Status | Descrição |
|--------|-----------|
| `400` | Transição de status inválida |
| `404` | Manutenção não encontrada |

**cURL:**
```bash
curl -X PATCH http://localhost:3440/v1/manutencoes/1/status \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"status": "CONCLUIDA"}'
```

---

#### `DELETE /v1/manutencoes/{id}` — Remover Manutenção

**Response:** `204 No Content`

**Erros:** `404` Manutenção não encontrada

**cURL:**
```bash
curl -X DELETE http://localhost:3440/v1/manutencoes/1 \
  -H "Authorization: Bearer <TOKEN>"
```

---

### DashboardController — `/v1/dashboard`

Todos os endpoints requerem autenticação JWT. Retornam indicadores e métricas da frota.

---

#### `GET /v1/dashboard/total-km` — Total de KM

Soma da quilometragem de um veículo específico ou de toda a frota.

**Query Parameters:**
| Param | Obrigatório | Descrição |
|-------|-------------|-----------|
| `veiculoId` | Não | ID do veículo (se omitido, retorna total da frota) |

**Response `200 OK`:**
```json
{
  "status": 200,
  "message": "Total de KM percorrido",
  "data": {
    "totalKm": 45230.50
  },
  "timestamp": "2026-04-05T12:00:00"
}
```

**cURL:**
```bash
curl -X GET "http://localhost:3440/v1/dashboard/total-km?veiculoId=1" \
  -H "Authorization: Bearer <TOKEN>"
```

---

#### `GET /v1/dashboard/volume-por-categoria` — Volume por Categoria

Quantidade de veículos por tipo (LEVE/PESADO) com percentuais.

**Response `200 OK`:**
```json
{
  "status": 200,
  "message": "Volume por categoria",
  "data": [
    { "tipo": "LEVE", "quantidade": 8, "percentual": 61.54 },
    { "tipo": "PESADO", "quantidade": 5, "percentual": 38.46 }
  ],
  "timestamp": "2026-04-05T12:00:00"
}
```

**cURL:**
```bash
curl -X GET http://localhost:3440/v1/dashboard/volume-por-categoria \
  -H "Authorization: Bearer <TOKEN>"
```

---

#### `GET /v1/dashboard/cronograma-manutencao` — Cronograma

Próximas 5 manutenções pendentes ou em realização.

**Response `200 OK`:**
```json
{
  "status": 200,
  "message": "Cronograma de manutenção",
  "data": [
    {
      "id": 1,
      "veiculoPlaca": "ABC1D23",
      "veiculoModelo": "Volvo FH 540",
      "tipoServico": "Troca de óleo",
      "dataInicio": "2026-04-10",
      "dataFinalizacao": "2026-04-15",
      "custoEstimado": 1500.00,
      "status": "PENDENTE"
    }
  ],
  "timestamp": "2026-04-05T12:00:00"
}
```

**cURL:**
```bash
curl -X GET http://localhost:3440/v1/dashboard/cronograma-manutencao \
  -H "Authorization: Bearer <TOKEN>"
```

---

#### `GET /v1/dashboard/ranking-utilizacao` — Ranking

Top 5 veículos por km percorrida.

**Response `200 OK`:**
```json
{
  "status": 200,
  "message": "Ranking de utilização",
  "data": [
    {
      "veiculoId": 1,
      "placa": "ABC1D23",
      "modelo": "Volvo FH 540",
      "tipo": "PESADO",
      "totalViagens": 12,
      "totalKm": 15430.00
    }
  ],
  "timestamp": "2026-04-05T12:00:00"
}
```

**cURL:**
```bash
curl -X GET http://localhost:3440/v1/dashboard/ranking-utilizacao \
  -H "Authorization: Bearer <TOKEN>"
```

---

#### `GET /v1/dashboard/projecao-financeira` — Projeção Financeira

Soma do custo total estimado em manutenções para o mês atual.

**Response `200 OK`:**
```json
{
  "status": 200,
  "message": "Projeção financeira",
  "data": {
    "mes": 4,
    "ano": 2026,
    "custoTotal": 8500.00,
    "totalManutencoes": 5
  },
  "timestamp": "2026-04-05T12:00:00"
}
```

**cURL:**
```bash
curl -X GET http://localhost:3440/v1/dashboard/projecao-financeira \
  -H "Authorization: Bearer <TOKEN>"
```

---

## DTOs de Request

### Autenticação

| DTO | Campos | Validações |
|-----|--------|------------|
| `RegisterRequest` | `name`, `email`, `password` | name: 3–150 chars; email: formato válido; password: 8–100 chars |
| `LoginRequest` | `email`, `password` | email: formato válido; ambos obrigatórios |

### Veículos

| DTO | Campos | Validações |
|-----|--------|------------|
| `VeiculoRequest` | `placa`, `modelo`, `tipo`, `ano` | placa: regex `ABC-1234` ou `ABC1D23`; modelo: máx 50; tipo: `LEVE`/`PESADO` |

### Manutenções

| DTO | Campos | Validações |
|-----|--------|------------|
| `CreateManutencaoRequest` | `veiculoId`, `dataInicio`, `dataFinalizacao`, `tipoServico`, `custoEstimado` | veiculoId e dataInicio obrigatórios; custo > 0 |
| `UpdateManutencaoRequest` | `dataInicio`, `dataFinalizacao`, `tipoServico`, `custoEstimado`, `status` | Todos exceto dataFinalizacao obrigatórios |
| `UpdateManutencaoStatusRequest` | `status` | Obrigatório: `PENDENTE`, `EM_REALIZACAO`, `CONCLUIDA` |

---

## Autenticação JWT

Para acessar endpoints protegidos, inclua o token JWT no header:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

O token é retornado nos endpoints `/v1/auth/register` e `/v1/auth/login` e expira em **24 horas** (`86400000ms`).

---

## Padrões Aplicados

| Padrão | Onde |
|--------|------|
| **Primary Adapter** | Controllers como adaptadores de entrada (Hexagonal) |
| **DTO Pattern** | Records Java como objetos de transferência de dados |
| **Request Validation** | Bean Validation via `@Valid` + Jakarta annotations |
| **Response Wrapper** | `ApiResponse<T>` e `PagedApiResponse<T>` padronizam todas as respostas |
| **OpenAPI/Swagger** | Anotações `@Operation`, `@Schema`, `@Tag` para documentação automática |
| **RESTful Conventions** | Uso correto de `POST/GET/PUT/PATCH/DELETE` e status codes HTTP |
