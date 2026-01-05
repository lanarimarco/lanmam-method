# RPGLE Transformer Baseline

A baseline implementation demonstrating the transformation of a legacy RPGLE application into a modern Java/React architecture. This project serves as a reference point for evaluating advanced modernization techniques and patterns.

## Project Overview

This repository contains the complete transformation of the **CUST001 Customer Inquiry** program from an IBM RPG LE green-screen application into a contemporary full-stack web application.

### Key Characteristics

- **Original Technology Stack**: RPGLE (green-screen 5250 UI), DDS display/physical files, DB2/IFS
- **Modern Technology Stack**: Java Spring Boot + React + PostgreSQL/H2
- **Purpose**: Baseline for modernization technique comparison and evaluation
- **Artifact Usage**: Used to validate and benchmark advanced modernization approaches

## Project Structure

```
rpgle-transformer-baseline/
├── source-rpgle/                   # Original RPGLE application artifacts
│   ├── programs/                   # RPGLE program source (CUST001.rpgle)
│   ├── copybooks/                  # RPGLE copybooks and includes
│   └── dds/                        # DDS definitions (display, physical, logical files)
│
├── final-output/                   # Transformed modern application
│   ├── backend/                    # Java Spring Boot backend service
│   │   ├── pom.xml                # Maven configuration
│   │   └── src/main/
│   │       ├── java/com/smeup/customerinquiry/  # Java application code
│   │       └── resources/          # Application properties and sample data
│   │
│   ├── frontend/                   # React frontend application
│   │   ├── package.json            # NPM dependencies
│   │   ├── public/                 # Static assets
│   │   └── src/                    # React components and styles
│   │
│   ├── README.md                   # Detailed application documentation
│   └── TRANSFORMATION-SUMMARY.md   # Technical transformation details
```

## Transformation Overview

### What Changed

| Aspect | Original (RPGLE) | Modernized (Java/React) |
|--------|------------------|------------------------|
| **UI Framework** | 5250 Green Screen (DDS) | React Single-Page Application |
| **API Style** | File-based I/O, Indicators | RESTful HTTP JSON API |
| **Backend** | RPGLE Program Logic | Java Spring Boot Service |
| **Data Access** | DDS Physical Files, CHAIN/READ | JPA/Hibernate ORM |
| **Database** | DB2 or IFS Files | PostgreSQL/H2 |
| **Error Handling** | Indicator-based (*IN90) | Exceptions + Global Handler |
| **Deployment** | IBM i System | Standard JVM + Web Server |

### Transformation Scope

**Original CUST001 Program Features:**
- Customer number lookup screen (PROMPT format)
- Customer detail display (DETAIL format)
- Database queries against CUSTMAST file
- Function key navigation (F3=Exit, F12=Return)
- Basic error handling for missing customers

**Modernized Equivalent:**
- REST API endpoints for customer operations
- React component-based UI
- JPA entity/repository pattern for data access
- HTTP status codes for responses
- Comprehensive exception handling and validation

## Building and Running

### Prerequisites

- **Java**: JDK 11 or higher
- **Maven**: 3.6+
- **Node.js**: 14+ with npm
- **Database**: PostgreSQL 12+ (or use H2 for development)

### Backend Setup

```bash
cd final-output/backend
mvn clean install
mvn spring-boot:run
```

Backend runs on `http://localhost:8080`

### Frontend Setup

```bash
cd final-output/frontend
npm install
npm start
```

Frontend runs on `http://localhost:3000`

### API Documentation

See [final-output/README.md](final-output/README.md) for detailed API endpoints and usage examples.

## Baseline Purpose and Metrics

This baseline serves as a reference implementation for:

1. **Comparison Studies**: Evaluate advanced modernization techniques against this simple transformation
2. **Metrics Collection**: Measure code quality, performance, maintainability
3. **Pattern Validation**: Test different architectural patterns for RPGLE modernization
4. **Technology Evaluation**: Assess framework and library choices
5. **Complexity Benchmarking**: Establish baseline for code and architectural complexity

### Collected Metrics

- **Lines of Code**: Source vs. modernized implementation
- **Cyclomatic Complexity**: Original vs. refactored logic
- **Test Coverage**: Unit and integration test percentages
- **Build Time**: Compilation and deployment metrics
- **Runtime Performance**: Response times and resource usage

## Advanced Modernization Techniques

This baseline can be extended to evaluate:

- Event-driven architecture with message queues (Kafka, RabbitMQ)
- Microservices decomposition
- GraphQL API layer
- Container orchestration (Docker, Kubernetes)
- Reactive programming patterns (Project Reactor)
- Domain-driven design implementation
- CQRS and event sourcing
- Advanced caching strategies
- API gateway patterns

## Technical Details

### Backend Architecture

- **Framework**: Spring Boot 2.x with Spring Data JPA
- **Database**: JPA entities with Hibernate ORM
- **API**: RESTful with proper HTTP status codes
- **Error Handling**: Global exception handler with custom exceptions
- **Validation**: Bean Validation with custom validators
- **Configuration**: YAML profiles for dev/prod environments

### Frontend Architecture

- **Framework**: React 17+ with functional components
- **State Management**: React hooks (useState, useContext)
- **HTTP Client**: Axios for REST API calls
- **Styling**: CSS modules and inline styles
- **Component Structure**: Feature-based organization

## Source Code Organization

- [source-rpgle/](source-rpgle/) - Original RPG/DDS files for reference
- [final-output/backend/](final-output/backend/) - Production Java backend
- [final-output/frontend/](final-output/frontend/) - Production React frontend

## Documentation

- [final-output/README.md](final-output/README.md) - Application user guide and API reference
- [final-output/TRANSFORMATION-SUMMARY.md](final-output/TRANSFORMATION-SUMMARY.md) - Detailed technical transformation analysis
- [source-rpgle/programs/README.md](source-rpgle/programs/README.md) - RPGLE program documentation

## Contributing

When extending this baseline for advanced modernization studies:

1. Maintain backward compatibility with API contracts
2. Document all architectural changes
3. Include performance/complexity metrics
4. Update transformation comparison documentation
5. Keep source-rpgle artifacts as reference

## License

This baseline project is provided for educational and research purposes in software modernization techniques.

## Contact & Support

For questions about this modernization baseline or advanced technique implementations, refer to the project documentation and code comments.

---

**Last Updated**: 25 Dec 2025  
**Baseline Version**: 1.0.0  
**Status**: Stable - Ready for advanced technique evaluation
