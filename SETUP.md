## Setly Backend - Complete Setup & Installation Guide

This guide will walk you through setting up and running the Setly Splitwise clone application.

---

## Prerequisites

### System Requirements
- **Operating System**: Windows, macOS, or Linux
- **Java**: JDK 17 or higher
- **Maven**: 3.6.0 or higher (included via mvnw)
- **PostgreSQL**: 12 or higher
- **Git**: For version control (optional)

### Verify Prerequisites

#### Check Java Version
```bash
java -version
javac -version
```

Expected output: Java 17 or higher

#### Check Maven Version
```bash
mvn -version
```

Or use the Maven wrapper:
```bash
./mvnw -version
```

---

## Step-by-Step Installation

### 1. PostgreSQL Database Setup

#### Windows
1. Download PostgreSQL from https://www.postgresql.org/download/windows/
2. Run the installer and follow the wizard
3. Remember the password you set for the `postgres` user
4. Choose port 5432 (default)
5. Open pgAdmin 4 (included with PostgreSQL installer)

#### macOS (Using Homebrew)
```bash
brew install postgresql@15
brew services start postgresql@15
```

#### Linux (Ubuntu/Debian)
```bash
sudo apt-get install postgresql postgresql-contrib
sudo systemctl start postgresql
```

#### Create Database

Using **pgAdmin GUI**:
1. Right-click on "Databases"
2. Select "Create" > "Database"
3. Name it: `setly_db`
4. Click "Save"

Or using **Command Line**:
```bash
psql -U postgres
CREATE DATABASE setly_db;
\q
```

### 2. Project Setup

#### Clone or Download Project
```bash
# If using git
git clone <repository-url>
cd setly

# Or extract the downloaded folder
cd setly
```

#### Update Database Configuration

Edit `src/main/resources/application.yaml`:

```yaml
server:
  port: 8088

spring:
  application:
    name: setly
  datasource:
    url: jdbc:postgresql://localhost:5432/setly_db
    username: postgres
    password: <YOUR_POSTGRES_PASSWORD>  # Change this to your password

  jpa:
    hibernate:
      ddl-auto: update
    open-in-view: false
    show-sql: true
    properties:
      hibernate:
        format_sql: true
  flyway:
    enabled: true
    locations: classpath:db/migration

springdoc:
  api-docs:
    path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui.html

app:
  jwt:
    secret: mySecretKeyForJWTTokenGenerationPleaseChangeThisInProduction12345678901234567890
    expiration: 86400000  # 24 hours in milliseconds
```

**Important**: Change the JWT secret in production to a secure random string.

### 3. Build the Application

#### Using Maven Wrapper (Recommended)

**Windows:**
```bash
mvnw.cmd clean install
```

**macOS/Linux:**
```bash
./mvnw clean install
```

#### Using Maven (if installed globally)
```bash
mvn clean install
```

This command will:
- Download all dependencies
- Compile the source code
- Run tests
- Package the application

**Expected output**: `BUILD SUCCESS`

### 4. Run the Application

#### Using Maven Wrapper

**Windows:**
```bash
mvnw.cmd spring-boot:run
```

**macOS/Linux:**
```bash
./mvnw spring-boot:run
```

#### Using Java Command
```bash
java -jar target/setly-backend-0.0.1-SNAPSHOT.jar
```

#### Using IDE

**IntelliJ IDEA:**
1. Open the project
2. Right-click on `SetlyApplication.java`
3. Click "Run 'SetlyApplication.main()'"

**Eclipse:**
1. Open the project
2. Right-click on `SetlyApplication.java`
3. Run As > Java Application

**VS Code:**
1. Install "Extension Pack for Java"
2. Click Run on the main class

### 5. Verify Installation

#### Check Application Startup
Look for this log message:
```
Started SetlyApplication in X.XXX seconds
```

#### Test Health Endpoint
Open browser and visit:
```
http://localhost:8088/health
```

Expected response:
```json
{
  "status": "UP"
}
```

#### Access API Documentation
Open browser and visit:
```
http://localhost:8088/swagger-ui.html
```

You should see interactive API documentation.

---

## Database Setup via Flyway Migrations

When you start the application for the first time:

1. **Flyway** automatically runs migration scripts from `src/main/resources/db/migration/`
2. The `V1__init.sql` file creates all necessary tables:
   - `users`
   - `expense_groups`
   - `group_members`
   - `expenses`
   - `expense_splits`
   - `settlements`

3. Indexes are automatically created for performance optimization

**Verify Database Creation:**

Using pgAdmin:
1. Expand the `setly_db` database
2. Expand "Schemas" > "public" > "Tables"
3. You should see all 6 tables

Or using command line:
```bash
psql -U postgres -d setly_db -c "\dt"
```

---

## Project Structure Explanation

```
setly/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/kt/setly/
│   │   │       ├── SetlyApplication.java          # Entry point
│   │   │       ├── balance/                       # Balance calculation
│   │   │       ├── common/                        # Utilities, exceptions, responses
│   │   │       ├── config/                        # Security, OpenAPI config
│   │   │       ├── expense/                       # Expense module
│   │   │       ├── group/                         # Group management
│   │   │       ├── health/                        # Health checks
│   │   │       ├── security/                      # JWT security
│   │   │       └── user/                          # User management
│   │   └── resources/
│   │       ├── application.yaml                   # Configuration
│   │       └── db/migration/
│   │           └── V1__init.sql                   # Database schema
│   └── test/                                       # Test files
├── target/                                         # Build output (auto-generated)
├── pom.xml                                         # Maven configuration
├── mvnw                                            # Maven wrapper (macOS/Linux)
├── mvnw.cmd                                        # Maven wrapper (Windows)
├── README.md                                       # Full documentation
├── API_TESTING.http                               # API test requests
├── SETUP.md                                        # This file
└── HELP.md                                         # Additional help
```

---

## Common Issues & Troubleshooting

### Issue 1: "Connection refused" to database
```
org.postgresql.util.PSQLException: Connection to localhost:5432 refused
```

**Solution:**
1. Verify PostgreSQL is running:
   - Windows: Check Services panel
   - macOS: `brew services list`
   - Linux: `sudo systemctl status postgresql`
2. Verify database name, username, password in `application.yaml`
3. Verify database exists: `psql -U postgres -l`

### Issue 2: "Maven not found"
```
'mvn' is not recognized as an internal or external command
```

**Solution:**
- Use Maven wrapper instead: `mvnw.cmd` (Windows) or `./mvnw` (macOS/Linux)
- Or install Maven from https://maven.apache.org/download.cgi

### Issue 3: "Build failure" with compilation errors
```
[ERROR] COMPILATION ERROR
```

**Solution:**
1. Ensure Java 17+ is installed: `java -version`
2. Clean build: `./mvnw clean install`
3. Update IDE cache (IntelliJ: File > Invalidate Caches)

### Issue 4: Port 8088 already in use
```
Address already in use: bind
```

**Solution:**
- Find process using port: `netstat -ano | findstr :8088` (Windows)
- Change port in `application.yaml`: `server.port: 8089`
- Or kill the process using the port

### Issue 5: JWT token not working
```
Could not set user authentication
```

**Solution:**
- Verify JWT secret is set in `application.yaml`
- Check token format: `Authorization: Bearer <token>`
- Verify token hasn't expired (24 hours by default)

---

## Development Workflow

### 1. Start Database
```bash
# macOS with Homebrew
brew services start postgresql@15

# Windows - PostgreSQL service should auto-start
# Linux
sudo systemctl start postgresql
```

### 2. Start Application
```bash
./mvnw spring-boot:run
```

### 3. Test APIs
Use the `API_TESTING.http` file with:
- VS Code REST Client extension
- Postman
- Insomnia
- curl

### 4. Make Changes
Edit Java files and the server will recompile (with live reload enabled).

### 5. View Logs
Application logs are printed in the console during development.

---

## IDE Setup

### IntelliJ IDEA
1. Open project: File > Open > Select `pom.xml`
2. Trust the project
3. Maven will auto-download dependencies
4. Right-click `SetlyApplication.java` > Run

### Eclipse
1. Import project: File > Import > Existing Maven Projects
2. Select the project folder
3. Maven will auto-download dependencies
4. Right-click `SetlyApplication.java` > Run As > Java Application

### VS Code
1. Install extensions:
   - Extension Pack for Java
   - Postman (for API testing)
2. Open the project folder
3. Wait for Java language server to initialize
4. Press F5 to run

---

## Production Deployment

### Build for Production
```bash
./mvnw clean package -DskipTests
```

This creates: `target/setly-backend-0.0.1-SNAPSHOT.jar`

### Run in Production
```bash
java -Dspring.profiles.active=prod \
     -Dapp.jwt.secret=<your-secure-secret> \
     -Dspring.datasource.password=<your-db-password> \
     -jar target/setly-backend-0.0.1-SNAPSHOT.jar
```

### Docker Deployment (Optional)

Create `Dockerfile`:
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

Build and run:
```bash
docker build -t setly .
docker run -p 8088:8088 setly
```

---

## Environment Variables

Set these as environment variables for production:

```bash
# Database
SPRING_DATASOURCE_URL=jdbc:postgresql://prod-db:5432/setly_db
SPRING_DATASOURCE_USERNAME=setly_user
SPRING_DATASOURCE_PASSWORD=secure_password

# JWT
APP_JWT_SECRET=your-very-long-secure-random-string-here
APP_JWT_EXPIRATION=86400000

# Server
SERVER_PORT=8088
SERVER_SERVLET_CONTEXT_PATH=/api
```

---

## Next Steps

1. **Read** `README.md` for comprehensive API documentation
2. **Test** APIs using `API_TESTING.http`
3. **Explore** Swagger UI at `http://localhost:8088/swagger-ui.html`
4. **Customize** JWT secret and database credentials
5. **Deploy** to your server

---

## Support & Documentation

- **Full API Documentation**: See `README.md`
- **API Testing**: See `API_TESTING.http`
- **Swagger UI**: `http://localhost:8088/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8088/v3/api-docs`

---

## Success Indicators

✅ PostgreSQL running and accessible
✅ Application starts with "Started SetlyApplication in X.XXX seconds"
✅ Health check responds with status UP
✅ Swagger UI accessible at `/swagger-ui.html`
✅ Can register and login users
✅ Can create groups and add expenses
✅ Database tables created and populated

Enjoy building with Setly! 🚀

