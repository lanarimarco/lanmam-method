#!/bin/bash
# Run UI agent on a specific program
# This script prepares the workspace and generates an LLM-agnostic prompt

PROGRAM=$1
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

if [ -z "$PROGRAM" ]; then
    echo "Usage: ./run-ui.sh PROGRAM_NAME"
    exit 1
fi

echo "========================================="
echo "Phase 4: UI Agent Setup"
echo "Program: $PROGRAM"
echo "========================================="
echo ""

# Verify prerequisites
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

# Create work directory (clean if exists)
WORK_DIR="$PROJECT_ROOT/work-in-progress/$PROGRAM/04-ui"
if [ -d "$WORK_DIR" ]; then
    echo "✓ Cleaning existing workspace: $WORK_DIR"
    rm -rf "$WORK_DIR"
fi
mkdir -p "$WORK_DIR"

# Copy templates and guides
echo "✓ Creating workspace: $WORK_DIR"
cp -r "$PROJECT_ROOT/agents/04-ui-agent/"*.md "$WORK_DIR/" 2>/dev/null || true
cp -r "$PROJECT_ROOT/agents/04-ui-agent/"*.tsx "$WORK_DIR/" 2>/dev/null || true


# Generate the LLM prompt file
echo "✓ Generating LLM prompt..."
cat > "$WORK_DIR/PROMPT.md" <<EOF
# Phase 4: UI - Program: $PROGRAM

$(cat "$PROJECT_ROOT/agents/04-ui-agent/PROMPT.md")
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
