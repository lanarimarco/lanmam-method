# LANMAM Method: RPGLE Modernization Methodology Comparison

A comprehensive framework for evaluating and comparing different approaches to modernizing legacy RPGLE (RPG IV) programs to modern Java/Spring Boot and React applications.

## Overview

This repository contains two distinct methodologies for converting RPGLE programs to modern web-based applications, along with a detailed comparison framework to evaluate their effectiveness, quality, and production readiness.

### Purpose

- **Compare** different RPGLE modernization approaches
- **Evaluate** conversion quality, architecture, and maintainability
- **Provide** reusable frameworks for legacy system modernization
- **Document** best practices and lessons learned

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
└── docs/                          # Comparison framework and reports
    ├── rpgle-conversion-comparison-prompt.md  # Evaluation framework
    └── rpgle-conversion-comparison-report.md  # Detailed comparison analysis
```

## The Two Approaches

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

**Scoring**: 8.95/10 (see [comparison report](docs/rpgle-conversion-comparison-report.md))

**Best For**:
- Production RPGLE conversions requiring high fidelity
- Projects needing comprehensive documentation and testing
- Phased migration strategies
- Regulatory environments requiring audit trails
- Teams maintaining legacy database compatibility

### 2. Baseline Approach ([rpgle-transformer-baseline](rpgle-transformer-baseline/))

**Philosophy**: Build modern customer management systems inspired by RPGLE programs with full CRUD capabilities.

**Key Features**:
- Full CRUD REST API (beyond inquiry-only original)
- Modern naming conventions (customer_master vs CUSTMAST)
- JavaScript React frontend
- Comprehensive README documentation
- Profile-based configuration (dev/prod)

**Scoring**: 6.52/10 (see [comparison report](docs/rpgle-conversion-comparison-report.md))

**Best For**:
- Greenfield applications inspired by legacy programs
- Rapid prototyping and stakeholder feedback
- Projects requiring full CRUD from day one
- Learning and training exercises

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

## Comparison Framework

The [docs](docs/) directory contains a comprehensive evaluation framework for comparing RPGLE conversion approaches:

### Evaluation Criteria

1. **Functional Completeness** (30%): Business logic preservation, validation, error handling
2. **Architecture Quality** (25%): Separation of concerns, RESTful design, code organization
3. **Code Quality** (20%): Readability, maintainability, testing, documentation, security
4. **UI/UX Translation** (15%): Screen layout fidelity, user flow, accessibility
5. **Data Model Translation** (10%): Schema correctness, type mapping, migration path

### Key Findings

| Criterion | Agent-Based | Baseline | Winner |
|-----------|-------------|----------|--------|
| Functional Completeness | 9.5/10 | 6.0/10 | **Agents** (+3.5) |
| Architecture Quality | 9.0/10 | 7.0/10 | **Agents** (+2.0) |
| Code Quality | 9.0/10 | 6.5/10 | **Agents** (+2.5) |
| UI/UX Translation | 8.5/10 | 7.0/10 | **Agents** (+1.5) |
| Data Model Translation | 9.5/10 | 5.5/10 | **Agents** (+4.0) |
| **WEIGHTED TOTAL** | **8.95/10** | **6.52/10** | **Agents** (+2.43) |

**Winner**: Agent-Based Approach

See the [full comparison report](docs/rpgle-conversion-comparison-report.md) for detailed analysis.

## Technology Stack

### Backend
- Java 17+
- Spring Boot 3.x
- Spring Data JPA / Hibernate
- Spring Boot Validation
- DB2 JDBC Driver / H2 Database
- Maven
- JUnit 5, Mockito (testing)

### Frontend
- React 18
- TypeScript (agent-based) / JavaScript (baseline)
- React Router
- Axios / Fetch API
- react-scripts

## Sample Application: CUST001

Both approaches include a complete conversion of **CUST001** (Customer Inquiry), a simple RPGLE program that:
- Prompts user for customer number
- Validates input (non-zero)
- Looks up customer in CUSTMAST file
- Displays customer details (name, address, phone, balance)
- Handles F3 (Exit) and F12 (Return) function keys

This serves as a reference implementation and testing ground for the conversion methodologies.

## Key Differentiators

| Feature | Agent-Based | Baseline |
|---------|-------------|----------|
| **Test Coverage** | 267 lines of unit tests | No tests |
| **Documentation** | 8 detailed MD files | 1 README |
| **Frontend Language** | TypeScript | JavaScript |
| **Scope Fidelity** | Exact match to RPGLE | Enhanced with CRUD |
| **RPGLE Traceability** | Extensive comments | Minimal |
| **Table Naming** | Preserves RPGLE names | Modern conventions |
| **Migration Complexity** | LOW (compatible naming) | MEDIUM-HIGH (renaming required) |
| **Production Ready** | Yes (minor additions needed) | No (tests required) |

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

## Getting Started

1. **Review the comparison report**: Start with [docs/rpgle-conversion-comparison-report.md](docs/rpgle-conversion-comparison-report.md)
2. **Explore both approaches**: Examine the sample CUST001 implementations
3. **Test the agent-based workflow**: Run [rpgle-transformer-agents/test-programs/test-workflow.sh](rpgle-transformer-agents/test-programs/test-workflow.sh)
4. **Choose your approach**: Based on your project requirements
5. **Customize templates**: Adapt to your organization's standards

## Documentation

- [Agent-Based README](rpgle-transformer-agents/README.md) - Comprehensive framework guide
- [Agent-Based Workflow Guide](rpgle-transformer-agents/WORKFLOW-GUIDE.md) - Step-by-step execution
- [Baseline README](rpgle-transformer-baseline/final-output/README.md) - Setup and usage
- [Comparison Framework](docs/rpgle-conversion-comparison-prompt.md) - Evaluation criteria
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

## License

[Specify your license here]

## Acknowledgments

Built with AI-assisted development using:
- Claude Code (Anthropic)
- Claude Opus/Sonnet models
- Automated prompt generation for LLM-agnostic workflow

---

**Created**: December 2025
**Purpose**: RPGLE modernization methodology comparison and reference implementation
**Target**: Legacy system modernization teams
**Approach**: Multi-methodology evaluation with comprehensive quality analysis
