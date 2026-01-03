# LANMAM Method: RPGLE Modernization Methodology Comparison

A comprehensive framework for evaluating and comparing different approaches to modernizing legacy RPGLE (RPG IV) programs to modern Java/Spring Boot and React applications.

## Overview

This repository contains three distinct methodologies for converting RPGLE programs to modern web-based applications, along with a detailed comparison framework to evaluate their effectiveness, quality, and production readiness.

### Purpose

- **Compare** different RPGLE modernization approaches
- **Evaluate** conversion quality, architecture, and maintainability
- **Provide** reusable frameworks for legacy system modernization
- **Document** best practices and lessons learned

### Key Results

After comprehensive evaluation of all three approaches on the CUST001 sample program:

🥇 **Winner: Bmad Approach (9.1/10)** - Production-ready with modern architecture, comprehensive testing, and industry standards
🥈 **Runner-up: Agents Approach (8.5/10)** - Excellent documentation and RPGLE traceability
🥉 **Third: Baseline Approach (7.6/10)** - Simple and accessible for learning

See [detailed comparison report](docs/rpgle-conversion-comparison-report.md) for complete analysis.

## Repository Structure

```
lanmam-method/
├── rpgle-transformer-agents/     # 7-phase agent-based conversion framework
│   ├── agents/                    # Specialized AI agents for each conversion phase
│   ├── scripts/                   # Automation scripts for workflow execution
│   ├── common-patterns/           # Reusable conversion patterns
│   ├── documentation/             # Project documentation and tracking
│   ├── source-rpgle/              # RPGLE source programs
│   ├── test-programs/             # Sample programs (CUST001)
│   └── final-output/              # Complete modernized applications
├── rpgle-transformer-baseline/    # Baseline conversion approach
│   └── final-output/              # Generated applications
├── rpgle-transformer-bmad/        # Bmad AI-assisted transformation approach
│   ├── _bmad/                     # Bmad framework and configuration
│   ├── _bmad-output/              # Implementation and planning artifacts
│   ├── backend/                   # Spring Boot application
│   ├── frontend/                  # React + Vite application
│   ├── docs/                      # Standards and workflow documentation
│   ├── source-rpgle/              # RPGLE source programs
│   └── docker-compose.yml         # Container orchestration
└── docs/                          # Comparison framework and reports
    ├── rpgle-conversion-comparison-prompt.md  # Evaluation framework
    └── rpgle-conversion-comparison-report.md  # Detailed comparison analysis
```

## The Three Approaches

### 1. Agent-Based Approach ([rpgle-transformer-agents](rpgle-transformer-agents/))

**Philosophy**: Surgical precision in converting RPGLE programs with exact functional fidelity and comprehensive documentation.

**Key Features**:
- 7-phase conversion workflow (Analysis → Database → Conversion → UI → Testing → Review → Integration)
- LLM-agnostic design (works with Claude, GPT-4, Gemini, local models, etc.)
- Comprehensive testing (unit tests, integration tests)
- Extensive documentation (8+ markdown files per program)
- TypeScript frontend for type safety
- RPGLE naming preservation for easy legacy migration
- Detailed RPGLE-to-Java traceability in code comments

**Scoring**: 8.5/10 (see [comparison report](docs/rpgle-conversion-comparison-report.md))

**Best For**:
- AS/400/iSeries migrations (DB2 compatibility)
- Projects requiring exceptional documentation and RPGLE traceability
- Regulatory environments requiring audit trails
- Teams maintaining legacy database compatibility
- Phased migration strategies with comprehensive testing

### 2. Baseline Approach ([rpgle-transformer-baseline](rpgle-transformer-baseline/))

**Philosophy**: Build modern customer management systems inspired by RPGLE programs with full CRUD capabilities.

**Key Features**:
- Full CRUD REST API (beyond inquiry-only original)
- Modern naming conventions (customer_master vs CUSTMAST)
- JavaScript React frontend
- Comprehensive README documentation
- Profile-based configuration (dev/prod)

**Scoring**: 7.6/10 (see [comparison report](docs/rpgle-conversion-comparison-report.md))

**Best For**:
- Greenfield applications inspired by legacy programs
- Rapid prototyping and stakeholder feedback
- Projects requiring full CRUD from day one
- Learning and training exercises

### 3. Bmad Approach ([rpgle-transformer-bmad](rpgle-transformer-bmad/))

**Philosophy**: AI-assisted transformation for developers with ZERO RPGLE knowledge, using pattern-based implementation and validation-first methodology.

**Key Features**:
- 7-phase transformation workflow (Preparation → Semantic Analysis → Backend → Frontend → Testing → Validation → Documentation)
- Designed for developers without RPGLE expertise
- Modern tech stack: Java 21, Spring Boot 4.0.1, React 19 + Vite, PostgreSQL 16.x
- Pattern-based implementation following CUST001 reference examples
- Comprehensive testing: unit, integration, E2E (≥80% code coverage requirement)
- Validation-first approach ensuring functional equivalence
- Extensive documentation standards (Java, TypeScript, business logic mapping)
- Docker containerization support
- Code quality enforcement (Checkstyle, ESLint, Prettier, PMD)
- RFC 7807 standard error format
- React Query for state management and caching

**Scoring**: 9.1/10 ⭐ **WINNER** (see [comparison report](docs/rpgle-conversion-comparison-report.md))

**Best For**:
- Production applications requiring scalability and maintainability
- Teams without RPGLE knowledge modernizing legacy systems
- Projects requiring highest code quality and comprehensive testing (≥80% coverage)
- Organizations adopting latest Java 21 & Spring Boot 4.x stack
- Long-term projects expecting years of maintenance
- PostgreSQL-based deployments with containerization (Docker)
- Teams with modern JavaScript/TypeScript experience

## Quick Start

### Agent-Based Approach

```bash
# Navigate to the agent-based framework
cd rpgle-transformer-agents

# Test the workflow with the sample CUST001 program
cd test-programs
./test-workflow.sh

# Or set up a full pipeline for your own program
cd ../scripts
./run-full-pipeline.sh YOUR_PROGRAM_NAME

# Copy the generated prompt to use with any LLM
cat work-in-progress/YOUR_PROGRAM_NAME/MASTER-PIPELINE-PROMPT.md | pbcopy
```

See [rpgle-transformer-agents/README.md](rpgle-transformer-agents/README.md) for detailed instructions.

### Baseline Approach

```bash
# Navigate to baseline output
cd rpgle-transformer-baseline/final-output

# Review the generated application
cat README.md

# Examine the transformation summary
cat TRANSFORMATION-SUMMARY.md
```

See [rpgle-transformer-baseline/final-output/README.md](rpgle-transformer-baseline/final-output/README.md) for setup instructions.

### Bmad Approach

```bash
# Navigate to the Bmad framework
cd rpgle-transformer-bmad

# Review the comprehensive workflow guide
cat docs/workflow-guide.md

# Check transformation standards and checklists
ls docs/standards/

# Start backend and database with Docker
docker-compose up -d

# Review the project context and coding rules
cat _bmad-output/project-context.md
```

See [rpgle-transformer-bmad/docs/workflow-guide.md](rpgle-transformer-bmad/docs/workflow-guide.md) for the complete transformation process.

## Comparison Framework

The [docs](docs/) directory contains a comprehensive evaluation framework for comparing RPGLE conversion approaches:

### Evaluation Criteria

1. **Functional Completeness** (30%): Business logic preservation, validation, error handling
2. **Architecture Quality** (25%): Separation of concerns, RESTful design, code organization
3. **Code Quality** (20%): Readability, maintainability, testing, documentation, security
4. **UI/UX Translation** (15%): Screen layout fidelity, user flow, accessibility
5. **Data Model Translation** (10%): Schema correctness, type mapping, migration path

### Key Findings

| Criterion | Weight | Baseline | Agents | Bmad | Winner |
|-----------|--------|----------|--------|------|--------|
| Functional Completeness | 30% | 8.0/10 | 9.0/10 | 9.0/10 | Agents/Bmad |
| Architecture Quality | 25% | 7.0/10 | 8.0/10 | 9.5/10 | **Bmad** ⭐ |
| Code Quality | 20% | 7.0/10 | 8.5/10 | 9.0/10 | **Bmad** ⭐ |
| UI/UX Translation | 15% | 8.0/10 | 8.5/10 | 9.0/10 | **Bmad** ⭐ |
| Data Model Translation | 10% | 8.0/10 | 8.5/10 | 9.0/10 | **Bmad** ⭐ |
| **WEIGHTED TOTAL** | 100% | **7.6/10** | **8.5/10** | **9.1/10** | **Bmad** ⭐ |

**Winner**: **Bmad Approach (9.1/10)** ⭐

**Why Bmad Won:**
- **Modern Architecture (9.5/10)**: Feature-based structure, React Query, dedicated mapper class, API versioning (/api/v1/)
- **Production Tooling (8.9/10)**: Docker Compose, Flyway migrations, comprehensive quality gates
- **Code Quality (9.0/10)**: TypeScript end-to-end, JaCoCo 80% coverage, Checkstyle, PMD, ESLint, Prettier
- **Developer Experience**: Zod validation, React Hook Form, React Query caching, Tailwind CSS
- **Industry Standards**: RFC 7807 error format, REST best practices

**Each Approach's Strength:**
- **Bmad**: Production-ready with modern architecture and comprehensive tooling
- **Agents**: Exceptional documentation with RPGLE source code traceability (31+ unit tests)
- **Baseline**: Simple and accessible for learning, rapid prototyping

See the [full comparison report](docs/rpgle-conversion-comparison-report.md) for detailed 632-line analysis.

## Technology Stack

### Backend
- **Java**: 17+ (Agent-Based, Baseline) / 21 LTS (Bmad)
- **Spring Boot**: 3.x (Agent-Based, Baseline) / 4.0.1 (Bmad)
- **ORM**: Spring Data JPA / Hibernate
- **Validation**: Spring Boot Validation
- **Database**: DB2 JDBC Driver / H2 (Agent-Based, Baseline) / PostgreSQL 16.x (Bmad)
- **Build Tool**: Maven
- **Testing**: JUnit 5, Mockito, Testcontainers (Bmad)
- **Code Quality**: Checkstyle, PMD (Bmad)

### Frontend
- **React**: 18 (Agent-Based, Baseline) / 19 (Bmad)
- **Language**: TypeScript (Agent-Based, Bmad) / JavaScript (Baseline)
- **Build Tool**: react-scripts (Agent-Based, Baseline) / Vite (Bmad)
- **Routing**: React Router
- **HTTP Client**: Axios / Fetch API
- **Testing**: Vitest, Playwright (Bmad)
- **Code Quality**: ESLint, Prettier (Bmad)

### Infrastructure
- **Containerization**: Docker Compose (Bmad)

## Sample Application: CUST001

All three approaches include a complete conversion of **CUST001** (Customer Inquiry), a simple but complete RPGLE program that demonstrates common legacy patterns.

### Program Overview

CUST001 is a customer inquiry program that allows users to look up customer information by customer number. It demonstrates typical RPGLE patterns including:
- File I/O operations (CHAIN lookup)
- Interactive screen handling (ExFmt)
- Input validation and error messaging
- Function key handling (F3=Exit, F12=Return)
- Indicator-based flow control

### Data Structure: CUSTMAST File

The CUSTMAST physical file contains customer master records with the following fields:

| Field | Type | Description |
|-------|------|-------------|
| CUSTNO | 5P 0 | Customer Number (Primary Key) |
| CUSTNAME | 30A | Customer Name |
| ADDR1 | 30A | Address Line 1 |
| CITY | 20A | City |
| STATE | 2A | State Code |
| ZIP | 5P 0 | Zip Code |
| PHONE | 12A | Phone Number |
| BALANCE | 9P 2 | Account Balance |
| CREDITLIM | 9P 2 | Credit Limit |
| LASTORDER | 8P 0 | Last Order Date (YYYYMMDD) |

### Screen Flow

The program uses a display file (CUSTDSP) with two record formats:

#### 1. PROMPT Screen (Entry)
```
CUST001                    Customer Inquiry                      12:34:56
                                                                  01/03/26

Customer Number: _____

Error: [error message if validation fails]

F3=Exit
```

**Fields**:
- `PCUSTNO`: Input field for customer number (5-digit, zero-suppressed)
- `PMSG`: Error message field (displayed when indicator 90 is on)

**Function Keys**:
- F3 (indicator 03): Exit program
- F12 (indicator 12): Cancel/Return

#### 2. DETAIL Screen (Display)
```
CUST001                    Customer Detail                       12:34:56
                                                                  01/03/26

Customer Number: 12345
Name:            John Smith
Address:         123 Main Street
City:            Springfield
State:           IL
Zip:             62701
Phone:           217-555-1234
Balance:         1,234.56

F3=Exit  F12=Return
```

**Fields**: All output-only, populated from CUSTMAST record
- `DCUSTNO`, `DCUSTNAME`, `DADDR1`, `DCITY`, `DSTATE`, `DZIP`, `DPHONE`, `DBALANCE`

### Program Logic Flow

```
1. Display PROMPT screen (ExFmt PROMPT)
2. LOOP while F3 not pressed (*IN03 = *Off):
   a. Clear error indicator (*IN90 = *Off) and message (PMSG)

   b. VALIDATE customer number:
      - IF PCUSTNO = 0:
        * Set error indicator (*IN90 = *On)
        * Set error message: "Customer number required"
        * Re-display PROMPT screen
        * Continue to next iteration

   c. LOOKUP customer in CUSTMAST:
      - CHAIN PCUSTNO to CUSTMAST file

   d. IF customer found (%Found):
      * Copy CUSTMAST fields to DETAIL screen fields
      * Display DETAIL screen (ExFmt DETAIL)
      ELSE:
      * Set error indicator (*IN90 = *On)
      * Set error message: "Customer not found"

   e. Re-display PROMPT screen (ExFmt PROMPT)
3. END LOOP
4. Set Last Record (*INLR = *On) and exit
```

### Key RPGLE Concepts Demonstrated

1. **File Declarations**:
   - `FCUSTMAST IF E K DISK`: Input file, externally described, keyed access
   - `FCUSTDSP CF E WORKSTN`: Combined I/O file, externally described, workstation

2. **Screen I/O**:
   - `ExFmt`: Write then Read (display screen and wait for user input)
   - Indicator-based error display (*IN90 controls error message visibility)

3. **Database Operations**:
   - `CHAIN`: Random read by key (retrieves record matching PCUSTNO)
   - `%Found()`: Built-in function to check if CHAIN was successful

4. **Flow Control**:
   - `DoW` (Do While): Loop while condition is true
   - `Iter`: Continue to next iteration (skip remaining loop code)
   - Indicator-based termination (*IN03 for F3 key)

5. **Data Validation**:
   - Simple non-zero check for required input
   - Error messaging with conditional display

### Business Rules

1. **Customer Number Required**: Users must enter a non-zero customer number
2. **Inquiry Only**: No create, update, or delete operations (read-only access)
3. **Real-time Lookup**: Each inquiry performs a fresh database read
4. **User-Friendly Navigation**: F3 exits, F12 returns to entry screen from detail

This serves as a reference implementation and testing ground for the conversion methodologies, demonstrating how each approach handles:
- Interactive screen flows → REST APIs + React forms
- File I/O → JPA/Spring Data repository patterns
- Indicator logic → Boolean validation and error handling
- Function keys → UI navigation and cancel operations
- DDS field definitions → Database schemas and entity models

## Key Differentiators

| Feature | Agent-Based | Baseline | Bmad |
|---------|-------------|----------|------|
| **Test Coverage** | 31+ unit tests (267 lines) | 0 tests | 21 backend + Vitest + Playwright E2E |
| **Documentation** | 8 detailed MD files with RPGLE mapping | 1 README | Extensive standards + workflow guide (2,400 lines) |
| **Frontend Language** | TypeScript | JavaScript | TypeScript |
| **React Version** | 18.2 | 18.2 | 19.2 |
| **Build Tool** | react-scripts | react-scripts | Vite |
| **State Management** | useState | useState | React Query |
| **Form Handling** | Manual | Manual | React Hook Form + Zod |
| **Styling** | CSS | CSS | Tailwind CSS |
| **Scope Fidelity** | Exact match to RPGLE | Enhanced with CRUD | Inquiry-only (matches RPGLE) |
| **RPGLE Traceability** | Extensive JavaDoc comments | Minimal | DDS source in Flyway migrations |
| **Table Naming** | Preserves RPGLE (CUSTMAST) | Modern (customer_master) | Preserves RPGLE (CUSTMAST) |
| **Database Migrations** | None (Hibernate DDL) | None (Hibernate DDL) | **Flyway** |
| **Containerization** | None | None | **Docker Compose** |
| **API Versioning** | No | No | **Yes (/api/v1/)** |
| **Error Format** | Custom Map | Spring default | **RFC 7807** |
| **Code Quality Gates** | None | None | Checkstyle, PMD, JaCoCo 80%, ESLint, Prettier |
| **Target Developers** | RPGLE knowledgeable | Any | ZERO RPGLE knowledge required |
| **Production Readiness** | 7.6/10 (needs containerization) | 4.8/10 (needs tests) | **8.9/10** ⭐ |

## Usage Recommendations

### Use Agent-Based Approach When:
- Converting legacy RPGLE programs to modern stack
- Requiring traceability to original source
- Implementing phased migration strategies
- Needing comprehensive documentation and testing
- Working with existing DB2 databases
- Operating in regulated environments

### Use Baseline Approach When:
- Building new systems inspired by RPGLE
- Requiring full CRUD operations from day one
- Creating greenfield applications
- Rapid prototyping for stakeholder feedback
- Learning and training scenarios

### Use Bmad Approach When:
- Team has NO RPGLE expertise
- Modernizing to latest Java 21 & Spring Boot 4.x stack
- Adopting React 19 with Vite for optimal performance
- Requiring comprehensive testing (≥80% coverage)
- Deploying to PostgreSQL infrastructure
- Needing strict code quality enforcement (Checkstyle, PMD, ESLint, Prettier)
- Following validation-first methodology
- Using containerized deployments (Docker)
- AI-assisted transformation workflow preferred

## Getting Started

1. **Review the comparison framework**: Start with [docs/rpgle-conversion-comparison-prompt.md](docs/rpgle-conversion-comparison-prompt.md)
2. **Explore all three approaches**: Examine the sample CUST001 implementations
3. **Test workflows**:
   - Agent-based: Run [rpgle-transformer-agents/test-programs/test-workflow.sh](rpgle-transformer-agents/test-programs/test-workflow.sh)
   - Bmad: Review [rpgle-transformer-bmad/docs/workflow-guide.md](rpgle-transformer-bmad/docs/workflow-guide.md)
4. **Choose your approach**: Based on your team expertise, tech stack, and project requirements
5. **Customize templates**: Adapt to your organization's standards

## Documentation

- [Agent-Based README](rpgle-transformer-agents/README.md) - Comprehensive framework guide
- [Agent-Based Workflow Guide](rpgle-transformer-agents/WORKFLOW-GUIDE.md) - Step-by-step execution
- [Baseline README](rpgle-transformer-baseline/final-output/README.md) - Setup and usage
- [Bmad Workflow Guide](rpgle-transformer-bmad/docs/workflow-guide.md) - 7-phase transformation process
- [Bmad Documentation Standards](rpgle-transformer-bmad/docs/standards/) - Java, TypeScript, and business logic standards
- [Comparison Framework](docs/rpgle-conversion-comparison-prompt.md) - Evaluation criteria (includes Bmad)
- [Comparison Report](docs/rpgle-conversion-comparison-report.md) - Detailed analysis (1,697 lines)

## Project Metrics

### Agent-Based Approach
- Backend Production Code: ~450 lines
- Backend Test Code: ~267 lines (59% coverage ratio)
- Frontend Code: ~350 lines (TypeScript)
- Documentation: ~1,500 lines
- Test-to-Code Ratio: 59%

### Baseline Approach
- Backend Production Code: ~350 lines
- Backend Test Code: 0 lines
- Frontend Code: ~280 lines (JavaScript)
- Documentation: ~500 lines
- Test-to-Code Ratio: 0%

### Bmad Approach
- Backend Production Code: Modern feature-based architecture
- Backend Test Files: 21 test files (JUnit + Mockito + Testcontainers)
- Frontend Code: TypeScript + Vite with React 19
- Frontend Testing: Vitest + Playwright E2E
- Documentation: Extensive (workflow guide: ~2,400 lines, comprehensive standards)
- Code Coverage: JaCoCo 80% minimum enforced
- Quality Gates: Checkstyle (Google), PMD, ESLint, Prettier
- Infrastructure: Docker Compose + Flyway migrations
- Production Readiness: 8.9/10 ⭐

## Contributing

This repository serves as a reference implementation for RPGLE modernization methodologies. Contributions are welcome for:

- Additional conversion patterns
- New test programs
- Enhanced automation scripts
- Documentation improvements
- Alternative conversion approaches

## Future Enhancements

### Agent-Based Approach
- Integration tests for full stack
- Frontend tests (Jest, React Testing Library)
- API versioning
- Caching layer (Redis/Caffeine)
- Metrics and monitoring

### Baseline Approach
- Comprehensive test suite (critical)
- TypeScript migration
- RPGLE mapping documentation
- Data migration guide
- Deployment guide

### Bmad Approach
- Complete CUST001 reference implementation
- Additional program transformations
- Automated validation tooling
- CI/CD pipeline integration
- Performance benchmarking
- Migration playbooks for common RPGLE patterns

## License

[Specify your license here]

## Acknowledgments

Built with AI-assisted development using:
- Claude Code (Anthropic)
- Claude Sonnet 4.5 for comprehensive analysis
- Automated prompt generation for LLM-agnostic workflow

## Comparison Report

The comprehensive [comparison report](docs/rpgle-conversion-comparison-report.md) (632 lines) includes:
- Detailed evaluation of all three approaches
- Criterion-by-criterion scoring with evidence
- Production readiness assessment
- Technology stack comparison
- Specific recommendations for each use case
- Migration timeline estimates

---

**Created**: December 2025 - January 2026
**Last Updated**: January 2, 2026
**Purpose**: Three-way RPGLE modernization methodology comparison
**Target**: Legacy system modernization teams
**Approach**: Comprehensive quality analysis with weighted scoring
**Winner**: Bmad Approach (9.1/10) for production deployments
