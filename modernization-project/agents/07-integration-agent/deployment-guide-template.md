# Deployment Guide: {PROGRAM}

## Overview
**Program Name**: {PROGRAM}
**Original RPGLE**: {PROGRAM}.rpgle
**Converted Services**: [List service names]
**Deployment Type**: Embedded / Standalone / Microservice
**Database**: DB2 / PostgreSQL / Other

## Prerequisites

### Software Requirements
- Java 17 or higher
- Maven 3.8+
- Database: [DB2/PostgreSQL/etc.] version X.X
- Application Server: [Spring Boot embedded / Tomcat / etc.]

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

## Configuration

### Database Connection
Update `/final-output/src/main/resources/application.yml`:

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

### Environment Variables
Set these environment variables:

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

### Application Properties
Program-specific configuration:
- [List any program-specific settings]
- [API endpoints]
- [Business rule configurations]

## Build Instructions

### 1. Clone/Copy Source Code
```bash
cd /final-output
```

### 2. Build the Application
```bash
# Clean and compile
mvn clean compile

# Run tests
mvn test

# Package
mvn clean package
```

**Expected Output**: `target/{artifact-name}-{version}.jar` or `.war`

### 3. Verify Build
```bash
# Check the JAR/WAR was created
ls -lh target/*.jar

# Should see something like:
# modernization-app-1.0.0.jar
```

## Deployment Steps

### Option A: Standalone JAR (Recommended)

1. **Copy JAR to deployment server**:
   ```bash
   scp target/modernization-app-1.0.0.jar user@server:/opt/app/
   ```

2. **Set environment variables** (on deployment server):
   ```bash
   export DB_USERNAME=prod_user
   export DB_PASSWORD=secure_password
   export SPRING_PROFILES_ACTIVE=production
   ```

3. **Run the application**:
   ```bash
   java -jar /opt/app/modernization-app-1.0.0.jar
   ```

4. **Run as service** (optional):
   Create systemd service file `/etc/systemd/system/modernization-app.service`:
   ```ini
   [Unit]
   Description=Modernization Spring Boot App
   After=network.target

   [Service]
   Type=simple
   User=appuser
   ExecStart=/usr/bin/java -jar /opt/app/modernization-app-1.0.0.jar
   EnvironmentFile=/opt/app/app.env
   Restart=always

   [Install]
   WantedBy=multi-user.target
   ```

   Enable and start:
   ```bash
   sudo systemctl enable modernization-app
   sudo systemctl start modernization-app
   ```

### Option B: Application Server Deployment

1. **Deploy WAR to Tomcat**:
   ```bash
   cp target/modernization-app-1.0.0.war /opt/tomcat/webapps/
   ```

2. **Configure JNDI** (if using):
   Add to Tomcat's `context.xml`

3. **Restart Tomcat**:
   ```bash
   sudo systemctl restart tomcat
   ```

## UI Deployment

### Option A: Embedded Static Resources
- UI is bundled in the JAR/WAR
- Accessible at: `http://server:8080/`
- No additional deployment needed

### Option B: Separate React Application
1. **Build React app**:
   ```bash
   cd ui-app
   npm run build
   ```

2. **Deploy to web server** (nginx/Apache):
   ```bash
   cp -r build/* /var/www/html/
   ```

3. **Configure API proxy** in nginx:
   ```nginx
   location /api/ {
       proxy_pass http://localhost:8080/api/;
   }
   ```

## Post-Deployment Verification

### 1. Health Check
```bash
# Check application is running
curl http://localhost:8080/actuator/health

# Expected response: {"status":"UP"}
```

### 2. Database Connection
```bash
# Check logs for successful database connection
tail -f /var/log/modernization-app.log | grep "HikariPool"

# Should see: "HikariPool-1 - Start completed"
```

### 3. Smoke Tests

#### Test {PROGRAM} Endpoints
```bash
# Example: Test customer inquiry
curl -X GET http://localhost:8080/api/customers/12345

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

### 4. Functional Tests
Run through these business scenarios:
- [ ] [Scenario 1: e.g., "Query customer by number"]
- [ ] [Scenario 2: e.g., "Display customer details"]
- [ ] [Scenario 3: e.g., "Update customer information"]
- [ ] [Add program-specific scenarios]

## Monitoring

### Application Logs
```bash
# View logs
tail -f /var/log/modernization-app.log

# Search for errors
grep ERROR /var/log/modernization-app.log
```

### Performance Monitoring
- Monitor heap usage
- Monitor database connection pool
- Monitor response times

### Health Endpoints (Spring Boot Actuator)
- Health: `/actuator/health`
- Metrics: `/actuator/metrics`
- Info: `/actuator/info`

## Troubleshooting

### Application Won't Start
1. **Check Java version**: `java -version` (must be 17+)
2. **Check database connectivity**:
   ```bash
   psql -h [HOST] -U [USER] -d [DATABASE]
   ```
3. **Review logs**: Check for stack traces
4. **Verify configuration**: Check application.yml values

### Database Connection Errors
- Verify database is running
- Check connection string
- Verify credentials
- Check firewall rules
- Review database logs

### 404 Errors on API Calls
- Verify context-path configuration
- Check controller mapping annotations
- Review deployment logs for startup errors

### Performance Issues
- Check database query performance
- Review application logs for slow queries
- Monitor memory usage
- Check connection pool settings

## Rollback Procedures

### 1. Stop Application
```bash
# If running as systemd service
sudo systemctl stop modernization-app

# If running standalone
kill [PID]
```

### 2. Restore Previous Version
```bash
# Backup current version
mv /opt/app/modernization-app-1.0.0.jar /opt/app/backups/

# Restore previous version
cp /opt/app/backups/modernization-app-0.9.0.jar /opt/app/modernization-app.jar
```

### 3. Restart Application
```bash
sudo systemctl start modernization-app
```

### 4. Verify Rollback
Run smoke tests to confirm previous version is working.

## Environment-Specific Settings

### Development
```yaml
spring:
  jpa:
    show-sql: true
logging:
  level:
    com.company.modernization: DEBUG
```

### UAT/Staging
```yaml
spring:
  jpa:
    show-sql: false
logging:
  level:
    com.company.modernization: INFO
```

### Production
```yaml
spring:
  jpa:
    show-sql: false
    hibernate:
      ddl-auto: validate
logging:
  level:
    com.company.modernization: WARN
```

## Security Considerations
- [ ] Database credentials in environment variables (not in code)
- [ ] HTTPS/TLS configured (if applicable)
- [ ] Input validation enabled
- [ ] SQL injection prevention (JPA handles this)
- [ ] Sensitive data not logged
- [ ] Security headers configured

## Support Information
- **Technical Contact**: [Name/Email]
- **Documentation**: `/final-output/docs/{PROGRAM}/`
- **Source Code**: [Repository URL]
- **Issue Tracking**: [URL]

## Appendix

### A. API Endpoint Reference
List all REST endpoints for this program:

| Method | Endpoint | Description | Request | Response |
|--------|----------|-------------|---------|----------|
| GET | /api/resource/{id} | Get by ID | - | Resource JSON |
| POST | /api/resource | Create | Resource JSON | Created resource |
| PUT | /api/resource/{id} | Update | Resource JSON | Updated resource |
| DELETE | /api/resource/{id} | Delete | - | 204 No Content |

### B. Database Schema
Reference to database schema documentation from Phase 2.

### C. Business Rules
Key business rules implemented in this program:
- [Rule 1]
- [Rule 2]
- [Reference: conversion-notes.md for complete list]

### D. Known Limitations
Document any known limitations or differences from RPGLE original:
- [Limitation 1]
- [Limitation 2]

---

**Document Version**: 1.0
**Last Updated**: {DATE}
**Updated By**: Integration Agent
