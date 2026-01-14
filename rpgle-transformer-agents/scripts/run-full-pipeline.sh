#!/bin/bash
# Run complete conversion pipeline for a program
# This script orchestrates all 4 agents and creates individual + master prompts

PROGRAM=$1
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

if [ -z "$PROGRAM" ]; then
    echo "Usage: ./run-full-pipeline.sh PROGRAM_NAME"
    echo ""
    echo "This script will:"
    echo "  1. Set up workspaces for all 4 phases"
    echo "  2. Generate individual prompts for each phase"
    echo "  3. Generate a master prompt for the full pipeline"
    echo ""
    exit 1
fi

echo "========================================="
echo "Full Pipeline Setup for: $PROGRAM"
echo "========================================="
echo ""
echo "This will create workspaces and prompts for all 4 phases:"
echo "  Phase 1: Analysis"
echo "  Phase 2: Database Layer"
echo "  Phase 3: Business Logic Conversion"
echo "  Phase 4: Testing"
echo ""

# Run each phase setup script with error checking
echo "Phase 1: Setting up Analysis..."
./run-analysis.sh $PROGRAM
if [ $? -ne 0 ]; then
    echo "❌ Error in Phase 1: Analysis. Aborting pipeline."
    exit 1
fi

echo ""
echo "Phase 2: Setting up Database layer..."
./run-database.sh $PROGRAM
if [ $? -ne 0 ]; then
    echo "❌ Error in Phase 2: Database layer. Aborting pipeline."
    exit 1
fi

echo ""
echo "Phase 3: Setting up Conversion..."
./run-conversion.sh $PROGRAM
if [ $? -ne 0 ]; then
    echo "❌ Error in Phase 3: Conversion. Aborting pipeline."
    exit 1
fi

echo ""
echo "Phase 4: Setting up Testing..."
./run-testing.sh $PROGRAM
if [ $? -ne 0 ]; then
    echo "❌ Error in Phase 4: Testing. Aborting pipeline."
    exit 1
fi

# Create master pipeline prompt
echo ""
echo "========================================="
echo "Creating Master Pipeline Prompt..."
echo "========================================="

PIPELINE_DIR="$PROJECT_ROOT/work-in-progress/$PROGRAM"
cat > "$PIPELINE_DIR/MASTER-PIPELINE-PROMPT.md" <<'MASTER_EOF'
# Complete Conversion Pipeline for RPGLE Program: PROGRAM_PLACEHOLDER

This is a master prompt for converting the entire RPGLE program through all 4 phases.

You can either:
1. **Use this master prompt** for a single long LLM session (recommended for powerful models)
2. **Use individual phase prompts** for separate sessions (better for context limits)

---

## Pipeline Overview

```
Phase 1: Analysis          → Understand RPGLE program
Phase 2: Database Layer    → Create JPA entities and repositories
Phase 3: Conversion        → Convert to Java services and controllers
Phase 4: Testing           → Create comprehensive tests
```

---

# PHASE 1: ANALYSIS

<PHASE_1_CONTENT>

**Checkpoint**: After completing Phase 1, you should have:
- ✓ Complete analysis document
- ✓ Business purpose understood
- ✓ All dependencies mapped
- ✓ Data flow documented

**Output**: `10-analysis/PROGRAM_PLACEHOLDER-analysis.md`

---

# PHASE 2: DATABASE LAYER

**Prerequisites**: Phase 1 complete

<PHASE_2_CONTENT>

**Checkpoint**: After completing Phase 2, you should have:
- ✓ JPA entity classes created
- ✓ Repository interfaces created
- ✓ Type mappings documented
- ✓ Database notes complete

**Outputs**:
- `20-database/entities/*.java`
- `20-database/repositories/*.java`
- `20-database/database-notes.md`

---

# PHASE 3: BUSINESS LOGIC CONVERSION

**Prerequisites**: Phases 1-2 complete

<PHASE_3_CONTENT>

**Checkpoint**: After completing Phase 3, you should have:
- ✓ Service classes implementing business logic
- ✓ REST controllers exposing APIs
- ✓ DTOs for data transfer
- ✓ Conversion notes documenting decisions

**Outputs**:
- `30-conversion/services/*.java`
- `30-conversion/controllers/*.java`
- `30-conversion/dtos/*.java`
- `30-conversion/conversion-notes.md`

---

# PHASE 4: TESTING

**Prerequisites**: Phases 1-3 complete

<PHASE_4_CONTENT>

**Checkpoint**: After completing Phase 4, you should have:
- ✓ Unit tests for services
- ✓ Integration tests for controllers
- ✓ Test data and fixtures
- ✓ >80% code coverage for business logic

**Outputs**:
- `40-testing/unit-tests/*.java`
- `40-testing/integration-tests/*.java`
- `40-testing/test-data/*`
- `40-testing/testing-notes.md`

---

# FINAL CHECKLIST

After completing all 4 phases, verify:

- [ ] Phase 1: Analysis complete and thorough
- [ ] Phase 2: All database entities and repositories created
- [ ] Phase 3: All business logic converted to Java
- [ ] Phase 4: Tests comprehensive and passing

- [ ] All documentation files created
- [ ] All notes files explain decisions
- [ ] No critical issues remaining
- [ ] Code follows best practices
- [ ] Maven build succeeds
- [ ] All tests pass

---

## Working with This Prompt

**For a single LLM session**:
- Use this complete prompt
- Work through each phase sequentially
- Check each checkpoint before proceeding

**For multiple LLM sessions**:
- Use individual phase prompts from each phase directory
- Complete one phase at a time
- Review outputs before moving to next phase

**Files to Reference**:
- Individual prompts: `PROJECT_ROOT_PLACEHOLDER/work-in-progress/PROGRAM_PLACEHOLDER/XX-<phase>/PROMPT.md`
- Templates and guides: `PROJECT_ROOT_PLACEHOLDER/agents/XX-<phase>-agent/`
- Common patterns: `PROJECT_ROOT_PLACEHOLDER/common-patterns/`

---

**Project Root**: `PROJECT_ROOT_PLACEHOLDER`
**Program**: `PROGRAM_PLACEHOLDER`
**Output Base**: `PROJECT_ROOT_PLACEHOLDER/work-in-progress/PROGRAM_PLACEHOLDER/`

MASTER_EOF

# Build master prompt by concatenating sections
{
    # Header
    cat <<HEADER
# Complete Conversion Pipeline for RPGLE Program: $PROGRAM

This is a master prompt for converting the entire RPGLE program through all 4 phases.

You can either:
1. **Use this master prompt** for a single long LLM session (recommended for powerful models)
2. **Use individual phase prompts** for separate sessions (better for context limits)

---

## Pipeline Overview

\`\`\`
Phase 1: Analysis          → Understand RPGLE program
Phase 2: Database Layer    → Create JPA entities and repositories
Phase 3: Conversion        → Convert to Java services and controllers
Phase 4: Testing           → Create comprehensive tests
\`\`\`

---

HEADER

    # Phase 1
    echo "# PHASE 1: ANALYSIS"
    echo ""
    tail -n +3 "$PIPELINE_DIR/10-analysis/PROMPT.md"
    echo ""
    cat <<CHECKPOINT1
**Checkpoint**: After completing Phase 1, you should have:
- ✓ Complete analysis document
- ✓ Business purpose understood
- ✓ All dependencies mapped
- ✓ Data flow documented

**Output**: \`10-analysis/$PROGRAM-analysis.md\`

---

CHECKPOINT1

    # Phase 2
    echo "# PHASE 2: DATABASE LAYER"
    echo ""
    echo "**Prerequisites**: Phase 1 complete"
    echo ""
    tail -n +3 "$PIPELINE_DIR/20-database/PROMPT.md"
    echo ""
    cat <<CHECKPOINT2
**Checkpoint**: After completing Phase 2, you should have:
- ✓ JPA entity classes created
- ✓ Repository interfaces created
- ✓ Type mappings documented
- ✓ Database notes complete

**Outputs**:
- \`20-database/entities/*.java\`
- \`20-database/repositories/*.java\`
- \`20-database/database-notes.md\`

---

CHECKPOINT2

    # Phase 3
    echo "# PHASE 3: BUSINESS LOGIC CONVERSION"
    echo ""
    echo "**Prerequisites**: Phases 1-2 complete"
    echo ""
    tail -n +3 "$PIPELINE_DIR/30-conversion/PROMPT.md"
    echo ""
    cat <<CHECKPOINT3
**Checkpoint**: After completing Phase 3, you should have:
- ✓ Service classes implementing business logic
- ✓ REST controllers exposing APIs
- ✓ DTOs for data transfer
- ✓ Conversion notes documenting decisions

**Outputs**:
- \`30-conversion/services/*.java\`
- \`30-conversion/controllers/*.java\`
- \`30-conversion/dtos/*.java\`
- \`30-conversion/conversion-notes.md\`

---

CHECKPOINT3

    # Phase 4
    echo "# PHASE 4: TESTING"
    echo ""
    echo "**Prerequisites**: Phases 1-3 complete"
    echo ""
    tail -n +3 "$PIPELINE_DIR/40-testing/PROMPT.md"
    echo ""
    cat <<CHECKPOINT4
**Checkpoint**: After completing Phase 4, you should have:
- ✓ Unit tests for services
- ✓ Integration tests for controllers
- ✓ Test data and fixtures
- ✓ >80% code coverage for business logic

**Outputs**:
- \`40-testing/unit-tests/*.java\`
- \`40-testing/integration-tests/*.java\`
- \`40-testing/test-data/*\`
- \`40-testing/testing-notes.md\`

---

CHECKPOINT4

    # Footer
    cat <<FOOTER

# FINAL CHECKLIST

After completing all 4 phases, verify:

- [ ] Phase 1: Analysis complete and thorough
- [ ] Phase 2: All database entities and repositories created
- [ ] Phase 3: All business logic converted to Java
- [ ] Phase 4: Tests comprehensive and passing

- [ ] All documentation files created
- [ ] All notes files explain decisions
- [ ] No critical issues remaining
- [ ] Code follows best practices
- [ ] Maven build succeeds
- [ ] All tests pass

---

## Working with This Prompt

**For a single LLM session**:
- Use this complete prompt
- Work through each phase sequentially
- Check each checkpoint before proceeding

**For multiple LLM sessions**:
- Use individual phase prompts from each phase directory
- Complete one phase at a time
- Review outputs before moving to next phase

**Files to Reference**:
- Individual prompts: \`$PROJECT_ROOT/work-in-progress/$PROGRAM/XX-<phase>/PROMPT.md\`
- Templates and guides: \`$PROJECT_ROOT/agents/XX-<phase>-agent/\`
- Common patterns: \`$PROJECT_ROOT/common-patterns/\`

---

**Project Root**: \`$PROJECT_ROOT\`
**Program**: \`$PROGRAM\`
**Output Base**: \`$PROJECT_ROOT/work-in-progress/$PROGRAM/\`
FOOTER

} > "$PIPELINE_DIR/MASTER-PIPELINE-PROMPT.md"

echo "✓ Master prompt created"
echo ""

echo "========================================="
echo "Pipeline Setup Complete!"
echo "========================================="
echo ""
echo "All workspace directories and prompts have been created for $PROGRAM"
echo ""
echo "📁 Workspace: $PIPELINE_DIR"
echo ""
echo "🚀 You have TWO options:"
echo ""
echo "Option 1: Use the MASTER PROMPT (single LLM session)"
echo "  - Best for: Powerful LLMs with large context windows"
echo "  - File: work-in-progress/$PROGRAM/MASTER-PIPELINE-PROMPT.md"
echo "  - Copy to clipboard:"
echo "    cat \"$PIPELINE_DIR/MASTER-PIPELINE-PROMPT.md\" | pbcopy"
echo ""
echo "Option 2: Use INDIVIDUAL PROMPTS (separate sessions)"
echo "  - Best for: Managing context limits, iterative work"
echo "  - Phase 1: work-in-progress/$PROGRAM/10-analysis/PROMPT.md"
echo "  - Phase 2: work-in-progress/$PROGRAM/20-database/PROMPT.md"
echo "  - Phase 3: work-in-progress/$PROGRAM/30-conversion/PROMPT.md"
echo "  - Phase 4: work-in-progress/$PROGRAM/40-testing/PROMPT.md"
echo ""
echo "📋 To copy a specific phase prompt:"
echo "  cat \"$PIPELINE_DIR/10-analysis/PROMPT.md\" | pbcopy"
echo ""
echo "🎯 Compatible with ANY LLM:"
echo "  - Claude Code"
echo "  - ChatGPT"
echo "  - GitHub Copilot"
echo "  - Local models (Ollama, etc.)"
echo "  - Any LLM with API access"
echo ""
echo "Expected final outputs will be in:"
echo "  $PIPELINE_DIR/"
echo ""
