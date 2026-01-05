# RPGLE Transformer BMAD

A modern full-stack application for transforming legacy RPGLE/DDS systems into contemporary Java and React-based solutions using Business Model Architecture Driven (BMAD) methodology.

> ⚠️ **Note**: This project is currently **not working** and is **suspended**. Not all features are implemented and the application may not run successfully. Please refer to the [Implementation Artifacts](_bmad-output/implementation-artifacts/) for the current status of development tasks.

## Project Overview

This project demonstrates the transformation of legacy IBM i (AS/400) applications built in RPGLE with DDS-based interfaces into a modern microservices architecture with:

- **Backend**: Spring Boot REST API
- **Frontend**: React with TypeScript and Tailwind CSS
- **Database**: Flyway migrations for schema management
- **Testing**: Unit, integration, and end-to-end tests
- **Methodology**: BMAD for structured development planning

## Project Structure

```
rpgle-transformer-bmad/
├── backend/              # Spring Boot application
├── frontend/             # React TypeScript application
├── source-rpgle/         # Original RPGLE source code
│   ├── programs/         # RPGLE programs
│   ├── copybooks/        # RPGLE copybooks
│   └── dds/              # DDS display/physical files
├── docs/                 # Documentation
├── _bmad/                # BMAD configuration and manifests
└── _bmad-output/         # Generated artifacts and planning documents
```

## Quick Start

### Prerequisites

- **Java**: JDK 17 or higher
- **Node.js**: v18 or higher
- **Docker**: For containerized deployment (optional)
- **Maven**: 3.8+

### Backend Setup

```bash
cd backend
./mvnw clean install
./mvnw spring-boot:run
```

The backend API will be available at `http://localhost:8080`

### Frontend Setup

```bash
cd frontend
npm install
npm run dev
```

The frontend will be available at `http://localhost:5173`

### Using Docker Compose

```bash
docker-compose up
```

This starts both the backend and frontend services.

## Development

### Testing

**Backend:**
```bash
cd backend
./mvnw test                    # Unit tests
./mvnw verify                  # Integration tests with TestContainers
```

**Frontend:**
```bash
cd frontend
npm run test                   # Unit tests with Vitest
npm run e2e                    # End-to-end tests with Playwright
```

### Code Quality

- **Backend**: CheckStyle, SpotBugs, JaCoCo coverage
- **Frontend**: ESLint, code coverage reports

## Documentation

- [Workflow Guide](docs/workflow-guide.md) - Development workflow and processes
- [Implementation Artifacts](_bmad-output/implementation-artifacts/) - Detailed implementation tasks
- [Planning Artifacts](_bmad-output/planning-artifacts/) - Architecture, epics, and PRD

## BMAD Configuration

The project uses BMAD methodology for structured development:

- **Manifests**: [_bmad/_config/](/_bmad/_config/) - Agent, task, tool, and workflow definitions
- **Workflows**: [_bmad/core/workflows/](/_bmad/core/workflows/) - Automation workflows
- **Tasks**: [_bmad/core/tasks/](/_bmad/core/tasks/) - Development tasks

## Legacy System Integration

The project includes transformation artifacts from the original RPGLE/DDS system:

- **Source RPGLE**: [source-rpgle/](source-rpgle/) - Original legacy code
- **DDS Files**: Physical and display file definitions
- **Copybooks**: RPGLE copybook structures

## API Documentation

Once the backend is running, API documentation is available at:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI spec: `http://localhost:8080/v3/api-docs`

## Contributing

1. Follow the development workflow in [Workflow Guide](docs/workflow-guide.md)
2. Write tests for new features
3. Ensure code quality checks pass
4. Submit a pull request with clear description

## License

[Add your license information here]

## Support

For issues and questions, please refer to the project documentation or contact the development team.
