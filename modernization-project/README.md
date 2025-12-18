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

### 2. Run the Full Pipeline Setup
```bash
cd scripts
./run-full-pipeline.sh PROGRAM_NAME
```

This will:
- Create workspaces for all 6 phases
- Copy templates and reference files
- Generate LLM-agnostic prompts for each phase
- Create a master prompt for the complete pipeline

### 3. Choose Your Workflow

**Option A: Single LLM Session (Recommended for powerful LLMs)**
```bash
# Copy the master prompt to your clipboard
cat work-in-progress/PROGRAM_NAME/MASTER-PIPELINE-PROMPT.md | pbcopy

# Then paste into your preferred LLM:
# - Claude Code
# - ChatGPT
# - GitHub Copilot
# - Local models (Ollama, etc.)
```

**Option B: Individual Phase Sessions**
```bash
# Run each phase separately
cd scripts
./run-analysis.sh PROGRAM_NAME      # Phase 1
./run-database.sh PROGRAM_NAME      # Phase 2
./run-conversion.sh PROGRAM_NAME    # Phase 3
./run-ui.sh PROGRAM_NAME            # Phase 4
./run-testing.sh PROGRAM_NAME       # Phase 5
./run-review.sh PROGRAM_NAME        # Phase 6

# Copy each phase prompt as needed
cat work-in-progress/PROGRAM_NAME/01-analysis/PROMPT.md | pbcopy
```

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

## Usage with Any LLM (LLM-Agnostic Design)

The pipeline is designed to work with **any LLM tool**:

**Automated Prompt Generation:**
```bash
# Run the setup script
cd scripts
./run-full-pipeline.sh CUST001

# Copy the generated prompt
cat work-in-progress/CUST001/MASTER-PIPELINE-PROMPT.md | pbcopy
```

**Compatible with:**
- Claude Code (CLI or VS Code extension)
- ChatGPT (web or API)
- GitHub Copilot
- Local models (Ollama, LM Studio, etc.)
- Any LLM with sufficient context window

The scripts generate standalone prompt files that include all necessary instructions, making them portable across any LLM platform.

## Testing the Workflow

**Before processing production programs**, test the workflow with sample programs:

```bash
# Run the test workflow
cd test-programs
./test-workflow.sh

# This will set up CUST001 (a simple customer inquiry program)
# Follow the prompts to test with your preferred LLM
```

See [test-programs/README.md](test-programs/README.md) for complete testing instructions and validation checklist.

## Next Steps

1. **Test the workflow** with `test-programs/CUST001` (recommended first step!)
2. Customize templates for your company's standards
3. Add your RPGLE source files
4. Start with a simple production program as a calibration exercise
5. Build up the common patterns library as you discover reusable solutions

## Notes

- Each program conversion creates a folder in `work-in-progress/` with subfolders for each agent's output
- The `final-output/` directory contains the Spring Boot application structure
- Update `common-patterns/lessons-learned.md` as you encounter and solve new challenges
- Use the issues log to track blockers and recurring problems

---

**Created**: 2025-12-15
**Purpose**: AI-assisted RPGLE to Java modernization for 50 programs
