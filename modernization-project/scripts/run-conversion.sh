#!/bin/bash
# Run conversion agent on a specific program
# This script prepares the workspace and generates an LLM-agnostic prompt

PROGRAM=$1
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

if [ -z "$PROGRAM" ]; then
    echo "Usage: ./run-conversion.sh PROGRAM_NAME"
    exit 1
fi

echo "========================================="
echo "Phase 3: Conversion Agent Setup"
echo "Program: $PROGRAM"
echo "========================================="
echo ""

# Verify prerequisites
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

# Create work directory (clean if exists)
WORK_DIR="$PROJECT_ROOT/work-in-progress/$PROGRAM/03-conversion"
if [ -d "$WORK_DIR" ]; then
    echo "✓ Cleaning existing workspace: $WORK_DIR"
    rm -rf "$WORK_DIR"
fi
mkdir -p "$WORK_DIR/services"
mkdir -p "$WORK_DIR/controllers"
mkdir -p "$WORK_DIR/dtos"

# Copy templates and patterns
echo "✓ Creating workspace: $WORK_DIR"
cp -r "$PROJECT_ROOT/agents/03-conversion-agent/"*.java "$WORK_DIR/" 2>/dev/null || true
cp -r "$PROJECT_ROOT/agents/03-conversion-agent/"*.md "$WORK_DIR/" 2>/dev/null || true

# Generate the LLM prompt file
echo "✓ Generating LLM prompt..."
cat > "$WORK_DIR/PROMPT.md" <<EOF
# Phase 3: Conversion - Program: $PROGRAM

$(cat "$PROJECT_ROOT/agents/03-conversion-agent/PROMPT.md")
EOF

echo "✓ Prompt generated: $WORK_DIR/PROMPT.md"
echo ""
echo "========================================="
echo "Setup Complete!"
echo "========================================="
echo ""
echo "Next Steps:"
echo ""
echo "1. Copy prompt to clipboard:"
echo "   cat \"$WORK_DIR/PROMPT.md\" | pbcopy"
echo ""
echo "2. Paste into your preferred LLM"
echo ""
echo "Expected Outputs:"
echo "   - $WORK_DIR/services/*.java"
echo "   - $WORK_DIR/controllers/*.java"
echo "   - $WORK_DIR/dtos/*.java"
echo "   - $WORK_DIR/conversion-notes.md"
echo ""
