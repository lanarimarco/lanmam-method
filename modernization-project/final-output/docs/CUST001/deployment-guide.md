# Deployment Guide - CUST001 Customer Inquiry

## Overview

This document provides step-by-step instructions for deploying the CUST001 Customer Inquiry Spring Boot application to various environments (development, staging, production).

**Program**: CUST001 - Customer Inquiry
**Version**: 1.0.0-SNAPSHOT
**Technology**: Spring Boot 3.2.0, Java 17
**Original**: RPGLE program CUST001

---

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Database Setup](#database-setup)
3. [Configuration](#configuration)
4. [Build Process](#build-process)
5. [Deployment Steps](#deployment-steps)
6. [Smoke Testing](#smoke-testing)
7. [Rollback Procedures](#rollback-procedures)
8. [Troubleshooting](#troubleshooting)

---

## Prerequisites

### Software Requirements

| Component | Version | Required |
|-----------|---------|----------|
| Java JDK | 17+ | ✅ Yes |
| Maven | 3.8+ | ✅ Yes |
| DB2 Database | 11.5+ | ✅ Yes |
| Application Server | - | ❌ No (embedded Tomcat) |

### Database Access

- DB2 database server accessible from deployment environment
- Database credentials with read access to `CUSTMAST` table
- Network connectivity on DB2 port (default: 50000)

### Environment Variables

The following environment variables must be set:

```bash
# Database credentials
export DB_USERNAME=your_db_username
export DB_PASSWORD=your_db_password

# Optional: Override database connection
export DB_HOST=your-db-server
export DB_PORT=50000
export DB_NAME=DATABASE
```

---

## Database Setup

### 1. Verify CUSTMAST Table Exists

The application requires the `CUSTMAST` table with the following structure:

```sql
-- Verify table exists
SELECT * FROM CUSTMAST FETCH FIRST 1 ROWS ONLY;
```

### 2. Required Table Structure

```sql
CREATE TABLE CUSTMAST (
    CUSTNO    DECIMAL(5,0) NOT NULL PRIMARY KEY,
    CUSTNAME  CHAR(30),
    ADDR1     CHAR(30),
    CITY      CHAR(20),
    STATE     CHAR(2),
    ZIP       DECIMAL(5,0),
    PHONE     CHAR(12),
    BALANCE   DECIMAL(9,2),
    CREDLMT   DECIMAL(9,2),
    LASTORD   DATE
);
```

### 3. Create Database User (if needed)

```sql
-- Create read-only user for the application
CREATE USER cust_inquiry_user PASSWORD 'secure_password';
GRANT SELECT ON TABLE CUSTMAST TO USER cust_inquiry_user;
```

### 4. Test Database Connection

```bash
# Test DB2 connection
db2 connect to DATABASE user $DB_USERNAME using $DB_PASSWORD

# Verify table access
db2 "SELECT COUNT(*) FROM CUSTMAST"

# Disconnect
db2 disconnect DATABASE
```

---

## Configuration

### Application Properties

The application uses Spring Boot profiles for environment-specific configuration.

#### Development Configuration

File: `src/main/resources/application.yml`

```yaml
spring:
  datasource:
    url: jdbc:db2://localhost:50000/DATABASE
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}

logging:
  level:
    com.smeup.erp: INFO

app:
  cors:
    allowed-origins: http://localhost:3000,http://localhost:4200,http://localhost:5173
```

#### Production Configuration

File: `src/main/resources/application-prod.yml`

```yaml
spring:
  datasource:
    url: jdbc:db2://${DB_HOST}:${DB_PORT}/${DB_NAME}

logging:
  level:
    com.smeup.erp: WARN

app:
  cors:
    allowed-origins: https://erp.smeup.com,https://customer-inquiry.smeup.com
```

### Activate Production Profile

```bash
# Set Spring profile
export SPRING_PROFILES_ACTIVE=prod

# Or pass as JVM argument
java -Dspring.profiles.active=prod -jar modernization-1.0.0-SNAPSHOT.jar
```

---

## Build Process

### 1. Clean Previous Builds

```bash
cd /path/to/final-output
mvn clean
```

### 2. Run Compilation

```bash
mvn compile
```

Expected output:
```
[INFO] BUILD SUCCESS
[INFO] Compiling 18 source files
```

### 3. Run Tests (Optional)

**Note**: Currently tests need to be updated for refactored exception handling. Skip for now:

```bash
mvn test -DskipTests
```

### 4. Package Application

```bash
mvn package -DskipTests
```

This creates: `target/modernization-1.0.0-SNAPSHOT.jar`

### 5. Verify JAR Contents

```bash
jar tf target/modernization-1.0.0-SNAPSHOT.jar | grep -E "(Customer|Application)"
```

---

## Deployment Steps

### Development Environment

#### 1. Set Environment Variables

```bash
export DB_USERNAME=dev_user
export DB_PASSWORD=dev_password
export SPRING_PROFILES_ACTIVE=default
```

#### 2. Run Application

```bash
cd /path/to/final-output
mvn spring-boot:run
```

Or run the JAR directly:
```bash
java -jar target/modernization-1.0.0-SNAPSHOT.jar
```

#### 3. Verify Startup

Application should start on port 8080:
```
Started CustomerInquiryApplication in X.XXX seconds
```

#### 4. Access Endpoints

- Base URL: `http://localhost:8080`
- API Endpoints: `http://localhost:8080/api/v1/customers/*`

---

### Staging Environment

#### 1. Prepare Deployment Package

```bash
# Build the application
mvn clean package -DskipTests

# Create deployment directory
mkdir -p /opt/customer-inquiry
cd /opt/customer-inquiry

# Copy JAR
cp /path/to/final-output/target/modernization-1.0.0-SNAPSHOT.jar ./customer-inquiry.jar

# Copy configuration
cp /path/to/final-output/src/main/resources/application-prod.yml ./application.yml
```

#### 2. Create Systemd Service (Linux)

Create `/etc/systemd/system/customer-inquiry.service`:

```ini
[Unit]
Description=Customer Inquiry Service (CUST001)
After=network.target

[Service]
Type=simple
User=custinq
WorkingDirectory=/opt/customer-inquiry
Environment="DB_USERNAME=staging_user"
Environment="DB_PASSWORD=staging_password"
Environment="SPRING_PROFILES_ACTIVE=prod"
ExecStart=/usr/bin/java -jar /opt/customer-inquiry/customer-inquiry.jar
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
```

#### 3. Start Service

```bash
# Reload systemd
sudo systemctl daemon-reload

# Enable service
sudo systemctl enable customer-inquiry

# Start service
sudo systemctl start customer-inquiry

# Check status
sudo systemctl status customer-inquiry
```

#### 4. View Logs

```bash
# Follow logs
sudo journalctl -u customer-inquiry -f

# View last 100 lines
sudo journalctl -u customer-inquiry -n 100
```

---

### Production Environment

#### Prerequisites

- [ ] Database backup completed
- [ ] Staging deployment successful
- [ ] Smoke tests passing in staging
- [ ] Change request approved
- [ ] Rollback plan documented

#### Deployment Steps

Same as staging, but with production database credentials:

```bash
Environment="DB_USERNAME=prod_user"
Environment="DB_PASSWORD=prod_password"
Environment="DB_HOST=production-db-server"
```

#### Zero-Downtime Deployment (Blue-Green)

1. Deploy to "green" environment
2. Run smoke tests on green
3. Switch load balancer to green
4. Keep blue running for rollback
5. After verification, decommission blue

---

## Smoke Testing

### 1. Health Check

```bash
curl http://localhost:8080/actuator/health
```

Expected response:
```json
{
  "status": "UP"
}
```

### 2. API Endpoint Test

```bash
curl -X POST http://localhost:8080/api/v1/customers/inquire \
  -H "Content-Type: application/json" \
  -d '{"customerNumber": 12345}'
```

Expected response (successful):
```json
{
  "customerNumber": 12345,
  "customerName": "ACME Corporation",
  "address1": "123 Main Street",
  ...
  "success": true
}
```

### 3. Error Handling Test

```bash
curl -X POST http://localhost:8080/api/v1/customers/inquire \
  -H "Content-Type: application/json" \
  -d '{"customerNumber": 99999}'
```

Expected response (not found):
```json
{
  "timestamp": "2025-12-18T...",
  "status": 404,
  "error": "Not Found",
  "message": "Customer not found"
}
```

### 4. Database Connectivity Test

Check application logs for successful database connection:

```bash
grep "HikariPool" /var/log/customer-inquiry/application.log
```

Expected:
```
HikariPool-1 - Starting...
HikariPool-1 - Start completed.
```

---

## Rollback Procedures

### Immediate Rollback (Service Restart)

If application fails to start or crashes:

```bash
# Stop the service
sudo systemctl stop customer-inquiry

# Restore previous JAR
cp /opt/customer-inquiry/customer-inquiry.jar.backup \
   /opt/customer-inquiry/customer-inquiry.jar

# Restart service
sudo systemctl start customer-inquiry

# Verify
sudo systemctl status customer-inquiry
```

### Database Rollback

If database changes were made (not applicable for CUST001 - read-only):

```bash
# Restore database backup
db2 restore database DATABASE from /backup/path
```

### Configuration Rollback

```bash
# Restore previous configuration
cp /opt/customer-inquiry/application.yml.backup \
   /opt/customer-inquiry/application.yml

# Restart service
sudo systemctl restart customer-inquiry
```

---

## Troubleshooting

### Application Won't Start

**Symptom**: Service fails to start or exits immediately

**Check**:
1. Database credentials: `echo $DB_USERNAME`
2. Database connectivity: `telnet db-server 50000`
3. Java version: `java -version` (must be 17+)
4. Port availability: `netstat -tulpn | grep 8080`
5. Logs: `sudo journalctl -u customer-inquiry -n 50`

**Common Issues**:
- Database credentials incorrect → Update environment variables
- Port 8080 in use → Change `server.port` in application.yml
- Java version mismatch → Install Java 17

### Database Connection Errors

**Symptom**: Logs show "Unable to obtain JDBC Connection"

**Solutions**:
1. Verify DB2 service running: `db2 list active databases`
2. Check network connectivity: `ping db-server`
3. Verify credentials: `db2 connect to DATABASE user $DB_USERNAME`
4. Check firewall: `telnet db-server 50000`

### API Returns 404

**Symptom**: All API requests return 404

**Check**:
1. Application started successfully
2. Correct URL: `/api/v1/customers/inquire` (not `/api/customers/inquire`)
3. CORS configuration if calling from browser

### High Memory Usage

**Symptom**: Application consuming too much memory

**Solutions**:
1. Adjust JVM heap: `java -Xmx512m -jar customer-inquiry.jar`
2. Check for database connection leaks
3. Monitor with: `jstat -gc <pid> 1000`

### Slow Response Times

**Symptom**: API requests taking >1 second

**Check**:
1. Database query performance
2. Network latency to DB2 server
3. Application logs for slow queries
4. Consider connection pooling tuning

---

## Monitoring

### Log Files

Default log location: `/var/log/customer-inquiry/`

Configure in `application.yml`:
```yaml
logging:
  file:
    name: /var/log/customer-inquiry/application.log
  pattern:
    file: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
```

### Metrics (Spring Boot Actuator)

Enable actuator endpoints:
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
```

Access metrics:
- Health: `http://localhost:8080/actuator/health`
- Metrics: `http://localhost:8080/actuator/metrics`

### Application Performance Monitoring (APM)

Consider integrating:
- **New Relic**: Java agent for performance monitoring
- **Prometheus + Grafana**: Metrics collection and visualization
- **ELK Stack**: Centralized log management

---

## Security Checklist

Before production deployment:

- [ ] Database credentials stored securely (not in code)
- [ ] HTTPS/TLS enabled (not HTTP)
- [ ] CORS origins restricted to known domains
- [ ] Input validation enabled (`@Valid` annotations)
- [ ] SQL injection prevented (using JPA)
- [ ] Error messages don't expose sensitive data
- [ ] Actuator endpoints secured or disabled
- [ ] Application running as non-root user
- [ ] Firewall rules configured (only ports 8080/443 open)
- [ ] Security audit completed

---

## Performance Tuning

### JVM Options

Recommended for production:

```bash
java -Xms512m -Xmx1024m \
     -XX:+UseG1GC \
     -XX:MaxGCPauseMillis=200 \
     -Dspring.profiles.active=prod \
     -jar customer-inquiry.jar
```

### Database Connection Pool

Tune HikariCP in `application.yml`:

```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 10
      minimum-idle: 2
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000
```

---

## Support Contacts

| Issue Type | Contact | Email |
|------------|---------|-------|
| Application Issues | Development Team | dev-team@smeup.com |
| Database Issues | DBA Team | dba@smeup.com |
| Deployment Issues | DevOps Team | devops@smeup.com |
| Production Outages | On-Call Engineer | oncall@smeup.com |

---

**Document Version**: 1.0
**Last Updated**: 2025-12-19
**Next Review**: Before first production deployment
