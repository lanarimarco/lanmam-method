# RPGLE to Java Modernization Project

AI-assisted modernization framework for converting RPGLE programs to modern Java/Spring Boot applications using a 4-phase agent-based workflow.

## Project Structure

```
rpgle-transformer-agents/
├── agents/                      # Configuration for 4 specialized AI agents
│   ├── 10-analysis-agent/       # Analyzes RPGLE programs
│   │   ├── PROMPT.md            # Analysis phase prompt template
│   │   └── ...                  # Supporting templates and checklists
│   ├── 20-database-agent/       # Creates JPA entities and repositories
│   │   ├── PROMPT.md            # Database phase prompt template
│   │   └── ...                  # Naming conventions, type mappings, code templates
│   ├── 30-conversion-agent/     # Converts RPGLE to Java services
│   │   ├── PROMPT.md            # Conversion phase prompt template
│   │   └── ...                  # Conversion patterns, code templates
│   └── 40-testing-agent/        # Generates test suites
│       ├── PROMPT.md            # Testing phase prompt template
│       └── ...                  # Test templates
├── scripts/                     # Automation scripts
│   ├── run-full-pipeline.sh    # Complete 4-phase setup
│   ├── run-analysis.sh         # Phase 1 setup
│   ├── run-database.sh         # Phase 2 setup
│   ├── run-conversion.sh       # Phase 3 setup
│   └── run-testing.sh          # Phase 4 setup
├── source-rpgle/                # Your RPGLE source files
│   ├── programs/                # RPGLE programs (.rpgle files)
│   ├── dds/                     # DDS files
│   │   ├── display-files/       # Display file DDS
│   │   ├── physical-files/      # Physical file DDS
│   │   └── logical-files/       # Logical file DDS
│   └── copybooks/               # Copybook files
├── test-programs/               # Sample programs for testing workflow
│   ├── programs/                # Test RPGLE programs (CUST001)
│   ├── dds/                     # Test DDS files
│   ├── test-workflow.sh         # Automated test workflow
│   └── README.md                # Testing guide and validation checklist
├── work-in-progress/            # Active conversion workspaces (created dynamically by scripts)
│   └── {PROGRAM}/               # Individual program workspace
│       ├── 10-analysis/         # Analysis phase outputs
│       ├── 20-database/         # Database layer code
│       ├── 30-conversion/       # Business logic code
│       ├── 40-testing/          # Test suites
│       └── MASTER-PIPELINE-PROMPT.md  # Complete pipeline prompt
├── final-output/                # Completed conversion outputs
│   └── {PROGRAM}/               # Individual program final output
│       ├── docs/                # Agent-generated documentation (always present)
│       │   ├── analysis.md
│       │   ├── database-notes.md
│       │   ├── conversion-notes.md
│       │   ├── ui-notes.md
│       │   ├── testing-notes.md
│       │   ├── review-report.md
│       │   └── deployment-guide.md
│       ├── backend/             # Java/Spring Boot code (architectural decision)
│       │   ├── entities/
│       │   ├── repositories/
│       │   ├── services/
│       │   ├── controllers/
│       │   ├── dtos/
│       │   └── tests/
│       └── frontend/            # React/TypeScript code (architectural decision)
│           ├── pages/
│           ├── services/
│           ├── types/
│           └── tests/
├── common-patterns/             # Reusable patterns and knowledge base
│   ├── date-time-conversion.md  # Date/time handling patterns
│   ├── error-handling-strategy.md
│   ├── numeric-handling.md      # Packed decimal conversions
│   ├── logging-standards.md
│   ├── maven-project-structure.md
│   ├── react-project-structure.md
│   ├── lessons-learned.md       # Project-specific solutions
│   └── utilities/               # Utility code snippets
├── documentation/               # Project documentation
│   ├── project-overview.md      # High-level project summary
│   ├── architecture-decisions.md
│   └── progress-tracking/
│       ├── conversion-status.md  # Program status tracker
│       └── issues-log.md        # Issues and blockers log
├── project-settings.md          # Global project configuration
└── WORKFLOW-GUIDE.md            # Detailed workflow documentation
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
- Create workspaces for all 4 phases
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
./run-testing.sh PROGRAM_NAME       # Phase 4

# Copy each phase prompt as needed
cat work-in-progress/PROGRAM_NAME/10-analysis/PROMPT.md | pbcopy
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

**Backend:**
- Java 17+
- Spring Boot 3.2.0
- Spring Data JPA / Hibernate
- Spring Boot Validation
- DB2 JDBC Driver 11.5.8.0
- H2 Database (for development/testing)
- Lombok (optional, for reducing boilerplate)
- Maven

**Frontend:**
- React 18.2.0
- TypeScript 4.9.5
- React Router 6.20.0
- Axios 1.6.2
- react-scripts 5.0.1

**Testing:**
- JUnit 5 (Spring Boot Test)
- Mockito
- AssertJ
- React Testing Library

## The 4-Phase Conversion Workflow

### Phase 1: Analysis Agent
**Purpose:** Analyze and document RPGLE program structure and business logic

**Templates:**
- `analysis-template.md` - Program documentation template
- `checklist.md` - Analysis quality checklist

**Outputs:**
- `analysis.md` - Complete program analysis document

### Phase 2: Database Agent
**Purpose:** Convert DDS files to JPA entities and Spring Data repositories

**Templates:**
- `naming-conventions.md` - Entity and field naming standards
- `type-mappings.md` - AS/400 to Java type conversion rules

**Outputs:**
- `entities/*.java` - JPA entity classes
- `repositories/*.java` - Spring Data repositories
- `database-notes.md` - Database layer documentation

### Phase 3: Conversion Agent
**Purpose:** Convert RPGLE business logic to Java services and REST controllers

**Templates:**
- `conversion-notes-template.md` - Conversion documentation template
- `conversion-patterns.md` - Common RPGLE to Java patterns

**Outputs:**
- `services/*.java` - Service layer classes
- `controllers/*.java` - REST controllers
- `dtos/*.java` - Data transfer objects
- `conversion-notes.md` - Business logic documentation

### Phase 4: Testing Agent
**Purpose:** Generate comprehensive test suites

**Outputs:**
- `unit-tests/*.java` - Service unit tests
- `integration-tests/*.java` - Controller integration tests
- `testing-notes.md` - Test coverage documentation
- `deployment-guide-template.md` - Deployment documentation template
- `integration-checklist.md` - Integration verification checklist

**Outputs:**
- Build verification reports
- Deployment documentation
- Integration status report

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

## Common Patterns Library

The `common-patterns/` directory contains reusable knowledge and best practices:

- **date-time-conversion.md**: AS/400 date/time to Java LocalDate/LocalDateTime patterns
- **error-handling-strategy.md**: Exception handling approaches for modernized code
- **numeric-handling.md**: Packed decimal and numeric type conversions
- **logging-standards.md**: Logging best practices for Spring Boot applications
- **maven-project-structure.md**: Maven project organization and dependencies
- **react-project-structure.md**: React application structure and patterns
- **lessons-learned.md**: Project-specific solutions and discoveries

**Best Practice:** Update these files as you discover new patterns and solutions during conversions!

## Workflow Execution Modes

### Single Session Mode
Best for: Powerful LLMs with large context windows (200K+ tokens)

**Advantages:**
- Complete conversion in one session
- Better context retention across all phases
- Faster overall execution
- Fewer manual steps

**Process:**
1. Run `./scripts/run-full-pipeline.sh PROGRAM_NAME`
2. Copy `work-in-progress/PROGRAM_NAME/MASTER-PIPELINE-PROMPT.md`
3. Paste into LLM (Claude Opus/Sonnet, GPT-4 Turbo, etc.)
4. Let LLM work through all 4 phases autonomously

### Phase-by-Phase Mode
Best for: Cost management, iterative review, smaller context windows

**Advantages:**
- Better control over each phase
- Can review and validate before proceeding
- Works with smaller context windows
- More cost-effective for API-based LLMs

**Process:**
1. Run individual phase script (e.g., `./scripts/run-analysis.sh PROGRAM_NAME`)
2. Copy phase PROMPT.md from workspace
3. Paste into LLM and execute
4. Review outputs
5. Proceed to next phase

## Tracking Progress

### Individual Program Status

Each program conversion creates a structured workspace:

```
work-in-progress/{PROGRAM}/
├── 10-analysis/analysis.md
├── 20-database/
│   ├── entities/*.java
│   ├── repositories/*.java
│   └── database-notes.md
├── 30-conversion/
│   ├── services/*.java
│   ├── controllers/*.java
│   ├── dtos/*.java
│   └── conversion-notes.md
└── 40-testing/
    ├── unit-tests/*.java
    ├── integration-tests/*.java
    └── testing-notes.md
```

### Project-Wide Status

Track overall progress in:
- `documentation/progress-tracking/conversion-status.md` - Program completion tracker
- `documentation/progress-tracking/issues-log.md` - Issues and blockers log

## LLM Compatibility

### Recommended LLMs

**Tier 1 (Best for single-session):**
- Claude Opus 4.5 / Sonnet 4.5 (200K+ context)
- GPT-4 Turbo (128K context)
- Gemini 1.5 Pro (1M context)

**Tier 2 (Good for phase-by-phase):**
- Claude Haiku
- GPT-4 (8K context)
- Mistral Large

**Tier 3 (Local models):**
- Qwen 2.5 72B
- DeepSeek Coder 33B
- CodeLlama 70B

### Platform Examples

**Claude Code CLI:**
```bash
claude
# Paste prompt and execute
```

**ChatGPT Web:**
```bash
cat work-in-progress/PROGRAM/MASTER-PIPELINE-PROMPT.md | pbcopy
# Paste into ChatGPT interface
```

**Local LLM (Ollama):**
```bash
ollama run qwen2.5-coder:72b "$(cat work-in-progress/PROGRAM/MASTER-PIPELINE-PROMPT.md)"
```

## Best Practices

1. **Test First**: Always run test-programs/CUST001 before production conversions
2. **Review Outputs**: Don't blindly accept LLM-generated code; verify business logic
3. **Build Patterns**: Document recurring solutions in common-patterns/
4. **Track Issues**: Use the issues log for blockers and recurring problems
5. **Version Control**: Commit after each phase for easy rollback
6. **Update Documentation**: Keep conversion-status.md current

## Success Criteria

A successful conversion should produce:
- ✅ All 7 phases completed
- ✅ All expected files generated
- ✅ Code compiles successfully
- ✅ Tests pass (target: 90%+ coverage)
- ✅ Business logic matches RPGLE behavior
- ✅ No critical issues in review report

## Troubleshooting

**Problem:** LLM runs out of context
- **Solution:** Use phase-by-phase mode instead of master prompt

**Problem:** LLM doesn't understand RPGLE syntax
- **Solution:** Enhance analysis phase; add RPGLE patterns to common-patterns/

**Problem:** Generated code has compilation errors
- **Solution:** Phase 6 (review) will identify issues; iterate on fixes

**Problem:** Tests fail
- **Solution:** Review test logic against RPGLE behavior; update test data

**Problem:** Missing business logic in conversion
- **Solution:** Improve analysis phase documentation; add detailed notes

## Project Configuration

Global settings are defined in `project-settings.md`:

```
PACKAGE_NAME=com.lanarimarco.modernization
```

Variables can be referenced in prompts using `{VARIABLE_NAME}` syntax.

## Additional Documentation

- **WORKFLOW-GUIDE.md**: Detailed workflow execution guide
- **test-programs/README.md**: Testing and validation instructions
- **documentation/project-overview.md**: High-level project summary
- **documentation/architecture-decisions.md**: Architectural choices and rationale

---

**Created**: 2025-12-15
**Updated**: 2025-12-28
**Purpose**: AI-assisted RPGLE to Java modernization framework
**Target**: 50 RPGLE programs
**Approach**: Multi-phase agent-based conversion
**LLM Support**: Agnostic (works with any LLM)
