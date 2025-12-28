#!/bin/bash
# Run review agent on a specific program
# This script prepares the workspace and generates an LLM-agnostic prompt

PROGRAM=$1
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

if [ -z "$PROGRAM" ]; then
    echo "Usage: ./run-review.sh PROGRAM_NAME"
    exit 1
fi

echo "========================================="
echo "Phase 6: Review Agent Setup"
echo "Program: $PROGRAM"
echo "========================================="
echo ""

# Verify prerequisites
ANALYSIS_FILE="$PROJECT_ROOT/work-in-progress/$PROGRAM/01-analysis/$PROGRAM-analysis.md"
if [ ! -f "$ANALYSIS_FILE" ]; then
    echo "⚠️  Warning: Analysis document not found: $ANALYSIS_FILE"
    echo "  Please complete Phase 1 (Analysis) first"
    echo ""
fi

DATABASE_DIR="$PROJECT_ROOT/work-in-progress/$PROGRAM/02-database"
if [ ! -d "$DATABASE_DIR" ]; then
    echo "⚠️  Warning: Database layer not found: $DATABASE_DIR"
    echo "  Please complete Phase 2 (Database) first"
    echo ""
fi

CONVERSION_DIR="$PROJECT_ROOT/work-in-progress/$PROGRAM/03-conversion"
if [ ! -d "$CONVERSION_DIR" ]; then
    echo "⚠️  Warning: Conversion layer not found: $CONVERSION_DIR"
    echo "  Please complete Phase 3 (Conversion) first"
    echo ""
fi

UI_DIR="$PROJECT_ROOT/work-in-progress/$PROGRAM/04-ui"
if [ ! -d "$UI_DIR" ]; then
    echo "⚠️  Warning: UI layer not found: $UI_DIR"
    echo "  Please complete Phase 4 (UI) first"
    echo ""
fi

TESTING_DIR="$PROJECT_ROOT/work-in-progress/$PROGRAM/05-testing"
if [ ! -d "$TESTING_DIR" ]; then
    echo "⚠️  Warning: Testing layer not found: $TESTING_DIR"
    echo "  Please complete Phase 5 (Testing) first"
    echo ""
fi

# Create work directory (clean if exists)
WORK_DIR="$PROJECT_ROOT/work-in-progress/$PROGRAM/06-review"
if [ -d "$WORK_DIR" ]; then
    echo "✓ Cleaning existing workspace: $WORK_DIR"
    rm -rf "$WORK_DIR"
fi
mkdir -p "$WORK_DIR"

# Copy templates
echo "✓ Creating workspace: $WORK_DIR"
cp -r "$PROJECT_ROOT/agents/06-review-agent/"*.md "$WORK_DIR/" 2>/dev/null || true


# Generate the LLM prompt file
echo "✓ Generating LLM prompt..."
cat > "$WORK_DIR/PROMPT.md" <<EOF
# Phase 6: Review - Program: $PROGRAM

$(cat "$PROJECT_ROOT/agents/06-review-agent/PROMPT.md")
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
echo "3. Paste into your preferred LLM"
echo ""
