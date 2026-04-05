#!/bin/bash
set -e

# ============================================
#  TrackMaintenance - Setup Completo
#  Clona e sobe todas as aplicações via Docker
# ============================================

BACKEND_REPO="https://github.com/diegodanielsantana00/TrackMaintenance-Java-BackEnd.git"
FRONTEND_REPO="https://github.com/diegodanielsantana00/TrackMaintenance-NextJS-FrontEnd.git"
PROJECT_DIR="trackmaintenance-stack"

echo "============================================"
echo " TrackMaintenance - Setup Completo"
echo "============================================"
echo ""

# Verificar se Docker está instalado
if ! command -v docker &> /dev/null; then
    echo "ERRO: Docker não encontrado. Instale o Docker primeiro."
    echo "https://docs.docker.com/get-docker/"
    exit 1
fi

# Verificar se Docker Compose está disponível
if ! docker compose version &> /dev/null; then
    echo "ERRO: Docker Compose não encontrado."
    exit 1
fi

# Criar diretório do projeto
echo "[1/4] Criando diretório do projeto..."
mkdir -p "$PROJECT_DIR"
cd "$PROJECT_DIR"

# Clonar repositórios
echo "[2/4] Clonando repositórios..."

if [ -d "backend" ]; then
    echo "  Backend já existe. Atualizando..."
    cd backend && git pull && cd ..
else
    git clone "$BACKEND_REPO" backend
fi

if [ -d "frontend" ]; then
    echo "  Frontend já existe. Atualizando..."
    cd frontend && git pull && cd ..
else
    git clone "$FRONTEND_REPO" frontend
fi

# Gerar docker-compose unificado
echo "[3/4] Gerando docker-compose..."

cat > docker-compose.yml <<'EOF'
services:

  postgres:
    image: postgres:16-alpine
    container_name: trackmaintenance-postgres
    environment:
      POSTGRES_DB: trackmaintenance
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: root
    ports:
      - "5433:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres -d trackmaintenance"]
      interval: 10s
      timeout: 5s
      retries: 5
    networks:
      - trackmaintenance-net

  rabbitmq:
    image: rabbitmq:3.13-management-alpine
    container_name: trackmaintenance-rabbitmq
    environment:
      RABBITMQ_DEFAULT_USER: guest
      RABBITMQ_DEFAULT_PASS: guest
    ports:
      - "5672:5672"
      - "15672:15672"
    volumes:
      - rabbitmq_data:/var/lib/rabbitmq
    healthcheck:
      test: ["CMD", "rabbitmq-diagnostics", "ping"]
      interval: 10s
      timeout: 5s
      retries: 5
    networks:
      - trackmaintenance-net

  backend:
    build:
      context: ./backend
      dockerfile: Dockerfile
    container_name: trackmaintenance-backend
    environment:
      SPRING_PROFILES_ACTIVE: docker
    ports:
      - "3440:3440"
      - "5005:5005"
    depends_on:
      postgres:
        condition: service_healthy
      rabbitmq:
        condition: service_healthy
    networks:
      - trackmaintenance-net

  frontend:
    build:
      context: ./frontend
      dockerfile: Dockerfile.prod
      args:
        NEXT_PUBLIC_API_URL: http://localhost:3440
    container_name: trackmaintenance-frontend
    ports:
      - "3430:3430"
    environment:
      - NODE_ENV=production
    depends_on:
      - backend
    restart: always
    networks:
      - trackmaintenance-net

volumes:
  postgres_data:
  rabbitmq_data:

networks:
  trackmaintenance-net:
    driver: bridge
EOF

# Subir tudo
echo "[4/4] Subindo containers..."
docker compose up -d --build

echo ""
echo "============================================"
echo " Tudo rodando!"
echo "============================================"
echo ""
echo " Frontend:        http://localhost:3430"
echo " Backend API:     http://localhost:3440"
echo " Swagger Docs:    http://localhost:3440/swagger/index.html"
echo " RabbitMQ:        http://localhost:15672"
echo "                  (guest / guest)"
echo " PostgreSQL:      localhost:5433"
echo "                  (postgres / root)"
echo ""
echo " Para parar:  cd $PROJECT_DIR && docker compose down"
echo " Para logs:   cd $PROJECT_DIR && docker compose logs -f"
echo "============================================"
