# Setly Environment Configuration Templates

This file contains example configurations for different environments.

---

## Development Environment (.env.dev)

```yaml
# Server Configuration
SERVER_PORT=8088
SERVER_SERVLET_CONTEXT_PATH=

# Database Configuration
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/setly_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres
SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver

# JPA Configuration
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_SHOW_SQL=true
SPRING_JPA_PROPERTIES_HIBERNATE_FORMAT_SQL=true

# Flyway Configuration
SPRING_FLYWAY_ENABLED=true
SPRING_FLYWAY_LOCATIONS=classpath:db/migration

# JWT Configuration
APP_JWT_SECRET=mySecretKeyForDevelopmentPleaseChangeInProduction
APP_JWT_EXPIRATION=86400000

# Logging
LOGGING_LEVEL_ROOT=INFO
LOGGING_LEVEL_COM_KT_SETLY=DEBUG

# Application Name
SPRING_APPLICATION_NAME=setly

# API Documentation
SPRINGDOC_API_DOCS_PATH=/v3/api-docs
SPRINGDOC_SWAGGER_UI_PATH=/swagger-ui.html
```

---

## Testing Environment (.env.test)

```yaml
# Server Configuration
SERVER_PORT=8089

# Database Configuration (Test Database)
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/setly_test
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres

# JPA Configuration
SPRING_JPA_HIBERNATE_DDL_AUTO=create-drop
SPRING_JPA_SHOW_SQL=false

# JWT Configuration
APP_JWT_SECRET=testSecretKeyForTesting
APP_JWT_EXPIRATION=3600000

# Logging
LOGGING_LEVEL_ROOT=WARN
LOGGING_LEVEL_COM_KT_SETLY=INFO

# Application Name
SPRING_APPLICATION_NAME=setly-test
```

---

## Staging Environment (.env.staging)

```yaml
# Server Configuration
SERVER_PORT=8080
SERVER_SERVLET_CONTEXT_PATH=/api

# Database Configuration
SPRING_DATASOURCE_URL=jdbc:postgresql://staging-db.example.com:5432/setly_db
SPRING_DATASOURCE_USERNAME=setly_user
SPRING_DATASOURCE_PASSWORD=your_staging_password_here
SPRING_DATASOURCE_HIKARI_MAXIMUM_POOL_SIZE=10

# JPA Configuration
SPRING_JPA_HIBERNATE_DDL_AUTO=validate
SPRING_JPA_SHOW_SQL=false
SPRING_JPA_OPEN_IN_VIEW=false

# Flyway Configuration
SPRING_FLYWAY_ENABLED=true

# JWT Configuration
APP_JWT_SECRET=your_very_secure_staging_secret_key_change_this_immediately
APP_JWT_EXPIRATION=86400000

# Logging
LOGGING_LEVEL_ROOT=WARN
LOGGING_LEVEL_COM_KT_SETLY=INFO

# Application Name
SPRING_APPLICATION_NAME=setly

# Performance
SPRING_JPA_PROPERTIES_HIBERNATE_JDBC_BATCH_SIZE=20
SPRING_JPA_PROPERTIES_HIBERNATE_ORDER_INSERTS=true
SPRING_JPA_PROPERTIES_HIBERNATE_ORDER_UPDATES=true

# Cache
SPRING_CACHE_TYPE=simple

# API Documentation
SPRINGDOC_API_DOCS_PATH=/v3/api-docs
SPRINGDOC_SWAGGER_UI_PATH=/swagger-ui.html
SPRINGDOC_SWAGGER_UI_ENABLED=false
```

---

## Production Environment (.env.prod)

```yaml
# Server Configuration
SERVER_PORT=8080
SERVER_SERVLET_CONTEXT_PATH=/api
SERVER_COMPRESSION_ENABLED=true
SERVER_COMPRESSION_MIN_RESPONSE_SIZE=1024

# Database Configuration
SPRING_DATASOURCE_URL=jdbc:postgresql://prod-db.example.com:5432/setly_db
SPRING_DATASOURCE_USERNAME=setly_prod_user
SPRING_DATASOURCE_PASSWORD=your_super_secure_production_password_here
SPRING_DATASOURCE_HIKARI_MAXIMUM_POOL_SIZE=20
SPRING_DATASOURCE_HIKARI_MINIMUM_IDLE=5
SPRING_DATASOURCE_HIKARI_CONNECTION_TIMEOUT=30000

# JPA Configuration
SPRING_JPA_HIBERNATE_DDL_AUTO=validate
SPRING_JPA_SHOW_SQL=false
SPRING_JPA_OPEN_IN_VIEW=false
SPRING_JPA_PROPERTIES_HIBERNATE_JDBC_BATCH_SIZE=25
SPRING_JPA_PROPERTIES_HIBERNATE_ORDER_INSERTS=true
SPRING_JPA_PROPERTIES_HIBERNATE_ORDER_UPDATES=true
SPRING_JPA_PROPERTIES_HIBERNATE_GENERATE_STATISTICS=false

# Flyway Configuration
SPRING_FLYWAY_ENABLED=true
SPRING_FLYWAY_OUT_OF_ORDER=false

# JWT Configuration
APP_JWT_SECRET=your_extremely_long_and_secure_production_secret_key_minimum_32_characters
APP_JWT_EXPIRATION=86400000

# Logging
LOGGING_LEVEL_ROOT=WARN
LOGGING_LEVEL_COM_KT_SETLY=WARN
LOGGING_FILE=logs/setly.log
LOGGING_FILE_MAX_SIZE=10MB
LOGGING_FILE_MAX_HISTORY=30
LOGGING_PATTERN_FILE=%d{yyyy-MM-dd HH:mm:ss} - %msg%n

# Application Name
SPRING_APPLICATION_NAME=setly

# Performance Tuning
SPRING_CACHE_TYPE=redis
SPRING_REDIS_HOST=redis.example.com
SPRING_REDIS_PORT=6379
SPRING_REDIS_TIMEOUT=60000

# Actuator (Health Checks)
MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE=health,metrics
MANAGEMENT_ENDPOINT_HEALTH_SHOW_DETAILS=when-authorized

# Security Headers
SERVER_SERVLET_SESSION_COOKIE_SECURE=true
SERVER_SERVLET_SESSION_COOKIE_HTTP_ONLY=true
SERVER_SERVLET_SESSION_COOKIE_SAME_SITE=Strict

# API Documentation (Disabled in Production)
SPRINGDOC_SWAGGER_UI_ENABLED=false
SPRINGDOC_API_DOCS_PATH=/v3/api-docs
SPRINGDOC_SHOW_ACTUATOR=false

# Monitoring
SPRING_APPLICATION_NAME=setly-prod
MANAGEMENT_METRICS_ENABLE_JVM=true
MANAGEMENT_METRICS_ENABLE_PROCESS=true
MANAGEMENT_METRICS_ENABLE_SYSTEM=true
```

---

## Docker Environment (.env.docker)

```yaml
# Server Configuration
SERVER_PORT=8088

# Database Configuration (Linked from Docker Compose)
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/setly_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=docker_postgres_password
SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver

# JPA Configuration
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_SHOW_SQL=false

# Flyway
SPRING_FLYWAY_ENABLED=true

# JWT Configuration
APP_JWT_SECRET=dockerSecretKeyPleaseChangeInProduction
APP_JWT_EXPIRATION=86400000

# Logging
LOGGING_LEVEL_ROOT=INFO

# Application Name
SPRING_APPLICATION_NAME=setly
```

---

## Docker Compose Example (docker-compose.yml)

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:15-alpine
    container_name: setly-postgres
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: docker_postgres_password
      POSTGRES_DB: setly_db
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres"]
      interval: 10s
      timeout: 5s
      retries: 5

  redis:
    image: redis:7-alpine
    container_name: setly-redis
    ports:
      - "6379:6379"
    healthcheck:
      test: ["CMD", "redis-cli", "ping"]
      interval: 10s
      timeout: 5s
      retries: 5

  setly-app:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: setly-app
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/setly_db
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: docker_postgres_password
      SPRING_REDIS_HOST: redis
      SPRING_REDIS_PORT: 6379
      APP_JWT_SECRET: dockerSecretKeyPleaseChangeInProduction
      APP_JWT_EXPIRATION: 86400000
    ports:
      - "8088:8088"
    depends_on:
      postgres:
        condition: service_healthy
      redis:
        condition: service_healthy
    volumes:
      - ./logs:/app/logs

volumes:
  postgres_data:
```

---

## How to Use These Configurations

### Development (Local)
```bash
# Use default application.yaml or set env variables
export SPRING_DATASOURCE_PASSWORD=your_password
./mvnw spring-boot:run
```

### Docker
```bash
docker-compose up
# Automatically uses environment variables defined in docker-compose.yml
```

### Production (Server)
```bash
# Export environment variables
export SPRING_DATASOURCE_URL=jdbc:postgresql://prod-db:5432/setly_db
export SPRING_DATASOURCE_USERNAME=setly_prod_user
export SPRING_DATASOURCE_PASSWORD=secure_password
export APP_JWT_SECRET=your_secure_secret_key

# Run JAR file
java -jar target/setly-backend-0.0.1-SNAPSHOT.jar
```

### Using .env File (Spring Boot 3.5+)
```bash
# Create .env file with configuration
# Spring Boot automatically loads it
./mvnw spring-boot:run
```

---

## Important Security Notes

### JWT Secret
- **Minimum length**: 32 characters
- **In production**: Use a cryptographically secure random string
- **Never commit**: Don't commit secrets to version control
- **Example generation**:
  ```bash
  # On Linux/macOS
  openssl rand -base64 32
  
  # On Windows PowerShell
  [Convert]::ToBase64String((1..32 | ForEach-Object {Get-Random -Maximum 256}))
  ```

### Database Credentials
- **Never use defaults** in production
- **Strong passwords**: Use 16+ characters with mixed case, numbers, symbols
- **Separate users**: Use different DB users for different environments
- **Encryption**: Use SSL for database connections in production

### Environment Variables
- Never commit `.env` files to version control
- Add `.env*` to `.gitignore`:
  ```
  .env
  .env.local
  .env.*.local
  ```

---

## Switching Environments

### Development to Staging
```bash
# Build
./mvnw clean package -DskipTests

# Push to staging
scp target/setly-backend-0.0.1-SNAPSHOT.jar user@staging:/app/

# SSH to server
ssh user@staging

# Deploy
export $(cat /app/.env.staging | xargs)
java -jar /app/setly-backend-0.0.1-SNAPSHOT.jar
```

### Staging to Production
```bash
# Same steps as above, but use production credentials
# and validate thoroughly in staging first
```

---

## Configuration Precedence

Spring Boot loads configuration in this order (lowest to highest priority):
1. `application.yaml` (default)
2. `application-{profile}.yaml` (environment-specific)
3. Environment variables
4. System properties
5. Command line arguments

---

## Health Check Endpoint

Verify deployment with health check:
```bash
curl -H "Authorization: Bearer your-token" \
  http://localhost:8088/health
```

Should respond:
```json
{
  "status": "UP"
}
```

---

## Monitoring & Logging

### Access Logs (Production)
```bash
tail -f /app/logs/setly.log
```

### Monitor Database Connection Pool
```bash
curl -H "Authorization: Bearer your-token" \
  http://localhost:8088/actuator/metrics/hikaricp.connections
```

---

For more information, see:
- `README.md` - Full documentation
- `SETUP.md` - Installation guide
- `QUICKSTART.md` - Quick start guide

