# RPGLE to Java Conversion Workflow Guide

## Overview

This project uses an LLM-agnostic pipeline to convert RPGLE programs to modern Java/Spring Boot applications. The pipeline works with **any LLM tool** - no vendor lock-in.

## Key Features

✅ **LLM-Agnostic**: Works with Claude Code, ChatGPT, Copilot, or local models
✅ **Automated Setup**: Scripts create workspaces and generate prompts automatically
✅ **Flexible Execution**: Run as single session or phase-by-phase
✅ **Comprehensive**: Covers analysis, database, conversion, UI, testing, and review
✅ **Documented**: All prompts are standalone, portable markdown files

## Quick Start

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
- Workspace directories for all 6 phases
- Individual prompt files for each phase
- One master prompt for the complete pipeline
- Template files and references

### 3. Choose Your Workflow

#### Option A: Single LLM Session (Recommended)

Best for: Powerful LLMs (Claude Opus/Sonnet, GPT-4, etc.) with large context windows

```bash
# Copy the master prompt
cat work-in-progress/YOUR_PROGRAM/MASTER-PIPELINE-PROMPT.md | pbcopy

# Paste into your LLM and let it work through all 6 phases
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

# Repeat for phases 3-6...
```

**Pros:**
- Better control over each phase
- Can review outputs before proceeding
- Works with smaller context windows
- More cost-effective

**Cons:**
- More manual steps
- Need to track progress across sessions

## The 6 Phases

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

**Output**:
- `03-conversion/services/*.java`
- `03-conversion/controllers/*.java`
- `03-conversion/dtos/*.java`

### Phase 4: UI Layer
- Converts 5250 screens to React components
- Creates responsive web interface
- Implements accessibility features

**Output**:
- `04-ui/components/*.tsx`
- `04-ui/types/*.ts`

### Phase 5: Testing
- Creates unit tests for services
- Creates integration tests for controllers
- Generates test data

**Output**:
- `05-testing/unit-tests/*.java`
- `05-testing/integration-tests/*.java`

### Phase 6: Code Review
- Reviews all generated code
- Refactors as needed
- Creates final assessment

**Output**:
- `06-review/review-report.md`
- `06-review/final-checklist.md`

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
```

Each generates a `PROMPT.md` file in its phase directory.

## Tracking Progress

### For a Single Program

Check outputs in:
```
work-in-progress/YOUR_PROGRAM/
├── 01-analysis/YOUR_PROGRAM-analysis.md
├── 02-database/database-notes.md
├── 03-conversion/conversion-notes.md
├── 04-ui/ui-notes.md
├── 05-testing/testing-notes.md
└── 06-review/review-report.md
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
- Share patterns across the team

### 4. Leverage the Notes Files
- Each phase creates a `*-notes.md` file
- These explain decisions and document issues
- Review these to understand the conversion

### 5. Use Version Control
- Commit after each successful phase
- Makes it easy to roll back if needed
- Tracks evolution of the conversion

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

1. ✅ Run `./run-full-pipeline.sh` on your first program
2. ✅ Choose single-session or phase-by-phase workflow
3. ✅ Execute with your preferred LLM
4. ✅ Review outputs and integrate into Spring Boot app
5. ✅ Document lessons learned
6. ✅ Repeat for remaining programs

---

**Remember**: The scripts do the setup work. The LLM does the conversion work. You review and integrate the results.

**Questions?** Check the agent README files in `agents/0X-*-agent/README.md`
