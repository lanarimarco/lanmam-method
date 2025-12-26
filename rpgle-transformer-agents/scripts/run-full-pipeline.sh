#!/bin/bash
# Run complete conversion pipeline for a program
# This script orchestrates all 7 agents and creates individual + master prompts

PROGRAM=$1
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

if [ -z "$PROGRAM" ]; then
    echo "Usage: ./run-full-pipeline.sh PROGRAM_NAME"
    echo ""
    echo "This script will:"
    echo "  1. Set up workspaces for all 7 phases"
    echo "  2. Generate individual prompts for each phase"
    echo "  3. Generate a master prompt for the full pipeline"
    echo ""
    exit 1
fi

echo "========================================="
echo "Full Pipeline Setup for: $PROGRAM"
echo "========================================="
echo ""
echo "This will create workspaces and prompts for all 7 phases:"
echo "  Phase 1: Analysis"
echo "  Phase 2: Database Layer"
echo "  Phase 3: Business Logic Conversion"
echo "  Phase 4: UI Creation"
echo "  Phase 5: Testing"
echo "  Phase 6: Code Review"
echo "  Phase 7: Integration & Deployment"
echo ""

# Run each phase setup script
echo "Phase 1: Setting up Analysis..."
./run-analysis.sh $PROGRAM

echo ""
echo "Phase 2: Setting up Database layer..."
./run-database.sh $PROGRAM

echo ""
echo "Phase 3: Setting up Conversion..."
./run-conversion.sh $PROGRAM

echo ""
echo "Phase 4: Setting up UI..."
./run-ui.sh $PROGRAM

echo ""
echo "Phase 5: Setting up Testing..."
./run-testing.sh $PROGRAM

echo ""
echo "Phase 6: Setting up Review..."
./run-review.sh $PROGRAM

echo ""
echo "Phase 7: Setting up Integration..."
./run-integration.sh $PROGRAM

# Create master pipeline prompt
echo ""
echo "========================================="
echo "Creating Master Pipeline Prompt..."
echo "========================================="

PIPELINE_DIR="$PROJECT_ROOT/work-in-progress/$PROGRAM"
cat > "$PIPELINE_DIR/MASTER-PIPELINE-PROMPT.md" <<'MASTER_EOF'
# Complete Conversion Pipeline for RPGLE Program: PROGRAM_PLACEHOLDER

This is a master prompt for converting the entire RPGLE program through all 7 phases.

You can either:
1. **Use this master prompt** for a single long LLM session (recommended for powerful models)
2. **Use individual phase prompts** for separate sessions (better for context limits)

---

## Pipeline Overview

```
Phase 1: Analysis          → Understand RPGLE program
Phase 2: Database Layer    → Create JPA entities and repositories
Phase 3: Conversion        → Convert to Java services and controllers
Phase 4: UI Layer          → Create React components
Phase 5: Testing           → Create comprehensive tests
Phase 6: Review            → Review and refactor code
Phase 7: Integration       → Consolidate to final-output and prepare deployment
```

---

# PHASE 1: ANALYSIS

<PHASE_1_CONTENT>

**Checkpoint**: After completing Phase 1, you should have:
- ✓ Complete analysis document
- ✓ Business purpose understood
- ✓ All dependencies mapped
- ✓ Data flow documented

**Output**: `01-analysis/PROGRAM_PLACEHOLDER-analysis.md`

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
- `02-database/entities/*.java`
- `02-database/repositories/*.java`
- `02-database/database-notes.md`

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
- `03-conversion/services/*.java`
- `03-conversion/controllers/*.java`
- `03-conversion/dtos/*.java`
- `03-conversion/conversion-notes.md`

---

# PHASE 4: UI LAYER

**Prerequisites**: Phases 1-3 complete

<PHASE_4_CONTENT>

**Checkpoint**: After completing Phase 4, you should have:
- ✓ React components created
- ✓ TypeScript types defined
- ✓ Responsive styles implemented
- ✓ UI notes documenting UX decisions

**Outputs**:
- `04-ui/components/*.tsx`
- `04-ui/types/*.ts`
- `04-ui/styles/*`
- `04-ui/ui-notes.md`

---

# PHASE 5: TESTING

**Prerequisites**: Phases 1-4 complete

<PHASE_5_CONTENT>

**Checkpoint**: After completing Phase 5, you should have:
- ✓ Unit tests for services
- ✓ Integration tests for controllers
- ✓ Test data and fixtures
- ✓ >80% code coverage for business logic

**Outputs**:
- `05-testing/unit-tests/*.java`
- `05-testing/integration-tests/*.java`
- `05-testing/test-data/*`
- `05-testing/testing-notes.md`

---

# PHASE 6: CODE REVIEW AND REFACTORING

**Prerequisites**: All phases 1-5 complete

<PHASE_6_CONTENT>

**Checkpoint**: After completing Phase 6, you should have:
- ✓ Comprehensive review report
- ✓ All critical issues addressed
- ✓ Code refactored if needed
- ✓ Final checklist signed off

**Outputs**:
- `06-review/review-report.md`
- `06-review/final-checklist.md`
- `06-review/refactored-code/` (if needed)

---

# FINAL CHECKLIST

After completing all 7 phases, verify:

- [ ] Phase 1: Analysis complete and thorough
- [ ] Phase 2: All database entities and repositories created
- [ ] Phase 3: All business logic converted to Java
- [ ] Phase 4: UI components functional and accessible
- [ ] Phase 5: Tests comprehensive and passing
- [ ] Phase 6: Code reviewed and quality verified
- [ ] Phase 7: Code integrated to final-output and deployment ready

- [ ] All documentation files created
- [ ] All notes files explain decisions
- [ ] No critical issues remaining
- [ ] Code follows best practices
- [ ] Maven build succeeds in final-output
- [ ] All tests pass in final-output
- [ ] Deployment guide complete
- [ ] Ready for UAT/production deployment

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
- Individual prompts: `PROJECT_ROOT_PLACEHOLDER/work-in-progress/PROGRAM_PLACEHOLDER/0X-<phase>/PROMPT.md`
- Templates and guides: `PROJECT_ROOT_PLACEHOLDER/agents/0X-<phase>-agent/`
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

This is a master prompt for converting the entire RPGLE program through all 7 phases.

You can either:
1. **Use this master prompt** for a single long LLM session (recommended for powerful models)
2. **Use individual phase prompts** for separate sessions (better for context limits)

---

## Pipeline Overview

\`\`\`
Phase 1: Analysis          → Understand RPGLE program
Phase 2: Database Layer    → Create JPA entities and repositories
Phase 3: Conversion        → Convert to Java services and controllers
Phase 4: UI Layer          → Create React components
Phase 5: Testing           → Create comprehensive tests
Phase 6: Review            → Review and refactor code
Phase 7: Integration       → Consolidate to final-output and prepare deployment
\`\`\`

---

HEADER

    # Phase 1
    echo "# PHASE 1: ANALYSIS"
    echo ""
    tail -n +3 "$PIPELINE_DIR/01-analysis/PROMPT.md"
    echo ""
    cat <<CHECKPOINT1
**Checkpoint**: After completing Phase 1, you should have:
- ✓ Complete analysis document
- ✓ Business purpose understood
- ✓ All dependencies mapped
- ✓ Data flow documented

**Output**: \`01-analysis/$PROGRAM-analysis.md\`

---

CHECKPOINT1

    # Phase 2
    echo "# PHASE 2: DATABASE LAYER"
    echo ""
    echo "**Prerequisites**: Phase 1 complete"
    echo ""
    tail -n +3 "$PIPELINE_DIR/02-database/PROMPT.md"
    echo ""
    cat <<CHECKPOINT2
**Checkpoint**: After completing Phase 2, you should have:
- ✓ JPA entity classes created
- ✓ Repository interfaces created
- ✓ Type mappings documented
- ✓ Database notes complete

**Outputs**:
- \`02-database/entities/*.java\`
- \`02-database/repositories/*.java\`
- \`02-database/database-notes.md\`

---

CHECKPOINT2

    # Phase 3
    echo "# PHASE 3: BUSINESS LOGIC CONVERSION"
    echo ""
    echo "**Prerequisites**: Phases 1-2 complete"
    echo ""
    tail -n +3 "$PIPELINE_DIR/03-conversion/PROMPT.md"
    echo ""
    cat <<CHECKPOINT3
**Checkpoint**: After completing Phase 3, you should have:
- ✓ Service classes implementing business logic
- ✓ REST controllers exposing APIs
- ✓ DTOs for data transfer
- ✓ Conversion notes documenting decisions

**Outputs**:
- \`03-conversion/services/*.java\`
- \`03-conversion/controllers/*.java\`
- \`03-conversion/dtos/*.java\`
- \`03-conversion/conversion-notes.md\`

---

CHECKPOINT3

    # Phase 4
    echo "# PHASE 4: UI LAYER"
    echo ""
    echo "**Prerequisites**: Phases 1-3 complete"
    echo ""
    tail -n +3 "$PIPELINE_DIR/04-ui/PROMPT.md"
    echo ""
    cat <<CHECKPOINT4
**Checkpoint**: After completing Phase 4, you should have:
- ✓ React components created
- ✓ TypeScript types defined
- ✓ Responsive styles implemented
- ✓ UI notes documenting UX decisions

**Outputs**:
- \`04-ui/components/*.tsx\`
- \`04-ui/types/*.ts\`
- \`04-ui/styles/*\`
- \`04-ui/ui-notes.md\`

---

CHECKPOINT4

    # Phase 5
    echo "# PHASE 5: TESTING"
    echo ""
    echo "**Prerequisites**: Phases 1-4 complete"
    echo ""
    tail -n +3 "$PIPELINE_DIR/05-testing/PROMPT.md"
    echo ""
    cat <<CHECKPOINT5
**Checkpoint**: After completing Phase 5, you should have:
- ✓ Unit tests for services
- ✓ Integration tests for controllers
- ✓ Test data and fixtures
- ✓ >80% code coverage for business logic

**Outputs**:
- \`05-testing/unit-tests/*.java\`
- \`05-testing/integration-tests/*.java\`
- \`05-testing/test-data/*\`
- \`05-testing/testing-notes.md\`

---

CHECKPOINT5

    # Phase 6
    echo "# PHASE 6: CODE REVIEW AND REFACTORING"
    echo ""
    echo "**Prerequisites**: All phases 1-5 complete"
    echo ""
    tail -n +3 "$PIPELINE_DIR/06-review/PROMPT.md"
    echo ""
    cat <<CHECKPOINT6
**Checkpoint**: After completing Phase 6, you should have:
- ✓ Comprehensive review report
- ✓ All critical issues addressed
- ✓ Code refactored if needed
- ✓ Final checklist signed off

**Outputs**:
- \`06-review/review-report.md\`
- \`06-review/final-checklist.md\`
- \`06-review/refactored-code/\` (if needed)

---

CHECKPOINT6

    # Phase 7
    echo "# PHASE 7: INTEGRATION & DEPLOYMENT PREPARATION"
    echo ""
    echo "**Prerequisites**: All phases 1-6 complete, Phase 6 review passed"
    echo ""
    tail -n +3 "$PIPELINE_DIR/07-integration/PROMPT.md"
    echo ""
    cat <<CHECKPOINT7
**Checkpoint**: After completing Phase 7, you should have:
- ✓ All code consolidated to /final-output
- ✓ Maven build compiles successfully
- ✓ All tests passing
- ✓ Documentation package complete
- ✓ Deployment guide created
- ✓ Integration report complete

**Outputs**:
- \`/final-output/src/main/java/...\` (consolidated code)
- \`/final-output/src/test/java/...\` (consolidated tests)
- \`/final-output/docs/$PROGRAM/integration-report.md\`
- \`/final-output/docs/$PROGRAM/deployment-guide.md\`
- \`/final-output/docs/$PROGRAM/\` (all documentation)

---

CHECKPOINT7

    # Footer
    cat <<FOOTER

# FINAL CHECKLIST

After completing all 7 phases, verify:

- [ ] Phase 1: Analysis complete and thorough
- [ ] Phase 2: All database entities and repositories created
- [ ] Phase 3: All business logic converted to Java
- [ ] Phase 4: UI components functional and accessible
- [ ] Phase 5: Tests comprehensive and passing
- [ ] Phase 6: Code reviewed and quality verified
- [ ] Phase 7: Code integrated to final-output and deployment ready

- [ ] All documentation files created
- [ ] All notes files explain decisions
- [ ] No critical issues remaining
- [ ] Code follows best practices
- [ ] Maven build succeeds in final-output
- [ ] All tests pass in final-output
- [ ] Deployment guide complete
- [ ] Ready for UAT/production deployment

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
- Individual prompts: \`$PROJECT_ROOT/work-in-progress/$PROGRAM/0X-<phase>/PROMPT.md\`
- Templates and guides: \`$PROJECT_ROOT/agents/0X-<phase>-agent/\`
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
echo "  - Phase 1: work-in-progress/$PROGRAM/01-analysis/PROMPT.md"
echo "  - Phase 2: work-in-progress/$PROGRAM/02-database/PROMPT.md"
echo "  - Phase 3: work-in-progress/$PROGRAM/03-conversion/PROMPT.md"
echo "  - Phase 4: work-in-progress/$PROGRAM/04-ui/PROMPT.md"
echo "  - Phase 5: work-in-progress/$PROGRAM/05-testing/PROMPT.md"
echo "  - Phase 6: work-in-progress/$PROGRAM/06-review/PROMPT.md"
echo "  - Phase 7: work-in-progress/$PROGRAM/07-integration/PROMPT.md"
echo ""
echo "📋 To copy a specific phase prompt:"
echo "  cat \"$PIPELINE_DIR/01-analysis/PROMPT.md\" | pbcopy"
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
