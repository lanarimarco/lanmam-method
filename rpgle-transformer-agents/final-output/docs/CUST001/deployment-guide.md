# Deployment Guide: CUST001 - Customer Inquiry

## Overview
**Program Name**: CUST001
**Original RPGLE**: CUST001.rpgle
**Converted Services**: Customer Inquiry Service
**Deployment Type**: Spring Boot Microservice + React SPA
**Database**: DB2 (Production/UAT) / H2 (Development)

## Prerequisites

### Backend (Java) Requirements
- Java 17 or higher
- Maven 3.8+
- Application Server: Spring Boot embedded Tomcat

### Frontend (React) Requirements
- Node.js 16 or higher
- npm 8.0+ or yarn 1.22+

### Database Prerequisites

#### Development Environment
- H2 Database (embedded, in-memory)
- No external database setup required
- MODE=DB2 compatibility enabled

#### UAT/Production Environments
1. **Database Instance**:
   - Database: DB2
   - Schema: CUSTMAST
   - Character set: UTF-8

2. **Required Tables**:
   - **CUSTMAST** - Customer Master File
     - Fields: CUSTNBR (Customer Number), CUSTNAME (Name), CUSTADDR (Address), CUSTCITY (City), CUSTSTAT (State), CUSTZIP (Zip), CUSTPHON (Phone)

3. **Database User**:
   - Required permissions: SELECT on CUSTMAST table
   - Read-only access sufficient for Customer Inquiry

4. **Initial Data**:
   - Customer master data should already exist in CUSTMAST table
   - No migration needed if existing DB2 database is used

### Network/Infrastructure
- Port 8080: Backend Spring Boot application
- Port 80/443: Frontend web server (nginx)
- Database connectivity: DB2 port 50000 (UAT/Production)

## Directory Structure

```
/final-output/
├── backend/                          # Java/Spring Boot application
│   ├── src/
│   │   ├── main/java/com/lanarimarco/modernization/
│   │   │   ├── ModernizationApplication.java
│   │   │   ├── config/WebConfig.java
│   │   │   ├── entities/Customer.java
│   │   │   ├── repositories/CustomerRepository.java
│   │   │   ├── services/CustomerInquiryService.java
│   │   │   └── controllers/CustomerInquiryController.java
│   │   ├── resources/
│   │   │   ├── application.yml
│   │   │   ├── application-dev.yml
│   │   │   ├── application-uat.yml
│   │   │   └── application-prod.yml
│   │   └── test/
│   ├── pom.xml
│   └── target/                       # Build output
├── frontend/                         # React application
│   ├── src/
│   │   ├── pages/CustomerInquiry/
│   │   ├── services/api/
│   │   ├── types/
│   │   ├── App.tsx
│   │   └── index.tsx
│   ├── public/
│   ├── .env.development
│   ├── .env.production
│   ├── package.json
│   └── build/                        # Build output
└── docs/CUST001/                     # Documentation
```

## Configuration

### Backend Database Connection

The application uses Spring Profiles for environment-specific configuration:

#### Development (`application-dev.yml`)
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:devdb;MODE=DB2
    driver-class-name: org.h2.Driver
    username: sa
    password:
  h2:
    console:
      enabled: true
      path: /h2-console
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: create-drop
    show-sql: true
```

#### UAT (`application-uat.yml`)
```yaml
spring:
  datasource:
    url: ${DB_URL:jdbc:db2://uat-db-server:50000/UATDB}
    driver-class-name: com.ibm.db2.jcc.DB2Driver
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    database-platform: org.hibernate.dialect.DB2Dialect
    hibernate:
      ddl-auto: validate
    show-sql: false
```

#### Production (`application-prod.yml`)
```yaml
spring:
  datasource:
    url: ${DB_URL:jdbc:db2://prod-db-server:50000/PRODDB}
    driver-class-name: com.ibm.db2.jcc.DB2Driver
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 30000
  jpa:
    database-platform: org.hibernate.dialect.DB2Dialect
    hibernate:
      ddl-auto: validate
    show-sql: false
```

### Backend Environment Variables

Set these for UAT/Production environments:

```bash
# Database Connection
export DB_URL=jdbc:db2://your-db-server:50000/YOURDB
export DB_USERNAME=your_db_user
export DB_PASSWORD=your_db_password

# Application Profile
export SPRING_PROFILES_ACTIVE=prod  # or 'uat' for UAT
export SERVER_PORT=8080

# Logging
export LOG_LEVEL=INFO
```

### Frontend Configuration

#### Development (`.env.development`)
```bash
REACT_APP_API_URL=http://localhost:8080
# NOTE: NO /api suffix - service code adds it automatically
```

#### Production (`.env.production`)
```bash
REACT_APP_API_URL=https://api.yourcompany.com
# NOTE: NO /api suffix - service code adds it automatically
```

**CRITICAL**: The frontend service code appends `/api` to the base URL. Setting `REACT_APP_API_URL=http://localhost:8080/api` will result in incorrect URLs like `http://localhost:8080/api/api/customers`.

## Build Instructions

### Backend Build

#### 1. Navigate to Backend Directory
```bash
cd /Users/lana/Documents/dev/java/smeup/lanmam-method/rpgle-transformer-agents/final-output/backend
```

#### 2. Clean and Compile
```bash
mvn clean compile
```

#### 3. Run Tests
```bash
mvn test
```
Expected: 32 tests passing (repository, service, and controller tests)

#### 4. Package Application
```bash
mvn clean package
```

**Expected Output**: `target/modernization-backend-1.0.0.jar`

#### 5. Verify Build
```bash
ls -lh target/*.jar
# Should show: modernization-backend-1.0.0.jar
```

### Frontend Build

#### 1. Navigate to Frontend Directory
```bash
cd /Users/lana/Documents/dev/java/smeup/lanmam-method/rpgle-transformer-agents/final-output/frontend
```

#### 2. Install Dependencies
```bash
npm install
```

#### 3. Build for Production
```bash
npm run build
```

**Expected Output**: `build/` directory with optimized static files

#### 4. Verify Build
```bash
ls -lh build/
# Should see: index.html, static/ directory with JS and CSS bundles
```

## Deployment Steps

### Option A: Standalone Deployment (Recommended for Production)

This option deploys backend and frontend separately with nginx as reverse proxy.

#### Backend Deployment

1. **Copy JAR to deployment server**:
```bash
scp target/modernization-backend-1.0.0.jar user@server:/opt/modernization/
```

2. **Create environment file** (on deployment server):
```bash
sudo mkdir -p /opt/modernization
sudo cat > /opt/modernization/app.env << EOF
DB_URL=jdbc:db2://prod-db-server:50000/PRODDB
DB_USERNAME=prod_user
DB_PASSWORD=your_secure_password
SPRING_PROFILES_ACTIVE=prod
SERVER_PORT=8080
EOF
sudo chmod 600 /opt/modernization/app.env
```

3. **Create systemd service** `/etc/systemd/system/modernization-backend.service`:
```ini
[Unit]
Description=Modernization Backend - CUST001 Customer Inquiry
After=network.target

[Service]
Type=simple
User=appuser
WorkingDirectory=/opt/modernization
ExecStart=/usr/bin/java -jar /opt/modernization/modernization-backend-1.0.0.jar
EnvironmentFile=/opt/modernization/app.env
Restart=always
RestartSec=10
StandardOutput=journal
StandardError=journal

[Install]
WantedBy=multi-user.target
```

4. **Enable and start service**:
```bash
sudo systemctl daemon-reload
sudo systemctl enable modernization-backend
sudo systemctl start modernization-backend
sudo systemctl status modernization-backend
```

#### Frontend Deployment

1. **Copy build to web server**:
```bash
scp -r build/* user@server:/var/www/modernization/
```

2. **Set permissions**:
```bash
sudo chown -R www-data:www-data /var/www/modernization/
sudo chmod -R 755 /var/www/modernization/
```

3. **Configure nginx** (`/etc/nginx/sites-available/modernization`):
```nginx
server {
    listen 80;
    server_name localhost;

    root /var/www/modernization;
    index index.html;

    # Serve static files with caching
    location /static/ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }

    # Proxy API requests to Spring Boot backend
    location /api/ {
        proxy_pass http://localhost:8080/api/;
        proxy_http_version 1.1;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_set_header Connection "";
        proxy_buffering off;
    }

    # SPA routing: redirect all non-file requests to index.html
    location / {
        try_files $uri $uri/ /index.html;
    }
}
```

4. **Enable nginx site**:
```bash
sudo ln -s /etc/nginx/sites-available/modernization /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl reload nginx
```

### Option B: Local Development Deployment

For local development/testing:

#### 1. Start Backend (Development Mode)
```bash
cd final-output/backend
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```
Backend runs on `http://localhost:8080`
H2 Console available at `http://localhost:8080/h2-console`

#### 2. Start Frontend (Development Mode)
```bash
cd final-output/frontend
npm start
```
Frontend runs on `http://localhost:3000`
Automatically proxies API calls to backend at `http://localhost:8080`

### Option C: Docker Deployment

#### 1. Create Backend Dockerfile (`final-output/backend/Dockerfile`):
```dockerfile
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/modernization-backend-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### 2. Create Frontend Dockerfile (`final-output/frontend/Dockerfile`):
```dockerfile
FROM nginx:alpine
COPY build /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

#### 3. Create docker-compose.yml:
```yaml
version: '3.8'
services:
  backend:
    build: ./backend
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - DB_URL=${DB_URL}
      - DB_USERNAME=${DB_USERNAME}
      - DB_PASSWORD=${DB_PASSWORD}
    restart: always

  frontend:
    build: ./frontend
    ports:
      - "80:80"
    depends_on:
      - backend
    restart: always
```

#### 4. Deploy with Docker Compose:
```bash
# Build and start
docker-compose up -d

# View logs
docker-compose logs -f

# Stop
docker-compose down
```

## Post-Deployment Verification

### 1. Backend Health Check
```bash
curl http://localhost:8080/api/health
# Expected: 200 OK
```

### 2. Test Customer Inquiry API
```bash
# Get customer by number (replace 12345 with actual customer number)
curl -X GET http://localhost:8080/api/customers/12345 \
  -H "Content-Type: application/json"

# Expected: JSON response with customer data or 404 if not found
```

### 3. Frontend Health Check
```bash
curl http://localhost/ | head -20
# Expected: HTML content starting with <!DOCTYPE html>
```

### 4. Test Frontend-Backend Integration
1. Open browser to `http://localhost/`
2. Navigate to "Customer Inquiry"
3. Enter a customer number
4. Click "Search"
5. Verify customer details are displayed
6. Check browser console (F12) for errors

### 5. Smoke Tests
- [ ] Backend starts successfully
- [ ] Database connection established (check logs)
- [ ] Customer Inquiry API endpoint responds
- [ ] Frontend loads without errors
- [ ] Customer search returns valid data
- [ ] Error handling works (try invalid customer number)

## Monitoring

### Backend Logs
```bash
# Systemd logs
sudo journalctl -u modernization-backend -f

# Search for errors
sudo journalctl -u modernization-backend | grep ERROR

# Check database connections
sudo journalctl -u modernization-backend | grep HikariPool
```

### Frontend Logs
```bash
# Nginx access logs
tail -f /var/log/nginx/access.log

# Nginx error logs
tail -f /var/log/nginx/error.log
```

### Application Health
```bash
# Check backend is running
sudo systemctl status modernization-backend

# Check nginx is running
sudo systemctl status nginx

# Check port availability
sudo lsof -i :8080  # Backend
sudo lsof -i :80    # Frontend
```

## Troubleshooting

### Backend Issues

#### Application Won't Start
1. Check Java version: `java -version` (must be 17+)
2. Check port 8080 availability: `lsof -i :8080`
3. Review logs: `sudo journalctl -u modernization-backend -n 50`
4. Verify environment variables: `sudo cat /opt/modernization/app.env`
5. Check database connectivity (see below)

#### Database Connection Errors
**Development (H2)**:
- H2 runs in-memory, no external setup needed
- Access H2 console at `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:devdb`
- Username: `sa`, Password: (blank)

**UAT/Production (DB2)**:
- Verify DB2 is accessible: `telnet db-server 50000`
- Check credentials in `/opt/modernization/app.env`
- Review connection string in application-{profile}.yml
- Check logs: `sudo journalctl -u modernization-backend | grep "Connection"`
- Test DB2 connectivity: Use DB2 command line tools

#### 404 on API Calls
- Verify controller uses `@RequestMapping("/api/customers")`
- Check application logs for startup errors
- Test directly: `curl http://localhost:8080/api/customers/123`
- Verify no context-path is set in application.yml

### Frontend Issues

#### Frontend Won't Load
1. Check nginx: `sudo systemctl status nginx`
2. Verify files exist: `ls -l /var/www/modernization/`
3. Check nginx config: `sudo nginx -t`
4. Review nginx error log: `tail -f /var/log/nginx/error.log`

#### API Calls from Frontend Failing
1. Check CORS configuration in WebConfig.java
2. Verify `.env.production` has correct `REACT_APP_API_URL` (NO /api suffix)
3. Check backend is running: `curl http://localhost:8080/api/customers/123`
4. Review browser console (F12) for CORS or network errors
5. Check nginx proxy configuration for `/api/` location

#### CORS Errors
- Development: CORS origins in `application-dev.yml` include `http://localhost:3000,http://localhost:5173`
- Production: Update CORS origins in `application-prod.yml` to match your frontend domain
- CORS is configured in WebConfig.java for `/api/**` endpoints

## Rollback Procedures

### Backend Rollback
```bash
# Stop service
sudo systemctl stop modernization-backend

# Restore previous JAR
sudo cp /opt/modernization/backups/modernization-backend-0.9.0.jar \
        /opt/modernization/modernization-backend-1.0.0.jar

# Start service
sudo systemctl start modernization-backend
```

### Frontend Rollback
```bash
# Restore previous build
sudo mv /var/www/modernization /var/www/modernization-new
sudo cp -r /var/www/modernization-backup /var/www/modernization
sudo chown -R www-data:www-data /var/www/modernization

# Reload nginx
sudo systemctl reload nginx
```

## API Endpoint Reference

### Customer Inquiry Endpoints

| Method | Endpoint | Description | Request | Response |
|--------|----------|-------------|---------|----------|
| GET | `/api/customers/{customerNumber}` | Get customer by number | - | Customer JSON or 404 |

#### Example Request
```bash
GET /api/customers/12345
```

#### Example Response (Success)
```json
{
  "customerNumber": 12345,
  "name": "ACME Corporation",
  "address": "123 Main Street",
  "city": "New York",
  "state": "NY",
  "zipCode": "10001",
  "phoneNumber": "212-555-1234"
}
```

#### Example Response (Not Found)
```json
{
  "timestamp": "2025-12-28T10:30:00.000+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Customer not found with number: 99999",
  "path": "/api/customers/99999"
}
```

## Security Considerations

### Backend Security
- [x] Database credentials in environment variables (not in code)
- [ ] HTTPS/TLS configured (production only)
- [x] Input validation on customer number (@PathVariable with Integer type)
- [x] JPA parameterized queries (prevents SQL injection)
- [x] CORS configured in WebConfig.java
- [ ] Rate limiting (to be implemented)
- [ ] Authentication/Authorization (CUST001 is read-only, low risk)

### Frontend Security
- [ ] HTTPS/TLS enforced (production deployment)
- [x] React XSS protection (default)
- [x] CORS properly configured
- [x] No sensitive data in localStorage
- [x] No API keys or secrets in code

### Production Security Checklist
- [ ] Configure HTTPS with valid SSL certificate
- [ ] Update CORS origins to production domains only
- [ ] Set secure database passwords (min 16 characters)
- [ ] Restrict database user to SELECT permissions only
- [ ] Enable nginx security headers (X-Frame-Options, CSP, etc.)
- [ ] Configure firewall rules (allow only 80, 443, 8080 from nginx)
- [ ] Regular security updates: `sudo apt update && sudo apt upgrade`

## Support Information
- **Technical Contact**: Development Team
- **Documentation**: `/final-output/docs/CUST001/`
- **Integration Report**: `integration-report.md`
- **Analysis**: `analysis.md`
- **Testing Notes**: `testing-notes.md`

## Appendix

### A. Quick Reference Commands

**Development**:
```bash
# Start backend (dev mode)
cd backend && mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Start frontend (dev mode)
cd frontend && npm start
```

**Building**:
```bash
# Build backend
cd backend && mvn clean package

# Build frontend
cd frontend && npm run build
```

**Production**:
```bash
# Start/stop/restart backend
sudo systemctl start modernization-backend
sudo systemctl stop modernization-backend
sudo systemctl restart modernization-backend

# Reload nginx
sudo systemctl reload nginx

# View logs
sudo journalctl -u modernization-backend -f
tail -f /var/log/nginx/access.log
```

**Testing**:
```bash
# Backend health
curl http://localhost:8080/api/health

# Customer lookup
curl http://localhost:8080/api/customers/12345

# Frontend
curl http://localhost/
```

### B. Database Schema

See `database-notes.md` for complete schema documentation.

**CUSTMAST Table**:
- CUSTNBR (INTEGER) - Primary Key
- CUSTNAME (VARCHAR 50)
- CUSTADDR (VARCHAR 50)
- CUSTCITY (VARCHAR 30)
- CUSTSTAT (VARCHAR 2)
- CUSTZIP (VARCHAR 10)
- CUSTPHON (VARCHAR 15)

### C. Known Limitations

1. **Read-Only Operation**: CUST001 implements only customer inquiry (read). Create/Update/Delete operations not included.
2. **Single Customer Lookup**: Supports lookup by customer number only. List/search functionality not implemented in this phase.
3. **No Pagination**: If extending to list view, pagination should be added.

### D. File Locations Reference (Production)

| Item | Location |
|------|----------|
| Backend JAR | `/opt/modernization/modernization-backend-1.0.0.jar` |
| Backend Config | `/opt/modernization/app.env` |
| Backend Service | `/etc/systemd/system/modernization-backend.service` |
| Backend Logs | `sudo journalctl -u modernization-backend` |
| Frontend Files | `/var/www/modernization/` |
| Nginx Config | `/etc/nginx/sites-available/modernization` |
| Nginx Logs | `/var/log/nginx/access.log`, `/var/log/nginx/error.log` |

---

**Document Version**: 1.0
**Last Updated**: 2025-12-28
**Program**: CUST001 - Customer Inquiry
**Created By**: Integration Agent
