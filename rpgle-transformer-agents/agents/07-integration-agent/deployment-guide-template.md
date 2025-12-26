# Deployment Guide: {PROGRAM}

## Overview
**Program Name**: {PROGRAM}
**Original RPGLE**: {PROGRAM}.rpgle
**Converted Services**: [List service names]
**Deployment Type**: Embedded / Standalone / Microservice
**Database**: DB2 / PostgreSQL / Other

## Prerequisites

### Backend (Java) Requirements
- Java 17 or higher
- Maven 3.8+
- Application Server: [Spring Boot embedded / Tomcat / etc.]

### Frontend (React) Requirements
- Node.js 16 or higher
- npm 8.0+ or yarn 1.22+
- Build tools: npm or yarn (included with Node.js)

### General Infrastructure Requirements
- Database: [DB2/PostgreSQL/etc.] version X.X

### Database Prerequisites
1. **Database Instance**:
   - Database name: [database_name]
   - Schema: [schema_name]
   - Character set: UTF-8

2. **Required Tables**:
   - List all tables this program uses
   - Note: Tables should be created from Phase 2 database layer

3. **Database User**:
   - Username: [app_user]
   - Required permissions: SELECT, INSERT, UPDATE, DELETE on [tables]

4. **Initial Data**:
   - List any required reference data
   - Indicate if migration from legacy system needed

### Network/Infrastructure
- Port requirements: [default 8080 or specify]
- External API access: [if any]
- File system access: [if any]

## Directory Structure

The project uses the following structure:
```
/final-output/
├── backend/                          # Java/Spring Boot application
│   ├── src/
│   ├── pom.xml
│   └── target/                       # Build output
└── frontend/                         # React application
    ├── src/
    ├── public/
    ├── package.json
    └── build/                        # Build output
```

## Configuration

### Backend Database Connection
Update `/final-output/backend/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://[HOST]:[PORT]/[DATABASE]
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: validate  # NEVER use 'create' or 'update' in production
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
    show-sql: false  # Set to true only in development

server:
  port: 8080
  servlet:
    context-path: /api

logging:
  level:
    com.company.modernization: INFO
    org.springframework: WARN
```

### Backend Environment Variables
Set these environment variables for the Java application:

```bash
# Database
export DB_USERNAME=your_db_user
export DB_PASSWORD=your_db_password

# Application
export SPRING_PROFILES_ACTIVE=production
export SERVER_PORT=8080

# Optional
export LOG_LEVEL=INFO
```

### Frontend Configuration
Update `/final-output/frontend/.env.production`:

```bash
# API endpoint pointing to backend
REACT_APP_API_URL=http://localhost:8080/api
# or for remote deployment:
# REACT_APP_API_URL=https://api.yourdomain.com/api

# Optional: Analytics, feature flags, etc.
REACT_APP_ENVIRONMENT=production
REACT_APP_LOG_LEVEL=info
```

### Frontend Environment Variables (Development)
Create `/final-output/frontend/.env.development`:

```bash
REACT_APP_API_URL=http://localhost:8080/api
REACT_APP_ENVIRONMENT=development
REACT_APP_LOG_LEVEL=debug
```

### Application Properties
Program-specific configuration:
- [List any program-specific settings]
- [API endpoints]
- [Business rule configurations]

## Build Instructions

### Backend Build

#### 1. Clone/Copy Source Code
```bash
cd /final-output/backend
```

#### 2. Build the Java Application
```bash
# Clean and compile
mvn clean compile

# Run tests (optional)
mvn test

# Package the application
mvn clean package
```

**Expected Output**: `target/{artifact-name}-{version}.jar` or `.war`

#### 3. Verify Backend Build
```bash
# Check the JAR/WAR was created
ls -lh target/*.jar

# Should see something like:
# modernization-app-1.0.0.jar
```

### Frontend Build

#### 1. Install Dependencies
```bash
cd /final-output/frontend
npm install
# or if using yarn:
# yarn install
```

#### 2. Build the React Application
```bash
# Production build
npm run build
# or if using yarn:
# yarn build
```

**Expected Output**: `/final-output/frontend/build/` directory with optimized production files

#### 3. Verify Frontend Build
```bash
# Check the build directory was created
ls -lh build/

# Should see directories like:
# static/  index.html  manifest.json  favicon.ico
```

### Full Build Pipeline (Optional)
Build both backend and frontend in sequence:

```bash
cd /final-output

# Build backend
cd backend
mvn clean package
cd ..

# Build frontend
cd frontend
npm install
npm run build
cd ..

echo "Build complete. Backend JAR: backend/target/*.jar, Frontend: frontend/build/"
```

## Deployment Steps

### Option A: Standalone JAR + Separate React (Recommended for Production)

#### Backend Deployment

1. **Copy JAR to deployment server**:
   ```bash
   scp backend/target/modernization-app-1.0.0.jar user@server:/opt/app/
   ```

2. **Create environment file** (on deployment server):
   ```bash
   cat > /opt/app/app.env << EOF
   DB_USERNAME=prod_user
   DB_PASSWORD=secure_password
   SPRING_PROFILES_ACTIVE=production
   SERVER_PORT=8080
   LOG_LEVEL=INFO
   EOF
   chmod 600 /opt/app/app.env
   ```

3. **Create systemd service file** `/etc/systemd/system/modernization-app.service`:
   ```ini
   [Unit]
   Description=Modernization Spring Boot Backend
   After=network.target

   [Service]
   Type=simple
   User=appuser
   ExecStart=/usr/bin/java -jar /opt/app/modernization-app-1.0.0.jar
   EnvironmentFile=/opt/app/app.env
   Restart=always
   RestartSec=10

   [Install]
   WantedBy=multi-user.target
   ```

4. **Enable and start the backend service**:
   ```bash
   sudo systemctl daemon-reload
   sudo systemctl enable modernization-app
   sudo systemctl start modernization-app
   ```

#### Frontend Deployment (Static Files)

1. **Copy React build to web server**:
   ```bash
   scp -r frontend/build/* user@server:/var/www/html/
   ```

2. **Set proper permissions**:
   ```bash
   sudo chown -R www-data:www-data /var/www/html/
   sudo chmod -R 755 /var/www/html/
   ```

3. **Configure nginx** (create `/etc/nginx/sites-available/modernization`):
   ```nginx
   server {
       listen 80;
       server_name localhost;  # or your domain

       root /var/www/html;
       index index.html index.htm;

       # Serve static files
       location /static/ {
           expires 1y;
           add_header Cache-Control "public, immutable";
       }

       # Proxy API requests to backend
       location /api/ {
           proxy_pass http://localhost:8080/api/;
           proxy_http_version 1.1;
           proxy_set_header Upgrade $http_upgrade;
           proxy_set_header Connection 'upgrade';
           proxy_set_header Host $host;
           proxy_cache_bypass $http_upgrade;
           proxy_set_header X-Real-IP $remote_addr;
           proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
           proxy_set_header X-Forwarded-Proto $scheme;
       }

       # SPA routing: redirect all non-file requests to index.html
       location / {
           try_files $uri $uri/ /index.html;
       }
   }
   ```

4. **Enable the nginx configuration**:
   ```bash
   sudo ln -s /etc/nginx/sites-available/modernization /etc/nginx/sites-enabled/
   sudo nginx -t  # Test configuration
   sudo systemctl restart nginx
   ```

### Option B: Standalone JAR with Embedded Frontend

If you want to embed the React build in the Java application:

1. **Copy React build to Spring Boot resources**:
   ```bash
   mkdir -p backend/src/main/resources/static
   cp -r frontend/build/* backend/src/main/resources/static/
   ```

2. **Configure Spring Boot** (`/final-output/backend/src/main/resources/application.yml`):
   ```yaml
   spring:
     web:
       resources:
         static-locations: classpath:/static/
   ```

3. **Rebuild backend**:
   ```bash
   cd backend
   mvn clean package
   ```

4. **Deploy single JAR**:
   ```bash
   scp backend/target/modernization-app-1.0.0.jar user@server:/opt/app/
   ```

5. **Frontend is accessible at**: `http://server:8080/`

### Option C: Docker Deployment

#### Build Docker images:

**Backend Dockerfile** (`/final-output/backend/Dockerfile`):
```dockerfile
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/modernization-app-1.0.0.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
```

**Frontend Dockerfile** (`/final-output/frontend/Dockerfile`):
```dockerfile
FROM nginx:alpine
COPY build /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

**Nginx configuration** (`/final-output/frontend/nginx.conf`):
```nginx
user nginx;
worker_processes auto;
error_log /var/log/nginx/error.log warn;
pid /var/run/nginx.pid;

events {
    worker_connections 1024;
}

http {
    include /etc/nginx/mime.types;
    default_type application/octet-stream;

    log_format main '$remote_addr - $remote_user [$time_local] "$request" '
                    '$status $body_bytes_sent "$http_referer" '
                    '"$http_user_agent" "$http_x_forwarded_for"';

    access_log /var/log/nginx/access.log main;
    sendfile on;
    keepalive_timeout 65;

    server {
        listen 80;
        root /usr/share/nginx/html;

        location /api/ {
            proxy_pass http://backend:8080/api/;
        }

        location / {
            try_files $uri $uri/ /index.html;
        }
    }
}
```

#### Build and run:
```bash
# Build backend image
cd backend
mvn clean package
docker build -t modernization-backend:1.0.0 .
cd ..

# Build frontend image
cd frontend
npm run build
docker build -t modernization-frontend:1.0.0 .
cd ..

# Run with docker-compose
cat > docker-compose.yml << 'EOF'
version: '3.8'
services:
  backend:
    image: modernization-backend:1.0.0
    ports:
      - "8080:8080"
    environment:
      DB_USERNAME: ${DB_USERNAME}
      DB_PASSWORD: ${DB_PASSWORD}
      SPRING_PROFILES_ACTIVE: production
    depends_on:
      - db

  frontend:
    image: modernization-frontend:1.0.0
    ports:
      - "80:80"
    depends_on:
      - backend

  db:
    image: postgres:14-alpine
    environment:
      POSTGRES_USER: ${DB_USERNAME}
      POSTGRES_PASSWORD: ${DB_PASSWORD}
      POSTGRES_DB: ${DB_NAME}
    volumes:
      - postgres_data:/var/lib/postgresql/data

volumes:
  postgres_data:
EOF

# Start services
docker-compose up -d
```

## Post-Deployment Verification

### 1. Backend Health Check
```bash
# Check backend application is running
curl http://localhost:8080/actuator/health

# Expected response: {"status":"UP"}
```

### 2. Frontend Health Check
```bash
# Check frontend is serving (should return index.html)
curl http://localhost/ | head -20

# Expected: HTML content starting with <!DOCTYPE html>

# Or open in browser:
# http://localhost/
```

### 3. Database Connection
```bash
# Check logs for successful database connection
tail -f /var/log/modernization-app.log | grep "HikariPool"

# Should see: "HikariPool-1 - Start completed"
```

### 4. Smoke Tests

#### Test Backend API Endpoints
```bash
# Example: Test customer inquiry
curl -X GET http://localhost:8080/api/customers/12345 \
  -H "Content-Type: application/json"

# Expected: Customer data JSON response
```

#### Test Database Operations
```bash
# Test read operation
curl -X GET http://localhost:8080/api/{resource}/{id}

# Test create operation (if applicable)
curl -X POST http://localhost:8080/api/{resource} \
  -H "Content-Type: application/json" \
  -d '{"field":"value"}'
```

#### Test Frontend UI
```bash
# Test frontend is loading correctly
curl -s http://localhost/ | grep -o '<title>.*</title>'

# Should return your application title

# Test API integration from frontend
# Open browser and check browser console (F12) for any errors
```

### 5. Cross-Layer Integration Tests
```bash
# Test frontend can call backend API
curl -X GET http://localhost/api/customers/12345

# This tests the nginx proxy routing to backend
```

### 6. Functional Tests
Run through these business scenarios:
- [ ] [Scenario 1: e.g., "Query customer by number"]
- [ ] [Scenario 2: e.g., "Display customer details"]
- [ ] [Scenario 3: e.g., "Update customer information"]
- [ ] [Frontend loads without JavaScript errors]
- [ ] [Frontend can successfully call backend APIs]
- [ ] [Add program-specific scenarios]

## Monitoring

### Backend Logs
```bash
# View backend application logs
tail -f /var/log/modernization-app.log

# Search for backend errors
grep ERROR /var/log/modernization-app.log

# View systemd service logs
sudo journalctl -u modernization-app -f
```

### Frontend Logs
```bash
# View nginx logs
tail -f /var/log/nginx/access.log    # Request logs
tail -f /var/log/nginx/error.log     # Error logs

# View browser console errors (in development)
# Open browser DevTools: F12 or Right-click > Inspect > Console tab
```

### Backend Performance Monitoring
- Monitor heap usage: `curl http://localhost:8080/actuator/metrics/jvm.memory.used`
- Monitor database connection pool
- Monitor response times

### Frontend Performance Monitoring
- Monitor page load time (browser DevTools > Network tab)
- Monitor Core Web Vitals (Largest Contentful Paint, Cumulative Layout Shift, etc.)
- Monitor bundle size and asset loading times

### Health Endpoints (Spring Boot Actuator)
- Health: `/actuator/health`
- Metrics: `/actuator/metrics`
- Info: `/actuator/info`

### Combined Monitoring Stack (Optional)
Consider implementing:
- **APM**: New Relic, DataDog, or Elastic APM
- **Logging**: ELK Stack (Elasticsearch, Logstash, Kibana)
- **Metrics**: Prometheus + Grafana
- **Frontend Monitoring**: Sentry for error tracking

## Troubleshooting

### Backend Issues

#### Application Won't Start
1. **Check Java version**: `java -version` (must be 17+)
2. **Check port availability**: `lsof -i :8080` (should be empty if port is free)
3. **Check database connectivity**:
   ```bash
   psql -h [HOST] -U [USER] -d [DATABASE]
   ```
4. **Review logs**: `sudo journalctl -u modernization-app -n 50`
5. **Verify configuration**: Check `/opt/app/app.env` values

#### Database Connection Errors
- Verify database is running: `sudo systemctl status postgresql`
- Check connection string in `application.yml`
- Verify credentials in `/opt/app/app.env`
- Check firewall rules: `sudo ufw allow 5432`
- Review database logs: `tail -f /var/log/postgresql/postgresql.log`

#### 404 Errors on API Calls
- Verify context-path configuration: `/api` in `application.yml`
- Check controller mapping annotations
- Review deployment logs: `sudo journalctl -u modernization-app`
- Test directly: `curl http://localhost:8080/api/health`

#### Backend Performance Issues
- Check database query performance: Enable `show-sql: true` (dev only)
- Review application logs for slow queries
- Monitor memory usage: `free -h`, `top`
- Check connection pool settings in `application.yml`

### Frontend Issues

#### Frontend Won't Load
1. **Check nginx is running**: `sudo systemctl status nginx`
2. **Check port 80 availability**: `sudo lsof -i :80`
3. **Check build directory exists**: `ls -l /var/www/html/`
4. **Verify nginx configuration**: `sudo nginx -t`
5. **Review nginx logs**: `tail -f /var/log/nginx/error.log`

#### 404 on Root Path
- Verify `index.html` exists: `ls -l /var/www/html/index.html`
- Check nginx try_files directive includes `/index.html`
- Clear browser cache: Ctrl+Shift+Delete (or Cmd+Shift+Delete on Mac)

#### API Calls from Frontend Failing
1. **Check CORS headers**: Backend should allow frontend domain
2. **Verify API URL in .env file**: `REACT_APP_API_URL` should point to backend
3. **Check backend is running**: `curl http://localhost:8080/actuator/health`
4. **Check nginx proxy configuration**: Verify `proxy_pass` directive
5. **Review browser console**: F12 > Console for error messages

#### Frontend JavaScript Errors
1. **Open browser DevTools**: F12
2. **Check Console tab** for error messages
3. **Check Network tab** for failed requests
4. **Look for CORS errors** or mixed content warnings
5. **Check source maps** if minified code is hard to debug

#### Build Size Issues
```bash
# Analyze React bundle size
cd frontend
npm run build -- --analyze
# or
npm install -g source-map-explorer
source-map-explorer 'build/static/js/*.js'
```

### Cross-Layer Issues

#### Frontend Can't Reach Backend
```bash
# Test backend accessibility from frontend server
curl http://localhost:8080/api/health

# If fails, check:
# 1. Backend is running: sudo systemctl status modernization-app
# 2. Port 8080 is listening: sudo lsof -i :8080
# 3. Firewall allows traffic: sudo ufw allow 8080
```

#### Nginx Not Proxying to Backend
```bash
# Test nginx configuration
sudo nginx -t

# Test backend reachability from nginx
curl http://localhost:8080/api/health

# Check nginx logs
tail -f /var/log/nginx/error.log
```

## Rollback Procedures

### Backend Rollback

#### 1. Stop Backend Service
```bash
sudo systemctl stop modernization-app
```

#### 2. Restore Previous Version
```bash
# Backup current version
sudo mv /opt/app/modernization-app-1.0.0.jar /opt/app/backups/

# Restore previous version
sudo cp /opt/app/backups/modernization-app-0.9.0.jar /opt/app/modernization-app-1.0.0.jar
```

#### 3. Restart Backend Service
```bash
sudo systemctl start modernization-app
```

#### 4. Verify Backend Rollback
```bash
# Check service is running
sudo systemctl status modernization-app

# Test health endpoint
curl http://localhost:8080/actuator/health
```

### Frontend Rollback

#### 1. Restore Previous Build
```bash
# Backup current build
sudo mv /var/www/html /var/www/html-new

# Restore previous build from backup
sudo cp -r /var/www/html-backup /var/www/html

# Fix permissions
sudo chown -R www-data:www-data /var/www/html
```

#### 2. Reload Nginx
```bash
sudo systemctl reload nginx
# or restart if reload doesn't work
sudo systemctl restart nginx
```

#### 3. Verify Frontend Rollback
```bash
# Check frontend is accessible
curl http://localhost/

# Verify no nginx errors
tail -f /var/log/nginx/error.log
```

### Complete Rollback (Both Layers)

```bash
#!/bin/bash
# Rollback both backend and frontend

echo "Rolling back backend..."
sudo systemctl stop modernization-app
sudo cp /opt/app/backups/modernization-app-0.9.0.jar /opt/app/modernization-app-1.0.0.jar
sudo systemctl start modernization-app

echo "Rolling back frontend..."
sudo mv /var/www/html /var/www/html-new
sudo cp -r /var/www/html-backup /var/www/html
sudo chown -R www-data:www-data /var/www/html
sudo systemctl reload nginx

echo "Verifying rollback..."
sleep 3
curl http://localhost:8080/actuator/health
curl http://localhost/
echo "Rollback complete"
```

### Database Rollback

If database schema changes were made and need to be rolled back:

```bash
# Restore from backup
sudo -u postgres psql -c "DROP DATABASE modernization_db;"
sudo -u postgres psql -f /backup/database-backup-0.9.0.sql
```

## Environment-Specific Settings

### Development

**Backend** (`/final-output/backend/src/main/resources/application.yml`):
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/modernization_dev
  jpa:
    show-sql: true
    hibernate:
      ddl-auto: update
logging:
  level:
    com.company.modernization: DEBUG
    org.hibernate.SQL: DEBUG
server:
  port: 8080
```

**Frontend** (`/final-output/frontend/.env.development`):
```bash
REACT_APP_API_URL=http://localhost:8080/api
REACT_APP_ENVIRONMENT=development
REACT_APP_LOG_LEVEL=debug
BROWSER=chrome
```

### UAT/Staging

**Backend** (`/final-output/backend/src/main/resources/application-uat.yml`):
```yaml
spring:
  datasource:
    url: jdbc:postgresql://staging-db.example.com:5432/modernization_uat
  jpa:
    show-sql: false
    hibernate:
      ddl-auto: validate
logging:
  level:
    com.company.modernization: INFO
server:
  port: 8080
```

**Frontend** (`/final-output/frontend/.env.staging`):
```bash
REACT_APP_API_URL=https://staging-api.example.com/api
REACT_APP_ENVIRONMENT=staging
REACT_APP_LOG_LEVEL=info
```

### Production

**Backend** (`/final-output/backend/src/main/resources/application-prod.yml`):
```yaml
spring:
  datasource:
    url: jdbc:postgresql://prod-db.example.com:5432/modernization_prod
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
  jpa:
    show-sql: false
    hibernate:
      ddl-auto: validate
logging:
  level:
    com.company.modernization: WARN
    org.springframework: WARN
  file:
    name: /var/log/modernization-app/app.log
    max-size: 100MB
    max-history: 30
server:
  port: 8080
  compression:
    enabled: true
    min-response-size: 1024
```

**Frontend** (`/final-output/frontend/.env.production`):
```bash
REACT_APP_API_URL=https://api.example.com/api
REACT_APP_ENVIRONMENT=production
REACT_APP_LOG_LEVEL=warn
REACT_APP_SENTRY_DSN=https://key@sentry.io/project
```

### Running with Specific Profile

**Backend**:
```bash
# Development (default)
java -jar modernization-app.jar --spring.profiles.active=dev

# UAT
java -jar modernization-app.jar --spring.profiles.active=uat

# Production
java -jar modernization-app.jar --spring.profiles.active=prod
```

**Frontend**:
```bash
# Development
npm start

# UAT Build
REACT_APP_API_URL=https://staging-api.example.com/api npm run build

# Production Build
REACT_APP_API_URL=https://api.example.com/api npm run build
```

## Security Considerations

### Backend Security
- [ ] Database credentials in environment variables (not in code)
- [ ] HTTPS/TLS configured
- [ ] Input validation enabled on all endpoints
- [ ] SQL injection prevention (JPA parameterized queries)
- [ ] Sensitive data not logged (passwords, tokens, PII)
- [ ] Security headers configured (CORS, CSP, X-Frame-Options)
- [ ] Rate limiting implemented
- [ ] Authentication/Authorization enabled
- [ ] Regular security dependency updates

### Frontend Security
- [ ] HTTPS/TLS enforced (redirect HTTP to HTTPS)
- [ ] Content Security Policy (CSP) headers configured
- [ ] X-Frame-Options to prevent clickjacking
- [ ] X-Content-Type-Options: nosniff
- [ ] Strict-Transport-Security (HSTS) enabled
- [ ] Sensitive data not stored in localStorage
- [ ] XSS protection (React handles this by default)
- [ ] CORS properly configured
- [ ] Regular npm dependency audits: `npm audit`
- [ ] No API keys or secrets in code or .env files

### HTTPS Configuration (Nginx)

```nginx
server {
    listen 80;
    server_name yourdomain.com;
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl http2;
    server_name yourdomain.com;

    ssl_certificate /etc/letsencrypt/live/yourdomain.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/yourdomain.com/privkey.pem;
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;

    # Security headers
    add_header Strict-Transport-Security "max-age=31536000; includeSubDomains" always;
    add_header X-Frame-Options "SAMEORIGIN" always;
    add_header X-Content-Type-Options "nosniff" always;
    add_header X-XSS-Protection "1; mode=block" always;
    add_header Referrer-Policy "strict-origin-when-cross-origin" always;

    root /var/www/html;
    index index.html;

    location /api/ {
        proxy_pass http://localhost:8080/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    location / {
        try_files $uri $uri/ /index.html;
    }
}
```

### Secrets Management

Never store secrets in code. Use environment variables:

```bash
# /opt/app/app.env (with restricted permissions: chmod 600)
DB_USERNAME=secure_user
DB_PASSWORD=secure_password
SPRING_PROFILES_ACTIVE=production
```

For frontend environment variables, use build-time configuration:

```bash
# .env.production (NEVER commit actual secrets)
REACT_APP_API_URL=https://api.example.com/api
```

## Support Information
- **Technical Contact**: [Name/Email]
- **Documentation**: `/final-output/docs/{PROGRAM}/`
- **Source Code**: [Repository URL]
- **Issue Tracking**: [URL]

## Appendix

### A. Complete Project Structure

```
/final-output/
├── backend/                              # Java Spring Boot Application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/company/modernization/
│   │   │   │   ├── controller/          # REST Controllers
│   │   │   │   ├── service/             # Business Logic
│   │   │   │   ├── repository/          # Data Access
│   │   │   │   ├── entity/              # JPA Entities
│   │   │   │   └── config/              # Configuration
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       ├── application-dev.yml
│   │   │       ├── application-uat.yml
│   │   │       ├── application-prod.yml
│   │   │       └── static/              # Embedded frontend (if applicable)
│   │   └── test/
│   ├── pom.xml                          # Maven configuration
│   ├── Dockerfile                       # Docker image for backend
│   └── target/                          # Build output
│       └── modernization-app-1.0.0.jar
│
├── frontend/                            # React Application
│   ├── src/
│   │   ├── components/                  # React Components
│   │   ├── pages/                       # Page Components
│   │   ├── services/                    # API Service Layer
│   │   ├── hooks/                       # Custom Hooks
│   │   ├── utils/                       # Utility Functions
│   │   ├── App.js                       # Root Component
│   │   └── index.js                     # Entry Point
│   ├── public/                          # Static Files
│   ├── .env.development                 # Dev Configuration
│   ├── .env.staging                     # Staging Configuration
│   ├── .env.production                  # Prod Configuration
│   ├── package.json                     # npm Dependencies
│   ├── Dockerfile                       # Docker image for frontend
│   ├── nginx.conf                       # Nginx configuration for Docker
│   └── build/                           # Build output
│
├── docker-compose.yml                   # Compose config for local deployment
├── docs/                                # Documentation
│   └── {PROGRAM}/                       # Program-specific docs
└── README.md                            # Project README
```

### B. API Endpoint Reference
List all REST endpoints for this program:

| Method | Endpoint | Description | Request | Response |
|--------|----------|-------------|---------|----------|
| GET | /api/resource/{id} | Get by ID | - | Resource JSON |
| POST | /api/resource | Create | Resource JSON | Created resource |
| PUT | /api/resource/{id} | Update | Resource JSON | Updated resource |
| DELETE | /api/resource/{id} | Delete | - | 204 No Content |

### C. Database Schema
Reference to database schema documentation from Phase 2.

### D. Business Rules
Key business rules implemented in this program:
- [Rule 1]
- [Rule 2]
- [Reference: conversion-notes.md for complete list]

### E. Known Limitations
Document any known limitations or differences from RPGLE original:
- [Limitation 1]
- [Limitation 2]

### F. Quick Reference: Common Commands

**Building**:
```bash
# Build backend
cd backend && mvn clean package

# Build frontend
cd frontend && npm run build

# Build both
./build-all.sh  # if script exists
```

**Deploying**:
```bash
# Start backend service
sudo systemctl start modernization-app

# Restart frontend
sudo systemctl restart nginx

# View logs
sudo journalctl -u modernization-app -f
tail -f /var/log/nginx/access.log
```

**Testing**:
```bash
# Backend health
curl http://localhost:8080/actuator/health

# Frontend
curl http://localhost/

# API endpoint
curl http://localhost:8080/api/customers
```

**Troubleshooting**:
```bash
# Check services status
sudo systemctl status modernization-app
sudo systemctl status nginx

# Check ports
sudo lsof -i :8080
sudo lsof -i :80

# Clear browser cache
# Chrome: Ctrl+Shift+Delete (Cmd+Shift+Delete on Mac)
# Firefox: Ctrl+Shift+Delete (same on Mac)
```

### G. File Locations Reference

| Item | Location |
|------|----------|
| Backend JAR | `/opt/app/modernization-app-1.0.0.jar` |
| Backend Config | `/opt/app/app.env` |
| Backend Service | `/etc/systemd/system/modernization-app.service` |
| Backend Logs | `/var/log/modernization-app.log` |
| Frontend Files | `/var/www/html/` |
| Nginx Config | `/etc/nginx/sites-available/modernization` |
| Nginx Logs | `/var/log/nginx/access.log`, `/var/log/nginx/error.log` |
| Database Backup | `/backup/database-backup-*.sql` |

---

**Document Version**: 1.0
**Last Updated**: {DATE}
**Updated By**: Integration Agent
