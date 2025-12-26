#!/bin/bash
# Run integration agent on a specific program
# This script prepares the workspace and generates an LLM-agnostic prompt

PROGRAM=$1
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

if [ -z "$PROGRAM" ]; then
    echo "Usage: ./run-integration.sh PROGRAM_NAME"
    exit 1
fi

echo "========================================="
echo "Phase 7: Integration Agent Setup"
echo "Program: $PROGRAM"
echo "========================================="
echo ""

# Verify prerequisites - Phase 6 must be complete
REVIEW_REPORT="$PROJECT_ROOT/work-in-progress/$PROGRAM/06-review/review-report.md"
if [ ! -f "$REVIEW_REPORT" ]; then
    echo "❌ Error: Review report not found: $REVIEW_REPORT"
    echo "  Please complete Phase 6 (Review) first"
    exit 1
fi

# Check that all previous phases exist
ANALYSIS_FILE="$PROJECT_ROOT/work-in-progress/$PROGRAM/01-analysis/$PROGRAM-analysis.md"
if [ ! -f "$ANALYSIS_FILE" ]; then
    echo "❌ Error: Analysis document not found: $ANALYSIS_FILE"
    echo "  Please complete Phase 1 (Analysis) first"
    exit 1
fi

DATABASE_DIR="$PROJECT_ROOT/work-in-progress/$PROGRAM/02-database"
if [ ! -d "$DATABASE_DIR" ]; then
    echo "❌ Error: Database layer not found: $DATABASE_DIR"
    echo "  Please complete Phase 2 (Database) first"
    exit 1
fi

CONVERSION_DIR="$PROJECT_ROOT/work-in-progress/$PROGRAM/03-conversion"
if [ ! -d "$CONVERSION_DIR" ]; then
    echo "❌ Error: Conversion layer not found: $CONVERSION_DIR"
    echo "  Please complete Phase 3 (Conversion) first"
    exit 1
fi

UI_DIR="$PROJECT_ROOT/work-in-progress/$PROGRAM/04-ui"
if [ ! -d "$UI_DIR" ]; then
    echo "❌ Error: UI layer not found: $UI_DIR"
    echo "  Please complete Phase 4 (UI) first"
    exit 1
fi

TESTING_DIR="$PROJECT_ROOT/work-in-progress/$PROGRAM/05-testing"
if [ ! -d "$TESTING_DIR" ]; then
    echo "❌ Error: Testing layer not found: $TESTING_DIR"
    echo "  Please complete Phase 5 (Testing) first"
    exit 1
fi

# Verify final-output structure exists
FINAL_OUTPUT_DIR="$PROJECT_ROOT/final-output"
if [ ! -d "$FINAL_OUTPUT_DIR" ]; then
    echo "❌ Error: Final output directory not found: $FINAL_OUTPUT_DIR"
    echo "  This should exist in the project structure"
    exit 1
fi

# Check review report for success indicators
if grep -q "Status: Failed" "$REVIEW_REPORT" 2>/dev/null; then
    echo "⚠️  Warning: Review report shows failed status"
    echo "  Review may have issues. Proceed with caution."
    echo ""
    read -p "Continue anyway? (y/N) " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
fi

# Create work directory (clean if exists)
WORK_DIR="$PROJECT_ROOT/work-in-progress/$PROGRAM/07-integration"
if [ -d "$WORK_DIR" ]; then
    echo "✓ Cleaning existing workspace: $WORK_DIR"
    rm -rf "$WORK_DIR"
fi
mkdir -p "$WORK_DIR"

# Copy templates and checklists
echo "✓ Creating workspace: $WORK_DIR"
cp -r "$PROJECT_ROOT/agents/07-integration-agent/"*.md "$WORK_DIR/" 2>/dev/null || true

# Create docs directory structure in final-output if it doesn't exist
DOCS_DIR="$PROJECT_ROOT/final-output/docs/$PROGRAM"
if [ ! -d "$DOCS_DIR" ]; then
    echo "✓ Creating documentation directory: $DOCS_DIR"
    mkdir -p "$DOCS_DIR"
fi

# Generate the LLM prompt file
echo "✓ Generating LLM prompt..."
cat > "$WORK_DIR/PROMPT.md" <<EOF
# Phase 7: Integration - Program: $PROGRAM

$(cat "$PROJECT_ROOT/agents/07-integration-agent/PROMPT.md")
EOF

echo "✓ Prompt generated: $WORK_DIR/PROMPT.md"
echo ""

echo "========================================="
echo "Setup Complete!"
echo "========================================="
echo ""
echo "Next Steps:"
echo ""
echo "1. Review the prompt:"
echo "   cat \"$WORK_DIR/PROMPT.md\""
echo ""
echo "2. Copy to clipboard (macOS):"
echo "   cat \"$WORK_DIR/PROMPT.md\" | pbcopy"
echo ""
echo "3. Or use with Claude Code:"
echo "   cd \"$PROJECT_ROOT\""
echo "   claude"
echo "   # Then paste the prompt"
echo ""
echo "4. Paste into your preferred LLM and let it integrate"