#!/bin/bash
# Run analysis agent on a specific program
# This script prepares the workspace and generates an LLM-agnostic prompt

PROGRAM=$1
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

if [ -z "$PROGRAM" ]; then
    echo "Usage: ./run-analysis.sh PROGRAM_NAME"
    exit 1
fi

echo "========================================="
echo "Phase 1: Analysis Agent Setup"
echo "Program: $PROGRAM"
echo "========================================="
echo ""

# Verify source file exists
SOURCE_FILE="$PROJECT_ROOT/source-rpgle/programs/$PROGRAM.rpgle"
if [ ! -f "$SOURCE_FILE" ]; then
    echo "❌ Error: Source file not found: $SOURCE_FILE"
    echo "  Please add your RPGLE source file before running the analysis."
    exit 1
fi

# Create work directory (clean if exists)
WORK_DIR="$PROJECT_ROOT/work-in-progress/$PROGRAM/01-analysis"
if [ -d "$WORK_DIR" ]; then
    echo "✓ Cleaning existing workspace: $WORK_DIR"
    rm -rf "$WORK_DIR"
fi
mkdir -p "$WORK_DIR"

# Copy templates to work directory
echo "✓ Creating workspace: $WORK_DIR"
cp "$PROJECT_ROOT/agents/01-analysis-agent/analysis-template.md" "$WORK_DIR/"
cp "$PROJECT_ROOT/agents/01-analysis-agent/checklist.md" "$WORK_DIR/"


# Generate the LLM prompt file
echo "✓ Generating LLM prompt..."
cat > "$WORK_DIR/PROMPT.md" <<EOF
# Phase 1: Analysis - Program: $PROGRAM

$(cat "$PROJECT_ROOT/agents/01-analysis-agent/PROMPT.md")
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
echo "Expected Output:"
echo "   $WORK_DIR/$PROGRAM-analysis.md"
echo ""
