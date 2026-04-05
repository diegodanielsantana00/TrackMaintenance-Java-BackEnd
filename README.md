# LogiTrack - Sistema de Gestão de Manutenção

> ⚠️ **Aviso Importante**: Este projeto foi desenvolvido para fins de **aplicação e demonstração de conhecimentos técnicos**. 
> 
> ⚠️ **Over Engineering Intencional**: Algumas decisões arquiteturais e tecnológicas podem parecer excessivas para o escopo do projeto, mas foram implementadas propositalmente para demonstrar domínio de diferentes tecnologias e patterns.

## Deploy e Demonstração

O sistema está disponível em produção para visualização e testes:

- **Frontend (Aplicação Web)**: [https://logitrack.danieldiegosantana.me/](https://logitrack.danieldiegosantana.me/)
- **API Backend**: [https://api-logitrack.danieldiegosantana.me/](https://api-logitrack.danieldiegosantana.me/)
- **Documentação da API**: [https://logitrack.danieldiegosantana.me/swagger/index.html](https://logitrack.danieldiegosantana.me/swagger/index.html)

## Sobre o Projeto

O LogiTrack é um sistema desenvolvido para gerenciar e acompanhar manutenções de veículos, utilizando tecnologias modernas como Java 17 e Spring Boot. O projeto adota uma arquitetura limpa (Clean Architecture) para garantir modularidade e facilidade de manutenção, além de utilizar Docker Compose para orquestração de containers. A segurança é reforçada com autenticação baseada em JWT (JSON Web Token), proporcionando um ambiente confiável e escalável.

## Como Rodar o Projeto

### Requisitos

- [Docker](https://docs.docker.com/get-docker/) (versão 20+)
- [Docker Compose](https://docs.docker.com/compose/install/) (v2+)
- [Git](https://git-scm.com/downloads)

### Setup Rápido (um comando)

O projeto oferece scripts automatizados que clonam os repositórios (backend + frontend) e sobem toda a stack via Docker Compose.

**Windows (CMD ou PowerShell):**

```bash
curl -sSL https://raw.githubusercontent.com/diegodanielsantana00/TrackMaintenance-Java-BackEnd/main/setup.cmd -o setup.cmd && setup.cmd
```

**Linux / macOS:**

```bash
curl -sSL https://raw.githubusercontent.com/diegodanielsantana00/TrackMaintenance-Java-BackEnd/main/setup.sh | bash
```

### Setup Manual

1. **Clone os repositórios:**

```bash
git clone https://github.com/diegodanielsantana00/TrackMaintenance-Java-BackEnd.git backend
git clone https://github.com/diegodanielsantana00/TrackMaintenance-NextJS-FrontEnd.git frontend
```

2. **Suba os containers do backend:**

```bash
cd backend
docker compose up -d
```

3. **Suba o frontend:**

```bash
cd ../frontend
docker compose up -d
```

### Serviços e Portas

| Serviço       | Porta  | Descrição                          |
|---------------|--------|------------------------------------|
| API Backend   | `8080` | Spring Boot (perfil Docker)        |
| PostgreSQL    | `5433` | Banco de dados (mapeado do 5432)   |
| RabbitMQ      | `5672` | Mensageria                         |
| RabbitMQ UI   | `15672`| Painel de gerenciamento do RabbitMQ|
| Debug (JVM)   | `5005` | Remote debug via JDWP              |