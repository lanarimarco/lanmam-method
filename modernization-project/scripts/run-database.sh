#!/bin/bash
# Run database agent on a specific program
# This script prepares the workspace and generates an LLM-agnostic prompt

PROGRAM=$1
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

if [ -z "$PROGRAM" ]; then
    echo "Usage: ./run-database.sh PROGRAM_NAME"
    exit 1
fi

echo "========================================="
echo "Phase 2: Database Agent Setup"
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

# Create work directory (clean if exists)
WORK_DIR="$PROJECT_ROOT/work-in-progress/$PROGRAM/02-database"
if [ -d "$WORK_DIR" ]; then
    echo "✓ Cleaning existing workspace: $WORK_DIR"
    rm -rf "$WORK_DIR"
fi
mkdir -p "$WORK_DIR"
mkdir -p "$WORK_DIR"

# Copy templates to work directory
echo "✓ Creating workspace: $WORK_DIR"
cp "$PROJECT_ROOT/agents/02-database-agent/entity-template.java" "$WORK_DIR/"
cp "$PROJECT_ROOT/agents/02-database-agent/repository-template.java" "$WORK_DIR/"
cp "$PROJECT_ROOT/agents/02-database-agent/type-mappings.md" "$WORK_DIR/"
cp "$PROJECT_ROOT/agents/02-database-agent/naming-conventions.md" "$WORK_DIR/"


# Generate the LLM prompt file
echo "✓ Generating LLM prompt..."
cat > "$WORK_DIR/PROMPT.md" <<EOF
# Phase 2: Database - Program: $PROGRAM

$(cat "$PROJECT_ROOT/agents/02-database-agent/PROMPT.md")
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
echo "   - $WORK_DIR/entities/*.java"
echo "   - $WORK_DIR/repositories/*.java"
echo "   - $WORK_DIR/database-notes.md"
echo ""
