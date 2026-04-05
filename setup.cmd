@echo off
chcp 65001 >nul 2>&1
setlocal enabledelayedexpansion

:: ============================================
::  TrackMaintenance - Setup Completo (Windows)
::  Clona e sobe todas as aplicacoes via Docker
:: ============================================

set "BACKEND_REPO=https://github.com/diegodanielsantana00/TrackMaintenance-Java-BackEnd.git"
set "FRONTEND_REPO=https://github.com/diegodanielsantana00/TrackMaintenance-NextJS-FrontEnd.git"
set "PROJECT_DIR=trackmaintenance-stack"

echo ============================================
echo  TrackMaintenance - Setup Completo
echo ============================================
echo.

:: Verificar Docker
where docker >nul 2>&1
if %errorlevel% neq 0 (
    echo ERRO: Docker nao encontrado. Instale o Docker primeiro.
    echo https://docs.docker.com/get-docker/
    exit /b 1
)

docker compose version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERRO: Docker Compose nao encontrado.
    exit /b 1
)

:: Criar diretorio
echo [1/4] Criando diretorio do projeto...
if not exist "%PROJECT_DIR%" mkdir "%PROJECT_DIR%"
cd "%PROJECT_DIR%"

:: Clonar repositorios
echo [2/4] Clonando repositorios...

if exist "backend" (
    echo   Backend ja existe. Atualizando...
    cd backend
    git pull
    cd ..
) else (
    git clone %BACKEND_REPO% backend
)

if exist "frontend" (
    echo   Frontend ja existe. Atualizando...
    cd frontend
    git pull
    cd ..
) else (
    git clone %FRONTEND_REPO% frontend
)

:: Gerar docker-compose unificado
echo [3/4] Gerando docker-compose...

(
echo services:
echo.
echo   postgres:
echo     image: postgres:16-alpine
echo     container_name: trackmaintenance-postgres
echo     environment:
echo       POSTGRES_DB: trackmaintenance
echo       POSTGRES_USER: postgres
echo       POSTGRES_PASSWORD: root
echo     ports:
echo       - "5433:5432"
echo     volumes:
echo       - postgres_data:/var/lib/postgresql/data
echo     healthcheck:
echo       test: ["CMD-SHELL", "pg_isready -U postgres -d trackmaintenance"]
echo       interval: 10s
echo       timeout: 5s
echo       retries: 5
echo     networks:
echo       - trackmaintenance-net
echo.
echo   rabbitmq:
echo     image: rabbitmq:3.13-management-alpine
echo     container_name: trackmaintenance-rabbitmq
echo     environment:
echo       RABBITMQ_DEFAULT_USER: guest
echo       RABBITMQ_DEFAULT_PASS: guest
echo     ports:
echo       - "5672:5672"
echo       - "15672:15672"
echo     volumes:
echo       - rabbitmq_data:/var/lib/rabbitmq
echo     healthcheck:
echo       test: ["CMD", "rabbitmq-diagnostics", "ping"]
echo       interval: 10s
echo       timeout: 5s
echo       retries: 5
echo     networks:
echo       - trackmaintenance-net
echo.
echo   backend:
echo     build:
echo       context: ./backend
echo       dockerfile: Dockerfile
echo     container_name: trackmaintenance-backend
echo     environment:
echo       SPRING_PROFILES_ACTIVE: docker
echo     ports:
echo       - "3440:3440"
echo       - "5005:5005"
echo     depends_on:
echo       postgres:
echo         condition: service_healthy
echo       rabbitmq:
echo         condition: service_healthy
echo     networks:
echo       - trackmaintenance-net
echo.
echo   frontend:
echo     build:
echo       context: ./frontend
echo       dockerfile: Dockerfile.prod
echo       args:
echo         NEXT_PUBLIC_API_URL: http://localhost:3440
echo     container_name: trackmaintenance-frontend
echo     ports:
echo       - "3430:3430"
echo     environment:
echo       - NODE_ENV=production
echo     depends_on:
echo       - backend
echo     restart: always
echo     networks:
echo       - trackmaintenance-net
echo.
echo volumes:
echo   postgres_data:
echo   rabbitmq_data:
echo.
echo networks:
echo   trackmaintenance-net:
echo     driver: bridge
) > docker-compose.yml

:: Subir tudo
echo [4/4] Subindo containers...
docker compose up -d --build

echo.
echo ============================================
echo  Tudo rodando!
echo ============================================
echo.
echo  Frontend:        http://localhost:3430
echo  Backend API:     http://localhost:3440
echo  Swagger Docs:    http://localhost:3440/swagger/index.html
echo  RabbitMQ:        http://localhost:15672
echo                   (guest / guest)
echo  PostgreSQL:      localhost:5433
echo                   (postgres / root)
echo.
echo  Para parar:  cd %PROJECT_DIR% ^&^& docker compose down
echo  Para logs:   cd %PROJECT_DIR% ^&^& docker compose logs -f
echo ============================================
pause
