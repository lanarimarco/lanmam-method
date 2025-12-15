#!/bin/bash
# Run analysis agent on a specific program

PROGRAM=$1
PROJECT_ROOT="/Users/lana/Documents/dev/java/smeup/lanmam-method/modernization-project"

if [ -z "$PROGRAM" ]; then
    echo "Usage: ./run-analysis.sh PROGRAM_NAME"
    exit 1
fi

echo "Running analysis for $PROGRAM..."

# Create work directory
mkdir -p "$PROJECT_ROOT/work-in-progress/$PROGRAM/01-analysis"

# Instructions for Claude Code
echo "Analyze RPGLE program: $PROJECT_ROOT/source-rpgle/programs/$PROGRAM.rpgle"
echo "Follow instructions in: $PROJECT_ROOT/agents/01-analysis-agent/README.md"
echo "Output to: $PROJECT_ROOT/work-in-progress/$PROGRAM/01-analysis/"
echo ""
echo "Reference:"
echo "  - Analysis template: $PROJECT_ROOT/agents/01-analysis-agent/analysis-template.md"
echo "  - Checklist: $PROJECT_ROOT/agents/01-analysis-agent/checklist.md"
