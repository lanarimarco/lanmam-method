# CUST001 - Customer Inquiry Application

Modernized version of the RPG LE CUST001 program using Java Spring Boot backend and React frontend.

## Overview

This application modernizes the legacy CUST001 customer inquiry program by transforming it into a contemporary web application with:

- **Backend**: Java Spring Boot REST API with JPA/Hibernate
- **Frontend**: React single-page application with modern UI
- **Database**: H2 in-memory database (development) / PostgreSQL (production)

## Original RPG Program

The original CUST001 RPG program provided:
- Customer number entry screen (PROMPT)
- Customer detail display screen (DETAIL)
- Database lookup in CUSTMAST file
- Error handling for invalid/non-existent customers
- Function key support (F3=Exit, F12=Return)

## Modernized Architecture

### Backend Structure

```
backend/
├── src/main/java/com/smeup/customerinquiry/
│   ├── CustomerInquiryApplication.java       # Main Spring Boot application
│   ├── controller/
│   │   └── CustomerController.java           # REST API endpoints
│   ├── service/
│   │   └── CustomerService.java              # Business logic layer
│   ├── repository/
│   │   └── CustomerRepository.java           # Data access layer
│   ├── model/
│   │   └── Customer.java                     # JPA entity
│   ├── dto/
│   │   ├── CustomerDTO.java                  # Data transfer object
│   │   └── ErrorResponse.java                # Error response structure
│   └── exception/
│       ├── CustomerNotFoundException.java    # Custom exception
│       └── GlobalExceptionHandler.java       # Centralized error handling
├── src/main/resources/
│   ├── application.yml                       # Development configuration
│   ├── application-prod.yml                  # Production configuration
│   └── data.sql                              # Sample test data
└── pom.xml                                   # Maven dependencies
```

### Frontend Structure

```
frontend/
├── public/
│   └── index.html                            # HTML template
├── src/
│   ├── components/
│   │   ├── CustomerSearch.js                 # Search screen component
│   │   ├── CustomerSearch.css                # Search screen styles
│   │   ├── CustomerDetail.js                 # Detail screen component
│   │   └── CustomerDetail.css                # Detail screen styles
│   ├── services/
│   │   └── customerService.js                # API communication layer
│   ├── App.js                                # Main application component
│   ├── App.css                               # Application styles
│   ├── index.js                              # React entry point
│   └── index.css                             # Global styles
└── package.json                              # NPM dependencies
```

## Features

### Backend Features

- **RESTful API Design**: Clean REST endpoints following industry standards
- **Layered Architecture**: Controller → Service → Repository pattern
- **Exception Handling**: Centralized error handling with meaningful messages
- **Data Validation**: Bean validation with Jakarta Validation API
- **Database Abstraction**: JPA/Hibernate for database independence
- **Transaction Management**: Declarative transaction handling
- **CORS Support**: Configured for frontend communication
- **Logging**: SLF4J logging throughout application

### Frontend Features

- **Modern UI**: Terminal-inspired green-screen aesthetic
- **Responsive Design**: Mobile-friendly layouts
- **Component-Based**: Reusable React components
- **State Management**: React hooks for state handling
- **Error Handling**: User-friendly error messages
- **Keyboard Support**: F3/ESC to exit, F12/ESC to return
- **Loading States**: Visual feedback during API calls
- **Form Validation**: Client-side input validation

## Prerequisites

### Backend Requirements

- Java 17 or higher
- Maven 3.6 or higher

### Frontend Requirements

- Node.js 16 or higher
- npm 8 or higher

## Installation

### 1. Backend Setup

Navigate to the backend directory:

```bash
cd backend
```

Build the project:

```bash
mvn clean install
```

Run the application:

```bash
mvn spring-boot:run
```

The backend will start on [http://localhost:8080](http://localhost:8080)

#### H2 Database Console

Access the H2 console at: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

- **JDBC URL**: `jdbc:h2:mem:customerdb`
- **Username**: `sa`
- **Password**: (leave blank)

### 2. Frontend Setup

Navigate to the frontend directory:

```bash
cd frontend
```

Install dependencies:

```bash
npm install
```

Start the development server:

```bash
npm start
```

The frontend will start on [http://localhost:3000](http://localhost:3000)

## Usage

### Customer Search

1. Enter a customer number (1-99999)
2. Click "Search" or press Enter
3. If found, customer details will be displayed
4. If not found, an error message will appear

### Customer Detail

- View all customer information including:
  - Customer number and name
  - Address details (street, city, state, zip)
  - Contact information (phone)
  - Financial information (balance, credit limit)
  - Last order date
- Click "Return to Search" or press F12/ESC to go back

### Sample Customer Data

The application comes pre-loaded with test data:

| Customer # | Name | City | Balance |
|------------|------|------|---------|
| 10001 | Acme Corporation | New York | $15,000.50 |
| 10002 | TechStart Inc | San Francisco | $8,500.75 |
| 10003 | Global Traders LLC | Chicago | $22,000.00 |
| 10004 | Premier Solutions | Boston | $5,000.25 |
| 10005 | Midwest Manufacturing | Detroit | $31,500.00 |

## API Endpoints

### Customer Endpoints

```
GET    /api/customers/{customerNumber}   - Get customer by number
GET    /api/customers                    - Get all customers
POST   /api/customers                    - Create new customer
PUT    /api/customers/{customerNumber}   - Update customer
DELETE /api/customers/{customerNumber}   - Delete customer
```

### Example API Requests

#### Get Customer by Number

```bash
curl http://localhost:8080/api/customers/10001
```

Response:
```json
{
  "customerNumber": 10001,
  "customerName": "Acme Corporation",
  "addressLine1": "123 Main Street",
  "city": "New York",
  "state": "NY",
  "zipCode": 10001,
  "phoneNumber": "212-555-0100",
  "accountBalance": 15000.50,
  "creditLimit": 50000.00,
  "lastOrderDate": "2025-12-15"
}
```

#### Create Customer

```bash
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "customerNumber": 10006,
    "customerName": "New Customer Inc",
    "addressLine1": "456 Oak Avenue",
    "city": "Seattle",
    "state": "WA",
    "zipCode": 98101,
    "phoneNumber": "206-555-0600",
    "accountBalance": 0.00,
    "creditLimit": 10000.00
  }'
```

## Configuration

### Backend Configuration

#### Development (application.yml)

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:customerdb
  jpa:
    hibernate:
      ddl-auto: create-drop
server:
  port: 8080
```

#### Production (application-prod.yml)

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/customerdb
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: validate
```

To run with production profile:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### Frontend Configuration

API endpoint is configured in [src/services/customerService.js](frontend/src/services/customerService.js):

```javascript
const API_BASE_URL = 'http://localhost:8080/api/customers';
```

## Testing

### Backend Testing

Run unit tests:

```bash
cd backend
mvn test
```

### Frontend Testing

Run tests:

```bash
cd frontend
npm test
```

## Production Build

### Backend

Create executable JAR:

```bash
cd backend
mvn clean package
```

Run the JAR:

```bash
java -jar target/customer-inquiry-1.0.0.jar
```

### Frontend

Create production build:

```bash
cd frontend
npm run build
```

The optimized production build will be in the `build/` directory.

Serve the production build:

```bash
npx serve -s build
```

## Technology Stack

### Backend

- **Spring Boot 3.2.1**: Application framework
- **Spring Data JPA**: Data access abstraction
- **Hibernate**: ORM implementation
- **H2 Database**: In-memory database for development
- **PostgreSQL**: Production database
- **Lombok**: Reduce boilerplate code
- **Jakarta Validation**: Bean validation
- **SLF4J**: Logging framework
- **Maven**: Build tool

### Frontend

- **React 18.2.0**: UI framework
- **Axios**: HTTP client
- **React Scripts**: Build tooling
- **CSS3**: Styling

## Best Practices Implemented

### Backend

1. **Layered Architecture**: Separation of concerns with Controller, Service, Repository layers
2. **DTO Pattern**: Data Transfer Objects for API responses
3. **Exception Handling**: Centralized error handling with custom exceptions
4. **Validation**: Input validation using Jakarta Bean Validation
5. **Logging**: Comprehensive logging for debugging and monitoring
6. **Transaction Management**: Proper transaction boundaries
7. **RESTful Design**: Following REST principles for API design

### Frontend

1. **Component-Based Architecture**: Reusable and maintainable components
2. **Separation of Concerns**: Components, services, and styling separated
3. **Error Handling**: Graceful error handling with user feedback
4. **Loading States**: User feedback during async operations
5. **Responsive Design**: Mobile-friendly layouts
6. **Accessibility**: Keyboard navigation support
7. **Clean Code**: Well-structured and readable code

## Migration Guide from RPG

### Data Type Mappings

| RPG Type | Java Type | Notes |
|----------|-----------|-------|
| 5P 0 | Integer | Customer number, Zip code |
| 30A | String | Customer name, Address |
| 20A | String | City |
| 2A | String | State |
| 12A | String | Phone number |
| 9P 2 | BigDecimal | Balance, Credit limit |
| 8P 0 | LocalDate | Last order date (YYYYMMDD) |

### Screen Format Mappings

| RPG Format | React Component | Purpose |
|------------|-----------------|---------|
| PROMPT | CustomerSearch | Customer number entry |
| DETAIL | CustomerDetail | Customer information display |

### Function Keys

| RPG Key | Web Equivalent | Action |
|---------|----------------|--------|
| F3 | F3 or ESC (on search) | Exit application |
| F12 | F12 or ESC (on detail) | Return to search |

## Troubleshooting

### Backend Issues

**Port 8080 already in use**
```bash
# Change port in application.yml
server:
  port: 8081
```

**Database connection errors**
- Ensure PostgreSQL is running (production mode)
- Verify database credentials in environment variables

### Frontend Issues

**Cannot connect to backend**
- Verify backend is running on port 8080
- Check CORS configuration in CustomerController
- Update API_BASE_URL if backend port changed

**npm install fails**
- Clear npm cache: `npm cache clean --force`
- Delete node_modules and package-lock.json
- Run `npm install` again

## License

This project is created for educational and demonstration purposes.

## Support

For issues and questions, please refer to the documentation or contact the development team.

---

**Generated as part of the RPG LE to Modern Stack Transformation Project**
