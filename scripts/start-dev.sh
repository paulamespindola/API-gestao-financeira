#!/usr/bin/env bash
set -e

echo "🚀 Subindo ambiente..."
docker compose --env-file .env down -v
docker compose --env-file .env up -d --build

echo "⏳ Aguardando Postgres..."
until docker exec postgres pg_isready -U admin > /dev/null 2>&1; do
  sleep 2
done

echo "🗄️ Criando bancos..."
docker exec postgres psql -U admin -c "CREATE DATABASE users_db;" || true
docker exec postgres psql -U admin -c "CREATE DATABASE transaction_db;" || true

echo "✅ Ambiente pronto!"
