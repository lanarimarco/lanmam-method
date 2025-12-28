# RPGLE to Java Conversion Workflow Guide

## Overview

This project uses an LLM-agnostic pipeline to convert RPGLE programs to modern Java/Spring Boot applications. The pipeline works with **any LLM tool** - no vendor lock-in.

## Key Features

✅ **LLM-Agnostic**: Works with Claude Code, ChatGPT, Copilot, or local models
✅ **Automated Setup**: Scripts create workspaces and generate prompts automatically
✅ **Flexible Execution**: Run as single session or phase-by-phase
✅ **Comprehensive**: Covers analysis, database, conversion, UI, testing, review, and integration
✅ **Documented**: All prompts are standalone, portable markdown files

## Quick Start

> **Note**: Global project settings (like package names) are defined in `project-settings.md`. Variables can be referenced in prompts using `{VARIABLE_NAME}` syntax.

### 1. Add Your RPGLE Source Files

```bash
# Place files in appropriate directories
cp your-program.rpgle source-rpgle/programs/
cp your-file.dds source-rpgle/dds/physical-files/
cp your-screen.dds source-rpgle/dds/display-files/
```

### 2. Run Pipeline Setup

```bash
cd scripts
./run-full-pipeline.sh YOUR_PROGRAM_NAME
```

This creates:
- Workspace directories for all 7 phases
- Individual prompt files for each phase
- One master prompt for the complete pipeline
- Template files and references

### 3. Choose Your Workflow

#### Option A: Single LLM Session (Recommended)

Best for: Powerful LLMs (Claude Opus/Sonnet, GPT-4, etc.) with large context windows

```bash
# Copy the master prompt
cat work-in-progress/YOUR_PROGRAM/MASTER-PIPELINE-PROMPT.md | pbcopy

# Paste into your LLM and let it work through all 7 phases
```

**Pros:**
- Complete conversion in one session
- Better context retention between phases
- Faster overall execution

**Cons:**
- Requires large context window (~40KB+ prompt)
- May be expensive for API-based LLMs

#### Option B: Phase-by-Phase Sessions

Best for: Managing costs, iterative work, or context-limited LLMs

```bash
# Phase 1: Analysis
cat work-in-progress/YOUR_PROGRAM/01-analysis/PROMPT.md | pbcopy
# → Paste into LLM, wait for completion

# Phase 2: Database
cat work-in-progress/YOUR_PROGRAM/02-database/PROMPT.md | pbcopy
# → Paste into LLM, wait for completion

# Repeat for phases 3-7...
```

**Pros:**
- Better control over each phase
- Can review outputs before proceeding
- Works with smaller context windows
- More cost-effective

**Cons:**
- More manual steps
- Need to track progress across sessions

## The 7 Phases

### Phase 1: Analysis
- Reads RPGLE source code
- Documents business logic and data flows
- Identifies dependencies
- Creates analysis document

**Output**: `01-analysis/YOUR_PROGRAM-analysis.md`

### Phase 2: Database Layer
- Converts DDS to JPA entities
- Creates Spring Data repositories
- Maps DB2 types to Java types

**Output**:
- `02-database/entities/*.java`
- `02-database/repositories/*.java`

### Phase 3: Business Logic Conversion
- Converts RPGLE to Java services
- Creates REST controllers
- Defines DTOs
- Documents conversion decisions

**Output**:
- `03-conversion/services/*.java`
- `03-conversion/controllers/*.java`
- `03-conversion/dtos/*.java`
- `03-conversion/exceptions/*.java` (if needed)
- `03-conversion/conversion-notes.md`
- `03-conversion/README.md`

### Phase 4: UI Layer
- Converts 5250 screens to React components
- Creates responsive web interface
- Implements accessibility features
- Builds API service layer
- Creates program-specific components (not full React app)

**Output**:
- `04-ui/src/pages/{ProgramName}/*.tsx` - Page components
- `04-ui/src/services/api/*.ts` - API service layer
- `04-ui/src/types/*.ts` - TypeScript type definitions
- `04-ui/src/hooks/*.ts` - Custom hooks (if needed)
- `04-ui/src/utils/*.ts` - Utilities (if needed)
- `04-ui/ui-notes.md` - UI/UX decisions and mappings
- `04-ui/README.md` - Component documentation
- `04-ui/integration-notes.md` - Integration instructions
- `04-ui/dependencies-to-add.txt` - Additional npm packages

### Phase 5: Testing
- Creates unit tests for services
- Creates integration tests for controllers
- Generates test data
- Targets >80% code coverage

**Output**:
- `05-testing/unit-tests/*.java` - Service unit tests
- `05-testing/integration-tests/*.java` - Controller integration tests
- `05-testing/repository-tests/*.java` - Repository tests (if needed)
- `05-testing/test-data/*.sql` - Test data scripts
- `05-testing/testing-notes.md` - Test coverage documentation
- `05-testing/README.md` - Test setup and usage guide

### Phase 6: Code Review
- Reviews all generated code
- Refactors as needed
- Ensures quality and best practices
- Documents all changes

**Output**:
- `06-review/review-report.md` - Comprehensive review findings
- `06-review/refactoring-log.md` - All refactoring performed
- `06-review/src/` - Refactored code (if improvements made)

### Phase 7: Integration & Deployment
- Consolidates all code to final-output structure
- Creates/updates Spring Boot backend application
- Creates/updates React frontend application
- Verifies Maven build and tests
- Verifies npm build
- Creates deployment documentation
- Prepares for production deployment

**Output**:

**Backend** (`/final-output/backend/`):
- `src/main/java/...` - Consolidated Java code (entities, repositories, services, controllers, DTOs)
- `src/test/java/...` - Consolidated test suite
- `src/main/resources/application*.yml` - Configuration files
- `pom.xml` - Maven configuration
- `ModernizationApplication.java` - Spring Boot main class (created on first program)
- `config/WebConfig.java` - CORS configuration (created on first program)

**Frontend** (`/final-output/frontend/`):
- `src/pages/{ProgramName}/` - Program page components
- `src/services/api/` - API service layer
- `src/types/` - TypeScript types
- `src/App.tsx` - React app shell with routing (created on first program)
- `src/index.tsx` - Entry point (created on first program)
- `public/index.html` - HTML shell (created on first program)
- `package.json` - npm configuration
- `tsconfig.json` - TypeScript configuration
- `.env.development` / `.env.production` - Environment configs

**Documentation** (`/final-output/docs/{PROGRAM}/`):
- `integration-report.md` - Integration summary and verification
- `deployment-guide.md` - Deployment instructions
- `analysis.md` - From Phase 1
- `database-notes.md` - From Phase 2
- `conversion-notes.md` - From Phase 3
- `ui-notes.md` - From Phase 4
- `testing-notes.md` - From Phase 5
- `review-report.md` - From Phase 6

## LLM Compatibility

### Recommended LLMs

**Tier 1 (Best):**
- Claude Opus 4.5 / Sonnet 4.5 (200K+ context)
- GPT-4 Turbo (128K context)
- Gemini 1.5 Pro (1M context)

**Tier 2 (Good):**
- Claude Haiku (use phase-by-phase)
- GPT-4 (8K context - must use phase-by-phase)
- Mistral Large

**Tier 3 (Local Models):**
- Qwen 2.5 72B
- DeepSeek Coder 33B
- CodeLlama 70B

### Using with Different LLM Tools

#### Claude Code (CLI)
```bash
# Start session in project root
claude

# Paste the prompt and go
```

#### ChatGPT (Web)
```bash
# Copy prompt
cat work-in-progress/YOUR_PROGRAM/MASTER-PIPELINE-PROMPT.md | pbcopy

# Paste into ChatGPT web interface
```

#### Local LLM (Ollama)
```bash
# Copy prompt to a file or use directly
ollama run qwen2.5-coder:72b "$(cat work-in-progress/YOUR_PROGRAM/MASTER-PIPELINE-PROMPT.md)"
```

#### API Integration
```python
# Example with OpenAI API
import openai

with open("work-in-progress/YOUR_PROGRAM/MASTER-PIPELINE-PROMPT.md") as f:
    prompt = f.read()

response = openai.ChatCompletion.create(
    model="gpt-4-turbo",
    messages=[{"role": "user", "content": prompt}]
)
```

## Individual Phase Scripts

You can also set up individual phases:

```bash
cd scripts

# Just analysis
./run-analysis.sh YOUR_PROGRAM

# Just database
./run-database.sh YOUR_PROGRAM

# Just conversion
./run-conversion.sh YOUR_PROGRAM

# Just UI
./run-ui.sh YOUR_PROGRAM

# Just testing
./run-testing.sh YOUR_PROGRAM

# Just review
./run-review.sh YOUR_PROGRAM

# Just integration
./run-integration.sh YOUR_PROGRAM
```

Each generates a `PROMPT.md` file in its phase directory.

## Tracking Progress

### For a Single Program

Check outputs in:
```
work-in-progress/YOUR_PROGRAM/
├── 01-analysis/YOUR_PROGRAM-analysis.md
├── 02-database/
│   ├── entities/*.java
│   ├── repositories/*.java
│   └── database-notes.md
├── 03-conversion/
│   ├── services/*.java
│   ├── controllers/*.java
│   ├── dtos/*.java
│   ├── conversion-notes.md
│   └── README.md
├── 04-ui/
│   ├── src/pages/{ProgramName}/*.tsx
│   ├── src/services/api/*.ts
│   ├── src/types/*.ts
│   ├── ui-notes.md
│   ├── README.md
│   └── integration-notes.md
├── 05-testing/
│   ├── unit-tests/*.java
│   ├── integration-tests/*.java
│   ├── testing-notes.md
│   └── README.md
├── 06-review/
│   ├── review-report.md
│   └── refactoring-log.md
└── 07-integration/PROMPT.md

final-output/
├── backend/
│   ├── src/main/java/...
│   ├── src/test/java/...
│   └── pom.xml
├── frontend/
│   ├── src/
│   └── package.json
└── docs/YOUR_PROGRAM/
    ├── integration-report.md
    ├── deployment-guide.md
    └── (all phase notes consolidated here)
```

### Across All Programs

Update these files:
- `documentation/progress-tracking/conversion-status.md`
- `documentation/progress-tracking/issues-log.md`

## Tips for Success

### 1. Start with a Simple Program
- Choose a small, well-understood program for your first conversion
- Validate the approach before scaling to larger programs

### 2. Review Each Phase
- Don't blindly accept LLM outputs
- Verify business logic matches RPGLE
- Check that tests actually pass

### 3. Build the Common Patterns Library
- Document solutions to recurring problems
- Update `common-patterns/lessons-learned.md`
- Add reusable utility code to `common-patterns/utilities/`
  - Java utilities (DateUtils, NumericUtils, etc.)
  - TypeScript utilities (dateFormatters, apiHelpers, etc.)
  - SQL query patterns
- Share patterns across the team

### 4. Leverage the Notes Files
- Each phase creates a `*-notes.md` file
- These explain decisions and document issues
- Review these to understand the conversion

### 5. Use Version Control
- Commit after each successful phase
- Makes it easy to roll back if needed
- Tracks evolution of the conversion

### 6. Test Before Production
- Use `test-programs/test-workflow.sh` to validate the pipeline with sample programs
- Use `test-programs/cleanup-test.sh` to clean up test outputs and start fresh
- Verify all phases work correctly before processing production code

## Troubleshooting

### Problem: LLM runs out of context
**Solution**: Use phase-by-phase approach instead of master prompt

### Problem: LLM doesn't understand RPGLE
**Solution**: Provide more context in analysis phase; update common patterns

### Problem: Generated code has errors
**Solution**: Review phase outputs, use Phase 6 (review) to identify and fix issues

### Problem: Tests don't pass
**Solution**: Phase 6 review will catch this; iterate on failing tests

### Problem: Missing business logic
**Solution**: Improve analysis phase; add missing rules to conversion notes

## Next Steps

1. ✅ **Test the workflow** - Run `cd test-programs && ./test-workflow.sh` to test with CUST001 sample program
2. ✅ **Validate the approach** - Review test outputs and validate the conversion quality
3. ✅ **Run pipeline on first production program** - `./scripts/run-full-pipeline.sh YOUR_PROGRAM`
4. ✅ **Choose workflow mode** - Single-session or phase-by-phase
5. ✅ **Execute phases 1-6** with your preferred LLM
6. ✅ **Run integration** - `./scripts/run-integration.sh YOUR_PROGRAM` to consolidate to final-output
7. ✅ **Verify builds** - Maven build (backend) and npm build (frontend) in final-output/
8. ✅ **Review and test** - Check deployment guide and prepare for UAT
9. ✅ **Document lessons** - Update common-patterns/lessons-learned.md
10. ✅ **Repeat** for remaining programs

---

**Remember**: The scripts do the setup work. The LLM does the conversion work. You review and integrate the results.

**Questions?** Check the agent README files in `agents/0X-*-agent/README.md`
