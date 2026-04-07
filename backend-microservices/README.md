# DIGIQUAL Backend Microservices

This folder contains a separated backend architecture with four Spring Boot microservices.

## Services

- auth-service
  - Port: 8091
  - Owns: /api/auth/**
  - Health: /health
- admin-service
  - Port: 8092
  - Owns: /api/admin/**
  - Health: /health
- partner-service
  - Port: 8093
  - Owns: /api/partner/**
  - Health: /health
- student-service
  - Port: 8094
  - Owns: /api/student/**
  - Health: /health
- gateway-service
  - Port: 8090
  - Owns: unified entrypoint for /api/**
  - Health: /health

## Build

Run each service independently:

- mvn -f backend-microservices/auth-service/pom.xml clean spring-boot:run
- mvn -f backend-microservices/admin-service/pom.xml clean spring-boot:run
- mvn -f backend-microservices/partner-service/pom.xml clean spring-boot:run
- mvn -f backend-microservices/student-service/pom.xml clean spring-boot:run
- mvn -f backend-microservices/gateway-service/pom.xml clean spring-boot:run

## Docker Compose (Recommended)

Run the full stack (Postgres + all services + gateway):

- Copy backend-microservices/.env.example to backend-microservices/.env and set JWT_SECRET.
- docker compose -f backend-microservices/docker-compose.yml up --build

Run in detached mode:

- docker compose -f backend-microservices/docker-compose.yml up --build -d

Stop and remove containers:

- docker compose -f backend-microservices/docker-compose.yml down

Stop and remove containers + database volume:

- docker compose -f backend-microservices/docker-compose.yml down -v

Health checks once running:

- http://localhost:8090/health
- http://localhost:8091/health
- http://localhost:8092/health
- http://localhost:8093/health
- http://localhost:8094/health

## Environment

Each service has its own default database URL in application.properties:

- auth-service: digiqual_auth_db
- admin-service: digiqual_admin_db
- partner-service: digiqual_partner_db
- student-service: digiqual_student_db

Override with standard env vars:

- DB_URL
- DB_USERNAME
- DB_PASSWORD
- DB_DRIVER
- DB_DIALECT
- JWT_SECRET

Gateway hardening variables:

- GATEWAY_RATE_LIMIT_ENABLED (default: true)
- GATEWAY_RATE_LIMIT_WINDOW_MS (default: 60000)
- GATEWAY_RATE_LIMIT_MAX_REQUESTS (default: 120)

Docker Compose also wires:

- AUTH_SERVICE_URL, ADMIN_SERVICE_URL, PARTNER_SERVICE_URL, STUDENT_SERVICE_URL (for gateway)

## Notes

- The original monolith backend folder is untouched.
- Controller ownership is split by service.
- Dev auto-seeding is disabled in the microservice copies.
- Compose creates databases automatically via docker/postgres/init/01-create-databases.sql.

## Service Security Enforcement

Each downstream service now enforces role checks even when accessed directly:

- admin-service: /api/admin/** requires ADMIN role
- partner-service: /api/partner/** requires PARTNER role
- student-service: /api/student/** requires STUDENT role
- auth-service: only /api/auth/** and /health are accessible

This prevents role bypass if internal service ports are reachable.

## Actuator Health

All services expose actuator health/info endpoints:

- /actuator/health
- /actuator/info

## Frontend Integration

Use frontend/.env.example as the template and configure:

- VITE_API_GATEWAY_BASE_URL=http://localhost:8090/api
- VITE_AUTH_API_BASE_URL=http://localhost:8091/api
- VITE_ADMIN_API_BASE_URL=http://localhost:8092/api
- VITE_PARTNER_API_BASE_URL=http://localhost:8093/api
- VITE_STUDENT_API_BASE_URL=http://localhost:8094/api

Frontend clients now resolve URLs in this order:

- Service-specific URL (if set)
- Gateway URL (if set)
- Built-in direct service default

## Gateway Edge Hardening

The gateway now enforces at the edge:

- Request logging: method, path, status, latency, client IP
- Rate limiting: per-client in-memory window limit on /api/** except /api/auth/**
- Route auth guard: JWT is required for /api/admin/**, /api/partner/**, /api/student/**
- Role enforcement:
  - /api/admin/** requires ADMIN token role
  - /api/partner/** requires PARTNER token role
  - /api/student/** requires STUDENT token role
