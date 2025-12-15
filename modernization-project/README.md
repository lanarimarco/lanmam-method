# RPGLE to Java Modernization Project

This project structure has been set up to support the AI-assisted conversion of 50 RPGLE programs to modern Java applications.

## Project Structure

```
modernization-project/
├── agents/                      # Configuration for 6 specialized AI agents
│   ├── 01-analysis-agent/       # Analyzes RPGLE programs
│   ├── 02-database-agent/       # Creates JPA entities and repositories
│   ├── 03-conversion-agent/     # Converts RPGLE to Java services
│   ├── 04-ui-agent/             # Creates React UI components
│   ├── 05-testing-agent/        # Creates test suites
│   └── 06-review-agent/         # Reviews and refactors code
├── common-patterns/             # Reusable patterns and knowledge base
├── source-rpgle/                # Place your RPGLE source files here
│   ├── programs/                # RPGLE programs (.rpgle files)
│   ├── dds/                     # DDS files
│   │   ├── display-files/       # Display file DDS
│   │   ├── physical-files/      # Physical file DDS
│   │   └── logical-files/       # Logical file DDS
│   └── copybooks/               # Copybook files
├── work-in-progress/            # Active conversion work (created per program)
├── final-output/                # Spring Boot application with converted code
├── documentation/               # Project documentation and tracking
│   └── progress-tracking/       # Status tracking and issues log
└── scripts/                     # Helper scripts for running agents
```

## Quick Start

### 1. Add Your RPGLE Source Files
Place your RPGLE programs in the appropriate directories:
- Programs: `source-rpgle/programs/`
- DDS files: `source-rpgle/dds/`
- Copybooks: `source-rpgle/copybooks/`

### 2. Run Analysis on First Program
```bash
cd scripts
./run-analysis.sh PROGRAM_NAME
```

### 3. Follow the Agent Workflow
Each agent has a README.md with detailed instructions:
1. Analysis Agent (`agents/01-analysis-agent/README.md`)
2. Database Agent (`agents/02-database-agent/README.md`)
3. Conversion Agent (`agents/03-conversion-agent/README.md`)
4. UI Agent (`agents/04-ui-agent/README.md`)
5. Testing Agent (`agents/05-testing-agent/README.md`)
6. Review Agent (`agents/06-review-agent/README.md`)

### 4. Track Progress
Update the conversion status in:
- `documentation/progress-tracking/conversion-status.md`
- `documentation/progress-tracking/issues-log.md`

## Key Documents

- **Project Overview**: `documentation/project-overview.md`
- **Architecture Decisions**: `documentation/architecture-decisions.md`
- **Common Patterns**: `common-patterns/` directory
- **Conversion Status**: `documentation/progress-tracking/conversion-status.md`

## Technology Stack

- **Backend**: Spring Boot 3.x with Java 17+
- **Database**: JPA/Hibernate for DB2
- **UI**: React with TypeScript
- **Testing**: JUnit 5, Mockito
- **Build**: Maven

## Agent Templates

Each agent folder contains templates to ensure consistency:
- Analysis templates for documenting RPGLE programs
- Entity and repository templates for database layer
- Service, controller, and DTO templates for business logic
- React component templates for UI
- Test templates for comprehensive testing

## Usage with Claude Code

When converting a program, start a new Claude Code session and reference the appropriate agent folder:

```
"Using the agent instructions in /agents/01-analysis-agent/,
analyze program CUST001.rpgle from /source-rpgle/programs/"
```

## Next Steps

1. Review the setup guide: `rpgle-java-modernization-setup.md` (if you still have it)
2. Customize templates for your company's standards
3. Add your RPGLE source files
4. Start with a simple program (e.g., CUST001) as a calibration exercise
5. Build up the common patterns library as you discover reusable solutions

## Notes

- Each program conversion creates a folder in `work-in-progress/` with subfolders for each agent's output
- The `final-output/` directory contains the Spring Boot application structure
- Update `common-patterns/lessons-learned.md` as you encounter and solve new challenges
- Use the issues log to track blockers and recurring problems

---

**Created**: 2025-12-15
**Purpose**: AI-assisted RPGLE to Java modernization for 50 programs
