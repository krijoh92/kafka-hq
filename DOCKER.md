# Docker Compose Setup

This setup includes all services needed to run the complete ACE system locally:

- **Kafka**: Message broker (ports: 9092, 9094)
- **AKHQ**: Kafka web UI for topic/message management (port: 8081)
- **ace-calculator**: Kafka Streams application for data processing
- **ace-backend**: Backend for Frontend (BFF) service (port: 8080)
- **webapp**: React frontend application (port: 3000)

## Quick Start

1. **Start all services:**
   ```bash
   docker-compose up -d
   ```

2. **View logs:**
   ```bash
   docker-compose logs -f
   ```

3. **Access services:**
   - Frontend: http://localhost:3000
   - Backend API: http://localhost:8080/api
   - H2 Console: http://localhost:8080/h2-console
   - AKHQ (Kafka UI): http://localhost:8081
   - Kafka (external): localhost:9094

## Environment Variables

The frontend can access the ace-backend service via these environment variables:

- `VITE_ACE_BACKEND_URL`: Base URL for the ace-backend service
- `VITE_API_BASE_URL`: API endpoint base URL

Copy `.env.example` to `.env` and modify as needed:

```bash
cp .env.example .env
```

## Development

The frontend is configured for **live reloading** during development. Changes to your React code will automatically be reflected in the browser.

```bash
# Start all services with development setup
docker-compose up -d

# View frontend logs to see Vite dev server
docker-compose logs -f webapp
```

### Frontend Development Features:
- **Bun-based**: Uses Bun instead of Node.js for faster builds
- **Live Reloading**: File changes trigger automatic browser refresh
- **Volume Mounting**: Source files are mounted for real-time updates
- **API Proxy**: Vite dev server proxies `/api` calls to ace-backend
- **Hot Module Replacement**: React components update without page refresh

## Useful Commands

```bash
# Stop all services
docker-compose down

# Rebuild specific service
docker-compose build ace-backend
docker-compose up -d ace-backend

# View specific service logs
docker-compose logs -f webapp

# Execute commands in running container
docker-compose exec ace-backend bash

# Scale services (if needed)
docker-compose up -d --scale ace-calculator=2
```

## Service Dependencies

- `ace-calculator` depends on `kafka` being healthy
- `ace-backend` depends on `kafka` being healthy
- `webapp` depends on `ace-backend` being healthy

## Kafka Topics

The following topics are used:
- `topic-input`: Input data for ace-calculator
- `topic-a`: Processed data from ace-calculator to ace-backend

### Managing Topics with AKHQ

Use AKHQ web interface at http://localhost:8081 to:
- View and create topics
- Send test messages
- Monitor consumer groups
- Browse topic messages
- View cluster information

## Health Checks

Services include health checks:
- **kafka**: Checks if Kafka topics can be listed
- **ace-backend**: Checks Spring Boot actuator health endpoint
- **webapp**: Nginx serves the built React app

## Troubleshooting

1. **Port conflicts**: Change ports in docker-compose.yml if needed
2. **Build issues**: Run `docker-compose build --no-cache` to force rebuild
3. **Volume issues**: Run `docker-compose down -v` to remove volumes
4. **Kafka connection issues**: Wait for Kafka to be fully ready (can take 30-60s)